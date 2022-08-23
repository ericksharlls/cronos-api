package br.ufrn.ct.cronos.domain.service.distribuicaoturmas;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.ct.cronos.core.utils.ManipuladorDatas;
import br.ufrn.ct.cronos.core.utils.ManipuladorHorarioTurma;
import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.exception.PeriodoNaoEncontradoException;
import br.ufrn.ct.cronos.domain.exception.PredioNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Funcionario;
import br.ufrn.ct.cronos.domain.model.Turma;
import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.model.DisponibilidadeSala;
import br.ufrn.ct.cronos.domain.model.Sala;
import br.ufrn.ct.cronos.domain.repository.DisponibilidadeSalaRepository;
import br.ufrn.ct.cronos.domain.repository.FeriadoRepository;
import br.ufrn.ct.cronos.domain.repository.FuncionarioRepository;
import br.ufrn.ct.cronos.domain.repository.HorarioRepository;
import br.ufrn.ct.cronos.domain.repository.PeriodoRepository;
import br.ufrn.ct.cronos.domain.repository.TurmaRepository;
import br.ufrn.ct.cronos.domain.service.CadastroPeriodoService;
import br.ufrn.ct.cronos.domain.service.CadastroPredioService;

@Service
public class DistribuicaoTurmasService {

    @Autowired
    private CadastroPeriodoService cadastroPeriodoService;

    @Autowired
    private CadastroPredioService cadastroPredioService;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private DisponibilidadeSalaRepository disponibilidadeSalaRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private FeriadoRepository feriadoRepository;

    @Autowired
    private ManipuladorHorarioTurma manipuladorHorarioTurma;

