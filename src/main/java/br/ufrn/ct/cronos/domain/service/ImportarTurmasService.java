package br.ufrn.ct.cronos.domain.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.ct.cronos.domain.exception.ImportacaoTurmasNaoEncontradaException;
import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.exception.PeriodoNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Departamento;
import br.ufrn.ct.cronos.domain.model.HistoricoImportacaoTurmas;
import br.ufrn.ct.cronos.domain.model.ImportacaoTurmas;
import br.ufrn.ct.cronos.domain.model.NivelEnsinoTurmaEnum;
import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.model.StatusImportacaoTurmas;
import br.ufrn.ct.cronos.domain.model.enumeracoes.StatusImportacaoTurmasEnum;
import br.ufrn.ct.cronos.domain.repository.HistoricoImportacaoTurmasRepository;
import br.ufrn.ct.cronos.domain.repository.ImportacaoTurmasRepository;

@Service
public class ImportarTurmasService {
    
    private static final String VALOR_INVALIDO_SIGLA_NIVEL_ENSINO_TURMA 
        = "Não existe um Nível de Ensino de Turma com a sigla '%s'.";
    
    private static final String ID_DEPARTAMENTO_NAO_ENCONTRADO 
        = "Não existe Departamento na API da UFRN com id %d.";

    private static final String ID_PERIODO_NAO_ENCONTRADO 
        = "Não existe Período com id %d.";

    private static final String STATUS_NAO_APTO_PARA_REEXECUCAO
        = "A importação de id %d não pode ser reexecutada, pois sua última execução foi realizada com sucesso.";

    @Autowired
    private ImportacaoTurmasRepository importacaoTurmasRepository;
    
    @Autowired
    private HistoricoImportacaoTurmasRepository historicoImportacaoTurmasRepository;

    @Autowired
    private CadastroDepartamentoService departamentoService;

    @Autowired
    private ImportarTurmasPorUnidadeService importarTurmasPorUnidadeService;

    @Autowired
    private CadastroStatusImportacaoTurmasService statusImportacaoTurmasService;

    @Autowired
    private CadastroPeriodoService cadastroPeriodoService;

    private List<ImportacaoTurmas> importacoes;
    private Set<String> listaSiglasNivelEnsino;
    private Long idPeriodo;

    public ImportarTurmasService(){
        this.importacoes = new ArrayList<>(0);
        this.listaSiglasNivelEnsino = new HashSet<>(0);
        this.idPeriodo = Long.valueOf(0);
    }

    @Transactional
    public void agendarImportacoes(Set<String> siglasNivelEnsino, Set<Long> idsUnidades, Long periodoIdParameter) {
        validarNivelEnsinoTurma(siglasNivelEnsino);
        validarIdsDepartamentos(idsUnidades);
        validarIdPeriodo(periodoIdParameter);
        idsUnidades.forEach(idDepartamentoSigaa -> {
            ImportacaoTurmas importacao = new ImportacaoTurmas();
            StatusImportacaoTurmas status = statusImportacaoTurmasService.getByIdentificador(StatusImportacaoTurmasEnum.CRIADA_AGUARDANDO_EXECUCAO.name());
            Departamento departamento = departamentoService.getByIdSigaa(idDepartamentoSigaa);
            HistoricoImportacaoTurmas historico = new HistoricoImportacaoTurmas();

            importacao.setStatus(status);
            importacao.setDepartamento(departamento);
            importacao.setHorarioUltimaOperacao(LocalDateTime.now());
            importacaoTurmasRepository.save(importacao);

            historico.setImportacaoTurmas(importacao);
            historico.setRegistradoEm(LocalDateTime.now());
            historico.setStatus(status);
            historicoImportacaoTurmasRepository.save(historico);
            importacoes.add(importacao);
        });
    }

    public ImportacaoTurmas buscar(Long idImportacaoTurmas) {
        ImportacaoTurmas importacaoTurmas = importacaoTurmasRepository.findById(idImportacaoTurmas)
			        .orElseThrow(() -> new ImportacaoTurmasNaoEncontradaException(idImportacaoTurmas));
        importacaoTurmas.setListaHistorico(historicoImportacaoTurmasRepository.findByImportacaoTurmas(idImportacaoTurmas));
		return importacaoTurmas;
	}

    public void agendarImportacaoPorId(Long idImportacaoTurmas, Set<String> siglasNivelEnsino, Long periodoIdParameter) {
        ImportacaoTurmas importacaoTurmas = importacaoTurmasRepository.findById(idImportacaoTurmas)
			        .orElseThrow(() -> new ImportacaoTurmasNaoEncontradaException(idImportacaoTurmas));
        if (importacaoTurmas.getStatus().getIdentificador().equals(StatusImportacaoTurmasEnum.EXECUTADA_COM_SUCESSO.toString())) {
            throw new NegocioException(String.format(STATUS_NAO_APTO_PARA_REEXECUCAO, idImportacaoTurmas));
        }
        validarNivelEnsinoTurma(siglasNivelEnsino);
        validarIdPeriodo(periodoIdParameter);
	}

