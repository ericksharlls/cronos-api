package br.ufrn.ct.cronos.domain.service;

import br.ufrn.ct.cronos.domain.conversor.Conversor;
import br.ufrn.ct.cronos.domain.model.*;
import br.ufrn.ct.cronos.domain.model.dto.DocenteDTO;
import br.ufrn.ct.cronos.domain.model.dto.TurmaDTO;
import br.ufrn.ct.cronos.domain.model.enumeracoes.StatusImportacaoTurmasEnum;
import br.ufrn.ct.cronos.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ImportarTurmasPorUnidadeService {

    @Autowired
    private ImportacaoTurmasRepository importacaoTurmasRepository;
    
    @Autowired
    private HistoricoImportacaoTurmasRepository historicoImportacaoTurmasRepository;

    @Autowired
    private CadastroDepartamentoService departamentoService;

    @Autowired
    private CadastroStatusImportacaoTurmasService statusImportacaoTurmasService;

    @Autowired
    private TurmaApiUfrnRepository turmaApiUfrnRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PeriodoRepository periodoRepository;

    @Transactional
    public void importarTurmas(Set<String> siglasNivelEnsino, ImportacaoTurmas importacao, Long idPeriodo) {
        Periodo periodo = this.periodoRepository.getById(idPeriodo);
        Departamento departamento = departamentoService.getByIdSigaa(importacao.getDepartamento().getIdSigaa());
        List<TurmaDTO> turmasSigaa =  turmaApiUfrnRepository
                                .retornaTurmasAbertasPorSiglaNivelEnsinoIdDepartamentoAnoPeriodo
                                    (siglasNivelEnsino, Integer.valueOf(departamento.getIdSigaa().intValue()), Integer.valueOf(periodo.getAno()), Integer.valueOf(periodo.getNumero()));
        /**
         * Eliminando as turmas AGRUPADORAS. Turmas AGRUPADORAS funcionam como turmas "mãe" de outras turmas. Apenas turmas "filhas" devem ser
         * consideradas, pois os alunos são matriculados nas "filhas", e fazem o uso da turma virtual apenas das "filhas" tambem.
        */
        turmasSigaa = turmasSigaa
                    .stream()
                    .filter(t -> t.getAgrupadora().equals(false))
                    .collect(Collectors.toList());
        System.out.println("##### (" + departamento.getNome() + ") Total de Turmas: " + turmasSigaa.size());
               
        // Obtendo a lista de turmas locais por departamento e período
        List<Turma> listaTurmasLocais = this.turmaRepository.findByDepartamentoAndPeriodo(departamento.getId(), periodo.getId());
                
        Map<Long, Long> mapaTurmasLocais = new HashMap<Long, Long>();
        // Montando mapa com chave sendo o ID da Turma Local no SIGAA, e como valor o proprio ID da turma
        for (Turma turmaLocal : listaTurmasLocais) {
            mapaTurmasLocais.put(turmaLocal.getIdTurmaSIGAA(), turmaLocal.getId());
        }

        for (TurmaDTO turmaDTO : turmasSigaa) {
            // Se a turma nao existir no mapaTurmasLocais, eh pq a turma eh nova, logo, sera salva. (ENTRARA no PROXIMO IF)
            // Caso contrario, sera atualizada (ENTRARA NO ELSE)
            if (!mapaTurmasLocais.containsKey(Long.valueOf(turmaDTO.getId()))) {
                salvarDocentes(turmaDTO.getDocentesList());
                Turma turmaParaCadastro = Conversor.convertTurmaDtoToTurmaDomainObject(turmaDTO);
                turmaParaCadastro.setDepartamento(departamento);
                turmaParaCadastro.setPeriodo(periodo);
                turmaRepository.save(turmaParaCadastro);
            } else {
                salvarDocentes(turmaDTO.getDocentesList());
                Turma turmaParaAtualizacao = turmaRepository.findById(mapaTurmasLocais.get(Long.valueOf(turmaDTO.getId()))).get();
                Conversor.transferTurmaDtoResumidoToTurmaDomainObject(turmaDTO, turmaParaAtualizacao);
                turmaRepository.save(turmaParaAtualizacao);
            }
        }
        salvandoRelacionamentoTurmasComDocentes(turmasSigaa);
        deletarTurmasQueNaoExistemMais(turmasSigaa, listaTurmasLocais);

        StatusImportacaoTurmas status = statusImportacaoTurmasService.getByIdentificador(StatusImportacaoTurmasEnum.EXECUTADA_COM_SUCESSO.name());
        HistoricoImportacaoTurmas historico = new HistoricoImportacaoTurmas();

        importacao.setStatus(status);
        importacao.setDepartamento(departamento);
        importacao.setHorarioUltimaOperacao(LocalDateTime.now());
        importacaoTurmasRepository.save(importacao);

        historico.setImportacaoTurmas(importacao);
        historico.setRegistradoEm(LocalDateTime.now());
        historico.setStatus(status);
        historicoImportacaoTurmasRepository.save(historico);
    }

    /**
    * Salvando/Atualizando os dados do(s) docente(s) de uma turma especifica
    */
    private void salvarDocentes(List<DocenteDTO> docentesList) {
        for (DocenteDTO docenteDTO : docentesList) {
            Funcionario funcionario = this.funcionarioRepository.findByIdSigaaFuncionario(docenteDTO.getIdServidor());
            if (funcionario == null) {
                Funcionario docente = new Funcionario();
                docente.setNome(docenteDTO.getNome());
                docente.setIdSigaaFuncionario(docenteDTO.getIdServidor());

                TipoFuncionario tipoFuncionario = new TipoFuncionario();
                tipoFuncionario.setId(Long.valueOf(1));
                docente.setTipoFuncionario(tipoFuncionario);
                this.funcionarioRepository.save(docente);
            } else {
                funcionario.setNome(docenteDTO.getNome());
                funcionario.setIdSigaaFuncionario(docenteDTO.getIdServidor());

                TipoFuncionario tipoFuncionario = new TipoFuncionario();
                tipoFuncionario.setId(Long.valueOf(1));
                funcionario.setTipoFuncionario(tipoFuncionario);
                this.funcionarioRepository.save(funcionario);
            }
        }
    }

     /**
    * Removendo os relacionamentos antigos e salvando os novos entre turmas e seus respectivos docentes
    */
    private void salvandoRelacionamentoTurmasComDocentes(List<TurmaDTO> turmasSIGAA) {
        for (TurmaDTO turmaDTO : turmasSIGAA) {
           if (turmaDTO.getDocentesList().size() > 0) {
                Turma turma = this.turmaRepository.findByIdTurmaSIGAA(Long.valueOf(turmaDTO.getId()));
                turma.getDocentes().clear();
                for (DocenteDTO docenteDTO : turmaDTO.getDocentesList()) {
                    Funcionario docente = this.funcionarioRepository.findByIdSigaaFuncionario(docenteDTO.getIdServidor());
                    turma.addDocente(docente);
                }
                this.turmaRepository.save(turma);
           }
        }
    }

    /**
    * Removendo as turmas que existiam no SIGAA em importacao anterior, mas que na importacao atual nao existem mais
    */
    private void deletarTurmasQueNaoExistemMais(List<TurmaDTO> turmasSigaa, List<Turma> listaTurmasLocais) {
        Map<Integer, Integer> mapaTurmasSigaa = new HashMap<Integer, Integer>();
        for (TurmaDTO turmaDTO : turmasSigaa) {
            mapaTurmasSigaa.put(turmaDTO.getId(), turmaDTO.getId());
        }
        for (Turma turmaLocal : listaTurmasLocais) {
            if (!turmaLocal.getIdTurmaSIGAA().equals(Long.valueOf(0)) &&
                !mapaTurmasSigaa.containsKey(turmaLocal.getIdTurmaSIGAA().intValue())) {
                Turma turmaParaRemocao = this.turmaRepository.findById(turmaLocal.getId()).get();
                this.turmaRepository.delete(turmaParaRemocao);
            }
        }
    }
    
}
