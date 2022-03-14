package br.ufrn.ct.cronos.infrastructure.repository;

import java.time.LocalDate;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;


import br.ufrn.ct.cronos.domain.repository.CustomizedFeriadoRepository;

@Repository
public class FeriadoRepositoryImpl implements CustomizedFeriadoRepository{

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Boolean verificarSeADataDoFeriadoCorrespondeAoPeriodoInformado(Long periodoId, LocalDate data) {
		String jpql = "SELECT COUNT(p.id) FROM Periodo p WHERE "
					+ ":data BETWEEN p.dataInicio AND p.dataTermino "
					+ "AND "
					+ ":periodoId = p.id";
		
		TypedQuery<Long> query = manager.createQuery(jpql, Long.class);
		
		query.setParameter("data", data);
		query.setParameter("periodoId", periodoId);
		
		Long cont = (Long) query.getSingleResult();
		System.out.println(cont);
		if(cont > 0) {
			return true;
		}
		
		return false;
	}

	
}