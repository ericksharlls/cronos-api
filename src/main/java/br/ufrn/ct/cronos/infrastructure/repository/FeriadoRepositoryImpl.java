package br.ufrn.ct.cronos.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;


import br.ufrn.ct.cronos.domain.model.Feriado;
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
		
		if(cont > 0) {
			return true;
		}
		
		return false;
	}

	
	@Override
	public List<Feriado> consultarPorPeriodoId(Long periodoId) {
		
		String jpql = "from Feriado where periodo.id = :periodoId ";
		
		
		TypedQuery<Feriado> query = manager.createQuery(jpql,Feriado.class);
		
		query.setParameter("periodoId", periodoId);
		
		return query.getResultList();
	}


	@Override
	public Boolean verificarSeJaHaUmFeriadoComMesmaData(LocalDate data) {
		String jpql = "SELECT COUNT(f.id) FROM Feriado F WHERE "
				+ ";data = f.data";
		
		TypedQuery<Long> query = manager.createQuery(jpql, Long.class);
		
		query.setParameter("data", data);
		
		Long cont = query.getSingleResult();
		
		if(cont > 0) {
			return true;
		}
		
		return false;
	}


	
}