    @Async
    @Transactional
    public void executarDistribuicaoAssincronamente(Long idPeriodo, Long idPredio) {
        validarIdPeriodo(idPeriodo);
        validarIdPredio(idPredio);
        System.out.println("!! METODO PROCESSA -- Service do CT !!");
      
      Periodo periodo = this.periodoRepository.get(idPeriodo);

      // Passo 1 na Distribuicao - Distribuir as turmas q SAO CONSECUTIVAS E QUE EXISTIAM no semestre letivo anterior MAS que ESTAO SEM SALA

      // Obter os nomes de todos os docentes
      // for (String d : this.turmaDao.getAllNomesDocentes(idPeriodo, idPredio)) {
      for (Funcionario d : this.funcionarioRepository.getAllByTipoFuncionario(Long.valueOf(1))) { // ##### REFATORAR


         // Obter as turmas por docentes
         // for (Turma t1 : this.turmaDao.getTurmasPorDocente(d, idPeriodo, idPredio)) {
         for (Turma t1 : this.turmaRepository.getTurmasPorDocente(d.getId(), idPeriodo, idPredio)) { // ##### REFATORAR

            // Eliminar as turmas que tiver sem docente definido
            // if (!t1.getDocente().replaceAll(" ", "").equals("ADEFINIRDOCENTE")) { ##### DEIXAR COMENTADO (20-07-2017)

               if (manipuladorHorarioTurma.validarHorarioComSabado(t1.getHorario())) {
                  // Criando lista das turmas com horarios consecutivos
                  List<Turma> consecutivos = new ArrayList<Turma>(0);

                  // Montando lista das turmas com horarios consecutivos
               // for (Turma tu : this.turmaDao.getTurmasPorDocente(d, idPeriodo, idPredio)) {
               for (Turma tu : this.turmaRepository.getTurmasPorDocente(d.getId(), idPeriodo, idPredio)) { // ##### REFATORAR
                     if ((t1.getIdTipo().equals(tu.getIdTipo())) && (manipuladorHorarioTurma.saoConsecutivos(t1, tu)) && tu.getDistribuir()) {
                        if (!consecutivos.contains(t1)) {
                           consecutivos.add(t1);
                        }
                        consecutivos.add(tu);
                     }
                  }

                  // Iterando as turmas com horarios consecutivos
                  for (Turma turma : consecutivos) {

                     Periodo periodoLetivoAnterior = this.periodoRepository.getPeriodoLetivoAnterior();
                     // Verificar se existe Periodo Letivo Anterior no Sistema. Para o sistema comecando DO ZERO, NUNCA vai existir.
                     if (periodoLetivoAnterior != null) {
                        Turma turmaAnteriorSemelhanteUm =
                           this.turmaRepository.getTurmaAnteriorSemelhantePorPeriodo(turma, periodoLetivoAnterior.getId());
                     if (turmaAnteriorSemelhanteUm != null && !this.disponibilidadeSalaRepository.verificarTurmaTemSala(turma.getId())) {
                    	 
                    	 // Testes no dia 23/07/2018
                    	 //turmaAnteriorSemelhanteUm
                    	 
                           // Obter a sala dessa turma
                           Sala s = this.disponibilidadeSalaRepository.getSalaPorTurma(turmaAnteriorSemelhanteUm.getId());

                           // Verificar se jah tem sala mesmo
                           if (s != null) {

                              // Novamente iterando as mesmas turmas com horarios consecutivos
                              for (Turma t : consecutivos) {

                                 // Verificando se estou obtendo uma turma q n seja a q jah tem sala
                                 if (!t.equals(turma)) { // Qualquer problema, eh soh comentar essa linha e descomentar a de baixo
                                    // if (!consecutivos.equals(turma)) {

                                    // Verificar se a turma em questao jah tem sala.. Esse teste eh feito, pq corre o risco de mais de uma
                                    // turma
                                    // consecutiva jah ter uma sala. Caso tenha, permanecera onde estah
                                    if (!this.disponibilidadeSalaRepository.verificarTurmaTemSala(t.getId())) {

                                       if (this.disponibilidadeSalaRepository.verificarDisponibilidadeSala(t, s.getId())) {

                                          for (int h = 0; h < manipuladorHorarioTurma.contadorDeGruposComSabado(t.getHorario()); h++) {
                                             List<Long> idsHorarios = new ArrayList<Long>(0);
                                             String grupo = manipuladorHorarioTurma.retornaGrupoComSabado(t.getHorario(), h);
                                             String turno = manipuladorHorarioTurma.retornaTurno(grupo);
                                             String[] arrayHorarios = manipuladorHorarioTurma.retornaArrayHorarios(grupo);

                                             for (int k = 0; k < arrayHorarios.length; k++) {
                                                idsHorarios.add(this.horarioRepository.getByTurnoEHorario(turno,
                                                   Integer.parseInt(arrayHorarios[k]))
                                                         .getId());
                                             }
                                             List<Date> feriados = this.feriadoRepository.getDatasFeriadosByPeriodo(periodo.getId());
                                             List<Date> datasParaReserva =
                                                ManipuladorDatas.retornaDatasLetivasPorDias(DateUtils.datasEntre(periodo.getDataInicio(),
                                                   periodo.getDataTermino()), feriados, grupo);
                                             Collections.sort(datasParaReserva, new Comparator<Date>() {

                                                public int compare(Date d1, Date d2) {
                                                   return d1.compareTo(d2);
                                                }
                                             });

                                             for (Long idHorario : idsHorarios) {
                                                DisponibilidadeSala disponibilidadeSala = new DisponibilidadeSala();
                                                disponibilidadeSala.setIdHorarioSala(idHorario);
                                                disponibilidadeSala.setIdPeriodo(t.getIdPeriodo());
                                                disponibilidadeSala.setIdSala(s.getId());
                                                disponibilidadeSala.setIdTurma(t.getId());

                                                for (Date data : datasParaReserva) {
                                                   disponibilidadeSala.setDataReserva(data);
                                                   this.disponibilidadeSalaRepository.save(disponibilidadeSala);
                                                }
                                             }
                                          }
                                          t.setDistribuir(false);
                                          this.turmaRepository.merge(t);

                                          // Distribuindo o objeto turma
                                          if (!this.disponibilidadeSalaRepository.verificarTurmaTemSala(turma.getId())) {

                                             if (this.disponibilidadeSalaRepository.verificarDisponibilidadeSala(turma, s.getId())) {

                                                for (int h = 0; h < manipuladorHorarioTurma.contadorDeGruposComSabado(turma.getHorario()); h++) {
                                                   List<Long> idsHorarios = new ArrayList<Long>(0);
                                                   String grupo = manipuladorHorarioTurma.retornaGrupoComSabado(turma.getHorario(), h);
                                                   String turno = manipuladorHorarioTurma.retornaTurno(grupo);
                                                   String[] arrayHorarios = manipuladorHorarioTurma.retornaArrayHorarios(grupo);

                                                   for (int k = 0; k < arrayHorarios.length; k++) {
                                                      idsHorarios.add(this.horarioRepository.getByTurnoEHorario(turno,
                                                         Integer.parseInt(arrayHorarios[k]))
                                                               .getId());
                                                   }
                                                   List<Date> feriados = this.feriadoRepository.getDatasFeriadosByPeriodo(periodo.getId());
                                                   List<Date> datasParaReserva =
                                                      ManipuladorDatas.retornaDatasLetivasPorDias(
                                                         DateUtils.datasEntre(periodo.getDataInicio(),
                                                            periodo.getDataTermino()), feriados, grupo);
                                                   Collections.sort(datasParaReserva, new Comparator<Date>() {

                                                      public int compare(Date d1, Date d2) {
                                                         return d1.compareTo(d2);
                                                      }
                                                   });

                                                   for (Long idHorario : idsHorarios) {
                                                      DisponibilidadeSala disponibilidadeSala = new DisponibilidadeSala();
                                                      disponibilidadeSala.setIdHorarioSala(idHorario);
                                                      disponibilidadeSala.setIdPeriodo(turma.getIdPeriodo());
                                                      disponibilidadeSala.setIdSala(s.getId());
                                                      disponibilidadeSala.setIdTurma(turma.getId());

                                                      for (Date data : datasParaReserva) {
                                                         disponibilidadeSala.setDataReserva(data);
                                                         this.disponibilidadeSalaRepository.save(disponibilidadeSala);
                                                      }
                                                   }
                                                }
                                                turma.setDistribuir(false);
                                                this.turmaRepository.merge(turma);
                                             }
                                          }
                                          // Finalizada a distribuicao do objeto turma
                                       }

                                    }
                                 }
                              }
                           }
                        }
                     }

                  }
               }
            // }
         }

      }
      System.out.println("### FINALIZADO PASSO 1 ###");
    }

    private void validarIdPeriodo(Long idPeriodo) {
        try {
            cadastroPeriodoService.buscar(idPeriodo); 
        } catch (PeriodoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    private void validarIdPredio(Long idPredio) {
        try {
            cadastroPredioService.buscar(idPredio); 
        } catch (PredioNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }
    
}
