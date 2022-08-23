package br.ufrn.ct.cronos.domain.repository;

import java.util.Date;
import java.util.List;

import br.ufrn.ct.cronos.domain.model.Sala;
import br.ufrn.ct.cronos.domain.model.Turma;

public interface CustomizedDisponibilidadeSalaRepository {

    public boolean verificarTurmaTemSala(Long idTurma);
    public boolean verificarDisponibilidadeSalaParaAgendamentos(String horario, Long idSala, List<Date> datas, Long idPeriodo);
    public Sala getSalaPorTurma(Long idTurma);
    public boolean verificarDisponibilidadeSala(Turma turma, Long idSala);
    
}
