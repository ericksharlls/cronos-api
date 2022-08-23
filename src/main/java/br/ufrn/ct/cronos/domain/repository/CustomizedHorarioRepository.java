package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.model.Horario;

public interface CustomizedHorarioRepository {

    public Horario getByTurnoEHorario(String turno, Integer horario);
    
}
