package br.ufrn.ct.cronos.infrastructure.repository;

import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.repository.CustomizedPeriodoRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

// classe que implementa a interface dos metodos que devem ser customizados

@Repository
public class PeriodoRepositoryImpl implements CustomizedPeriodoRepository{
    
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Boolean verificarIntervaloDatasJaExiste(LocalDate dataInicio, LocalDate dataTermino) {
		
		String jpql = 
				" SELECT COUNT(p.id) FROM Periodo p WHERE "
				+ ":dataInicio BETWEEN p.dataInicio AND p.dataTermino "
				+ " OR "
				+ ":dataTermino BETWEEN p.dataInicio AND p.dataTermino";
	    
		TypedQuery<Long> query = manager
					.createQuery(jpql, Long.class);
		
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataTermino", dataTermino);
		
		Long contador = (Long) query.getSingleResult();
		
		if (contador > 0) {
			return true;
		}
				
		return false;
	}

	@Override
   public List<Periodo> findByIntervalo(LocalDate dataInicio, LocalDate dataTermino) {
		String jpql =
	  			"SELECT p from Periodo p WHERE "
	  			+ "p.dataInicio BETWEEN :dataInicio AND :dataTermino "
				+ " OR "
         		+ "p.dataTermino BETWEEN :dataInicio AND :dataTermino";
		
		TypedQuery<Periodo> query = manager
				 .createQuery(jpql, Periodo.class);
	 
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataTermino", dataTermino);

		return query.getResultList();
   }
	
}
