package br.ufrn.ct.cronos.domain.service;

import br.ufrn.ct.cronos.domain.exception.*;
import br.ufrn.ct.cronos.domain.model.*;
import br.ufrn.ct.cronos.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

import br.ufrn.ct.cronos.domain.filter.TurmaFilter;
import br.ufrn.ct.cronos.core.utils.ManipuladorHorarioTurma;
import br.ufrn.ct.cronos.infrastructure.repository.spec.TurmaSpecs;

@Service
public class CadastroTurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private CadastroPerfilSalaTurmaService perfilSalaTurmaService;

    @Autowired
    private CadastroHorarioService horarioService;

    @Autowired
    private CadastroPredioService predioService;

    @Autowired
    private CadastroPeriodoService periodoService;

    @Autowired
    private CadastroDepartamentoService departamentoService;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ManipuladorHorarioTurma manipuladorHorarioTurma;

    private static final String MSG_TURMA_JA_EXISTE = "Já existe um cadastro de Turma com os mesmos parâmetros informados para os campos: Código do Componente Curricular, Horário, Número e Período.";

    private static final String MSG_TURMA_DENTRO_DO_PERIODO_LETIVO = "A Turma não pode ser excluída, pois está alocada em uma Sala.";

    public Page<Turma> pesquisar(TurmaFilter filtro, Pageable pageable) {
        Page<Turma> turmasPage = turmaRepository.findAll(TurmaSpecs.usandoFiltro(filtro), pageable);

        for (Turma turma : turmasPage.getContent()) {
            List<Sala> salas = this.salaRepository.findByTurma(turma.getId());
            if (salas.size() > 0) {
                Sala[] arraySalas = (Sala[]) salas.toArray(new Sala[salas.size()]);
                turma.setSala("");
                for (int i = 0; i < arraySalas.length; i++) {
                    turma.setSala(turma.getSala() + arraySalas[i].getNome());
                    for (int h = 0; h < manipuladorHorarioTurma.contadorDeGruposComSabado(turma.getHorario()); h++) {
                        String grupo = manipuladorHorarioTurma.retornaGrupoComSabado(turma.getHorario(), h);
                        String turno = manipuladorHorarioTurma.retornaTurno(grupo);
                        String[] horariosDoGrupo = manipuladorHorarioTurma.retornaArrayHorarios(grupo);
                        String[] diasDoGrupo = manipuladorHorarioTurma.retornaArrayDias(grupo);

                        List<String> stringsDias = new ArrayList<String>(0);
                        for (int z = 0; z < diasDoGrupo.length; z++) {
                            stringsDias.add(diasDoGrupo[z]);
                        }
                        List<Long> idsHorarios = new ArrayList<Long>(0);
                        for (int z = 0; z < horariosDoGrupo.length; z++) {
                            idsHorarios.add(this.horarioService.findByTurnoAndHorario(turno, Integer.parseInt(horariosDoGrupo[z])).getId());
                        }
                        List<String> horarios =
                                this.turmaRepository.getHorariosPorTurmaESala(turma, arraySalas[i], turno, idsHorarios, stringsDias);
                        String hs = "";
                        String dias = "";
                        for (int r = 0; r < horarios.size(); r++) {
                            if (!manipuladorHorarioTurma.jaExisteHorario(hs, manipuladorHorarioTurma.retornaHorario(horarios.get(r)))) {
                                hs += manipuladorHorarioTurma.retornaHorario(horarios.get(r));
                            }
                            if (r == horarios.size() - 1) {
                                String dia = manipuladorHorarioTurma.retornaDia(horarios.get(r));
                                if (!manipuladorHorarioTurma.jaExisteDia(dias, dia)) {
                                    dias += dia;
                                }
                                turma.setSala(turma.getSala() + " (" + dias + manipuladorHorarioTurma.retornaTurno(horarios.get(r)) + hs + ") ");
                            } else {
                                String dia = manipuladorHorarioTurma.retornaDia(horarios.get(r));
                                if (!manipuladorHorarioTurma.jaExisteDia(dias, dia)) {
                                    dias += dia;
                                }
                            }
                        }
                    }
                }
            } else {
                turma.setSala("INDEFINIDO");
            }
        }

        return turmasPage;
    }

    @Transactional
    public Turma salvar (Turma turma) {
        turma = preparaObjectDomain(turma);
        // Validação da Turma
        validaTurma(turma);

        salvaOuAtualizaTurmaComSeusDocentes(turma);
        
        return turma;
    }

    @Transactional
    public Turma atualizar (Turma turma) {
        turmaRepository.detach(turma);

        turma = preparaObjectDomain(turma);

        validaTurma(turma);

        salvaOuAtualizaTurmaComSeusDocentes(turma);

        return turma;
    }

    @Transactional
    public void excluir(Long turmaid) {
        removerDocentesVinculadosATurma(turmaid);

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

    private void removerDocentesVinculadosATurma(Long turmaid) {
        Turma turma = turmaRepository.findByIdTurma(turmaid);

        if (Objects.nonNull(turma)) {
            Set<Funcionario> docentesList = turma.getDocentes();
            turma.getDocentes().clear();

            for (Funcionario funcionario : docentesList) {
                Funcionario docente = funcionarioRepository.findById(funcionario.getId()).orElseThrow(() -> new FuncionarioNaoEncontradoException(funcionario.getId()));

                turma.removeDocente(docente);
            }
        }

    }

    private Turma preparaObjectDomain(Turma turma) {
        Optional<Integer> optionalQuantidadeAlunosMatriculados = Optional.ofNullable(turma.getAlunosMatriculados());
        if (!optionalQuantidadeAlunosMatriculados.isPresent()) {
            turma.setAlunosMatriculados(0);
        }

        Long idPerfil = turma.getPerfil().getId();
        Long idPredio = turma.getPredio().getId();
        Long idPeriodo = turma.getPeriodo().getId();
        Long idDeparatamento = turma.getDepartamento().getId();

        PerfilSalaTurma perfil = perfilSalaTurmaService.buscar(idPerfil);
        Predio predio = predioService.buscar(idPredio);
        Periodo periodo = periodoService.buscar(idPeriodo);
        Departamento departamento = departamentoService.buscar(idDeparatamento);

        turma.setPerfil(perfil);
        turma.setPredio(predio);
        turma.setPeriodo(periodo);
        turma.setDepartamento(departamento);

        return turma;
    }
    private void validaTurma(Turma turma) {
        Optional<Turma> resultadoDaBusca;

        if (Objects.isNull(turma.getId())) {
            resultadoDaBusca = turmaRepository.buscarTurmaComMesmoParametro(turma.getCodigoDisciplina(), turma.getHorario(), turma.getNumero(), turma.getPeriodo().getId());
        } else {
            resultadoDaBusca = turmaRepository.buscarTurmaComMesmoParametro(turma.getCodigoDisciplina(), turma.getHorario(), turma.getNumero(), turma.getPeriodo().getId(), turma.getId());
        }

        if (resultadoDaBusca.isPresent()) {
            throw new NegocioException(MSG_TURMA_JA_EXISTE);
        }
    }

    public Turma buscarOuFalhar(Long turmaId) {
        return turmaRepository.findById(turmaId)
                .orElseThrow(() -> new TurmaNaoEncontradaException(turmaId));
    }

    public void salvaOuAtualizaTurmaComSeusDocentes(Turma turma) {
        /*
         * O fluxo (salvar a Turma e relacioná-las com seus docentes) a seguir é divido em 5 passos:
         * Passo 1: Fazendo uma cópia/backup da lista de Docentes da Turma, antes de limpa-la
         * Passo 2: Limpando a lista de docentes da Turma
         * Passo 3: Salvando a Turma
         * Passo 4: Adicionando os docentes de volta a Turma
         * passo 5: Atualizando a Turma com seus docentes
         */
        
        // Passo 1
        Set<Funcionario> docentesList = new HashSet<>();
        if (Objects.nonNull(turma.getDocentes())) {
            for (Funcionario docente : turma.getDocentes()) {
                docentesList.add(docente);
            }
        }

        // Passo 2
        if (Objects.nonNull(turma.getDocentes())) {
            turma.getDocentes().clear();
        }
        
        // Passo 3
        turma = turmaRepository.save(turma);
 
        // Passo 4
        for (Funcionario docente : docentesList) {
            Funcionario funcionario = funcionarioRepository.findById(docente.getId()).orElseThrow(() -> new FuncionarioNaoEncontradoException(docente.getId()));
            turma.addDocente(funcionario);
        }
        
        // Passo 5
        this.turmaRepository.save(turma);
    }

}
