package br.ufrn.ct.cronos.domain.service;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.exception.SalaNaoEncontradaException;
import br.ufrn.ct.cronos.domain.exception.TurmaNaoEncontradaException;
import br.ufrn.ct.cronos.domain.model.*;
import br.ufrn.ct.cronos.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CadastroTurmaService {
    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private PerfilSalaTurmaRepository perfilSalaTurmaRepository;

    @Autowired
    private PredioRepository predioRepository;

    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    private static final String MSG_TURMA_JA_EXISTE = "Já existe turma com mesmos parâmetros: Código do Componente Curricular da Turma, Horário  da Turma, Número da Turma e Período.";

    private static final String MSG_TURMA_DENTRO_DO_PERIODO_LETIVO = "A turma não pode ser excluida pois encontra-se em periodo letivo";

    // TODO Erick Shalls
    public void buscar () {

    }

    @Transactional
    public Turma salvar (Turma turma) {
        turma = preparaObjectDomain(turma);

        validaTurmaNoCadastro(turma);

        return turmaRepository.save(turma);
    }

    @Transactional
    public Turma atualizar (Turma turma) {
        turmaRepository.detach(turma);

        turma = preparaObjectDomain(turma);

        validaTurmaNaAtualizacao(turma);

        return turmaRepository.save(turma);
    }

    @Transactional
    public void excluir(Long turmaid) {
        try {
            turmaRepository.deleteById(turmaid);
            turmaRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new TurmaNaoEncontradaException (turmaid);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_TURMA_DENTRO_DO_PERIODO_LETIVO, turmaid));
        }
    }

    private Turma preparaObjectDomain(Turma turma) {
        Long idPerfil = turma.getPerfil().getId();
        Long idPredio = turma.getPredio().getId();
        Long idPeriodo = turma.getPeriodo().getId();
        Long idDeparatamento = turma.getDepartamento().getId();

        PerfilSalaTurma perfil = perfilSalaTurmaRepository.findById(idPerfil).get();
        Predio predio = predioRepository.findById(idPredio).get();
        Periodo periodo = periodoRepository.findById(idPeriodo).get();
        Departamento departamento = departamentoRepository.findById(idDeparatamento).get();

        turma.setPerfil(perfil);
        turma.setPredio(predio);
        turma.setPeriodo(periodo);
        turma.setDepartamento(departamento);

        return turma;
    }
    private void validaTurmaNoCadastro(Turma turma) {
       Optional<Turma> resultadoDaBusca = turmaRepository.buscarTurmaComMesmoParametro(turma.getCodigoDisciplina(), turma.getHorario(), turma.getNumero(), turma.getPeriodo().getId());

        if (resultadoDaBusca.isPresent()) {
            throw new NegocioException(MSG_TURMA_JA_EXISTE);
        }
    }

    private void validaTurmaNaAtualizacao(Turma turma) {
        Optional<Turma> resultadoDaBusca = turmaRepository.buscarTurmaComMesmoParametro(turma.getCodigoDisciplina(), turma.getHorario(), turma.getNumero(), turma.getPeriodo().getId(), turma.getId());

        if (resultadoDaBusca.isPresent()) {
            throw new NegocioException(MSG_TURMA_JA_EXISTE);
        }
    }

    public Turma buscarOuFalhar(Long turmaId) {
        return turmaRepository.findById(turmaId)
                .orElseThrow(() -> new SalaNaoEncontradaException(turmaId));
    }
}
