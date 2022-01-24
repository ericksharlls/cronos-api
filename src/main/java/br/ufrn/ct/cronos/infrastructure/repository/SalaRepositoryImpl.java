package br.ufrn.ct.cronos.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Sala;

@Repository
public class SalaRepositoryImpl {

    @PersistenceContext
    private EntityManager manager;

    public List<Sala> consultarPorPredio(Long predioId) {
        var jpql = "from Sala where predio.id = :predioId";
		
		return manager.createQuery(jpql, Sala.class)
				.setParameter("predioId", predioId)
				.getResultList();
    }
    
}
