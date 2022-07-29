package br.ufrn.ct.cronos.domain.repository;

import java.util.List;

import br.ufrn.ct.cronos.domain.model.Sala;
import br.ufrn.ct.cronos.domain.model.Turma;

public interface CustomizedTurmaRepository {

    public List<String> getHorariosPorTurmaESala(Turma turma, Sala sala, String turno, List<Long> listaIdsHorarios, 
                            List<String> listaStringsDias);
    
}