    @Async
    public void executarAssincronamenteImportacoes() {
        this.importacoes.forEach(importacao -> {
            try {
                importarTurmasPorUnidadeService.importarTurmas(listaSiglasNivelEnsino, importacao, idPeriodo);
                System.out.println("### SUCESSO com o Departamento: " + importacao.getDepartamento().getNome());
            } catch (Exception e) {
                System.out.println("### Erro ao importar turmas do Departamento: " + importacao.getDepartamento().getNome());
                e.printStackTrace();
                StatusImportacaoTurmas status = statusImportacaoTurmasService.getByIdentificador(StatusImportacaoTurmasEnum.ERRO_NA_EXECUCAO.name());
                // Atualizando o status da importacao para ERRO_NA_EXECUCAO
                importacao.setStatus(status);
                importacao.setHorarioUltimaOperacao(LocalDateTime.now());
                importacaoTurmasRepository.save(importacao);

                // Registrando na tabela de Historico a atualizacao no Status da Importacao
                HistoricoImportacaoTurmas historico = new HistoricoImportacaoTurmas();

                historico.setImportacaoTurmas(importacao);
                historico.setRegistradoEm(LocalDateTime.now());
                historico.setStatus(status);
                historicoImportacaoTurmasRepository.save(historico);
            }
        });
        limparDados();
        System.out.println("#### Terminou o SERVICE ####");
    }

    @Async
    public void reexecutarAssincronamenteImportacao(Long idImportacaoTurmas) {
        ImportacaoTurmas importacao = importacaoTurmasRepository.findById(idImportacaoTurmas).get();
        try {
            importarTurmasPorUnidadeService
                .importarTurmas(listaSiglasNivelEnsino, importacao, idPeriodo);
            System.out.println("### SUCESSO com o Departamento: " + importacao.getDepartamento().getNome());
        } catch (Exception e) {
            System.out.println("### Erro ao importar turmas do Departamento: " + importacao.getDepartamento().getNome());
            e.printStackTrace();
            StatusImportacaoTurmas status = statusImportacaoTurmasService.getByIdentificador(StatusImportacaoTurmasEnum.ERRO_NA_EXECUCAO.name());
            // Atualizando o status da importacao para ERRO_NA_EXECUCAO
            importacao.setStatus(status);
            importacao.setHorarioUltimaOperacao(LocalDateTime.now());
            importacaoTurmasRepository.save(importacao);

            // Registrando na tabela de Historico a atualizacao no Status da Importacao
            HistoricoImportacaoTurmas historico = new HistoricoImportacaoTurmas();

            historico.setImportacaoTurmas(importacao);
            historico.setRegistradoEm(LocalDateTime.now());
            historico.setStatus(status);
            historicoImportacaoTurmasRepository.save(historico);
        }
    }

    private void validarNivelEnsinoTurma(Set<String> siglasNivelEnsino) {
        siglasNivelEnsino.forEach(siglaNivelEnsino -> {
            try {
                NivelEnsinoTurmaEnum.getBySigla(siglaNivelEnsino);
                listaSiglasNivelEnsino.add(siglaNivelEnsino);
            } catch (IllegalArgumentException e) {
                limparDados();
                throw new NegocioException(String.format(VALOR_INVALIDO_SIGLA_NIVEL_ENSINO_TURMA, siglaNivelEnsino));
            }
        });
    }

    private void validarIdsDepartamentos(Set<Long> idsDepartamentos) {
        for (Long idDepartamento : idsDepartamentos) {
            if(!departamentoService.getAllIdsSigaa().contains(idDepartamento)) {
                limparDados();
                throw new NegocioException(String.format(ID_DEPARTAMENTO_NAO_ENCONTRADO, idDepartamento));
            }   
        }
    }

    private void validarIdPeriodo(Long periodoIdParameter) {
        try {
            Periodo periodo = cadastroPeriodoService.buscar(periodoIdParameter);
            this.idPeriodo = periodo.getId();    
        } catch (PeriodoNaoEncontradoException e) {
            limparDados();
            throw new NegocioException(String.format(ID_PERIODO_NAO_ENCONTRADO, periodoIdParameter));
        }
    }

    private void limparDados() {
        this.importacoes = new ArrayList<>(0);
        this.listaSiglasNivelEnsino = new HashSet<>();
        this.idPeriodo = Long.valueOf(0);
    }

}
