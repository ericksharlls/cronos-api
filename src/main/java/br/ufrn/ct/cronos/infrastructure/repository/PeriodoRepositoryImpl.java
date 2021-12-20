package br.ufrn.ct.cronos.infrastructure.repository;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.repository.CustomizedPeriodoRepository;

@Repository
public class PeriodoRepositoryImpl implements CustomizedPeriodoRepository{
    
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Boolean verificarIntervaloDatasJaExiste(LocalDate dataInicio, LocalDate dataTermino) {
		
		String jpql = 
				" SELECT "
				+ " COUNT(p.id) "
				+ " FROM Periodo p "
				+ " WHERE :dataInicio BETWEEN p.dataInicio AND p.dataTermino "
				+ " AND :dataTermino BETWEEN p.dataInicio AND p.dataTermino ";
	    
		TypedQuery<?> query = manager
					.createQuery(jpql, Periodo.class);
		
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataTermino", dataTermino);
		
		Integer contador = (Integer) query.getSingleResult();
		
		if (contador > 0) {
			return true;
		}
				
		return false;
	}
	
}
