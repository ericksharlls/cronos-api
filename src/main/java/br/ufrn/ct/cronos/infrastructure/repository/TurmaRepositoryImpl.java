package br.ufrn.ct.cronos.infrastructure.repository;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Sala;
import br.ufrn.ct.cronos.domain.model.Turma;
import br.ufrn.ct.cronos.domain.repository.CustomizedTurmaRepository;

@Repository
public class TurmaRepositoryImpl implements CustomizedTurmaRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<String> getHorariosPorTurmaESala(Turma turma, Sala sala, String turno, List<Long> listaIdsHorarios,
            List<String> listaStringsDias) {
        
        List<String> retorno = new ArrayList<>();
        Long[] arrayIdsHorarios = (Long[]) listaIdsHorarios.toArray(new Long[listaIdsHorarios.size()]);
          
        String[] arrayDias = (String[]) listaStringsDias.toArray(new String[listaStringsDias.size()]);
        
        String stringIdsHorarios = "";
        for (int i = 0; i < arrayIdsHorarios.length; i++) {
            if (i == arrayIdsHorarios.length - 1) {
                stringIdsHorarios += arrayIdsHorarios[i];
            } else {
                stringIdsHorarios += arrayIdsHorarios[i] + ",";
            }
        }

        String stringDias = "";
        for (int i = 0; i < arrayDias.length; i++) {
            if (i == arrayDias.length - 1) {
            stringDias += arrayDias[i];
            } else {
                stringDias += arrayDias[i] + ",";
            }
        }

        String sql =
         "select DAYOFWEEK(ds.dataReserva), h.turno, h.horario FROM Horario h, DisponibilidadeSala ds WHERE h.id = ds.horario.horario "
            +
            "AND ds.turma.id = :idTurma AND h.turno = :turno AND ds.sala.id = :idSala AND ds.periodo.id = :idPeriodo "
            + " AND ds.horario.id IN (" + stringIdsHorarios + ")" +
            " AND DAYOFWEEK(ds.dataReserva) IN (" + stringDias + ")"
            +
            "GROUP BY DAYOFWEEK(ds.dataReserva), ds.horario.id ORDER BY DAYOFWEEK(ds.dataReserva), ds.horario.id";
        
        Query query = manager.createQuery(sql);
        query.setParameter("idTurma", turma.getId());
        query.setParameter("turno", turno);
        query.setParameter("idSala", sala.getId());
        query.setParameter("idPeriodo", turma.getPeriodo().getId());
        
        List<Object[]> objetos = query.getResultList();
        for (Object[] objects : objetos) {
            String dia = String.valueOf(objects[0]);
            String t = String.valueOf(objects[1]);
            String horario = String.valueOf(objects[2]);
            retorno.add(dia + t + horario);
        }

        return retorno;
    }
    
}
