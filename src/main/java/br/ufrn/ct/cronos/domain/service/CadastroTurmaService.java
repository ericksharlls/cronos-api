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

    private static final String MSG_CODIGO_DO_COMPONENTE_CURRICULAR_JA_EXISTE = "O codigo do componente curricular inserido não pode ser utilizado, pois já está em uso";

    private static final String MSG_HORARIO_JA_EXISTE = "O horario inserido não pode ser utilizado, pois já está em uso";

    private static final String MSG_NUMERO_JA_EXISTE = "O numero da turma inserido não pode ser utilizado, pois já está em uso";

    private static final String MSG_PERIODO_JA_EXISTE = "O periodo inserido não pode ser utilizado, pois já está em uso";

    private static final String MSG_TURMA_NAO_EXISTE = "A turma informada não existe";

    private static final String MSG_TURMA_DENTRO_DO_PERIODO_LETIVO = "A turma não pode ser excluida pois encontra-se em periodo letivo";

    // TODO Erick Shalls
    public void buscar () {

    }

    @Transactional
    public Turma salvar (Turma turma) {
        turmaRepository.detach(turma);

        turma = preparaObjectDomain(turma);

        validaTurma(turma);

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

        // Trecho que deve ser removido ou tratado quando a informação for consumida da API
        turma.setIdTurmaSIGAA(15L);

        return turma;
    }
    private void validaTurma(Turma turma) {
        List<Turma> resultadosDaBusca = turmaRepository.buscarTurmaComMesmoParametro(turma.getCodigoDisciplina(), turma.getHorario(), turma.getNumero(), turma.getPeriodo().getId());

        if (!Objects.isNull(resultadosDaBusca)) {

            for (Turma turmaResultadoDaBusca: resultadosDaBusca) {
                if (turmaResultadoDaBusca.getCodigoDisciplina().equals(turma.getCodigoDisciplina()) && turmaResultadoDaBusca.getId() != turma.getId()) {
                    throw new NegocioException(MSG_CODIGO_DO_COMPONENTE_CURRICULAR_JA_EXISTE);

                } else if (turmaResultadoDaBusca.getHorario().equals(turma.getHorario()) && turmaResultadoDaBusca.getId() != turma.getId()) {
                    throw new NegocioException(MSG_HORARIO_JA_EXISTE);

                } else if (turmaResultadoDaBusca.getNumero().equals(turma.getNumero()) && turmaResultadoDaBusca.getId() != turma.getId()) {
                    throw new NegocioException(MSG_NUMERO_JA_EXISTE);

                } else if (turmaResultadoDaBusca.getPeriodo().getId().equals(turma.getPeriodo().getId()) && turmaResultadoDaBusca.getId() != turma.getId()) {
                    throw new NegocioException(MSG_PERIODO_JA_EXISTE);

                }
            }
        }
    }

    public Turma buscarOuFalhar(Long turmaId) {
        return turmaRepository.findById(turmaId)
                .orElseThrow(() -> new SalaNaoEncontradaException(turmaId));
    }
}
