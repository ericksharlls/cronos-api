package br.ufrn.ct.cronos.infrastructure.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import br.ufrn.ct.cronos.domain.model.Sala;
import br.ufrn.ct.cronos.domain.repository.CustomizedSalaRepository;

@Repository
public class SalaRepositoryImpl implements CustomizedSalaRepository {

    @PersistenceContext
    private EntityManager manager;
/*
    public List<Sala> consultarPorPredio(Long predioId) {
        var jpql = "from Sala where predio.id = :predioId";
		
		return manager.createQuery(jpql, Sala.class)
				.setParameter("predioId", predioId)
				.getResultList();
    }
*/
    /*
     * Decidi usar Criteria pela facilidade em passar argumentos para orndeção do resultado
    */
    @Override
    public Page<Sala> findByNomeAndPredioId(String nome, Long predioId, Pageable pageable) {
        var builder = manager.getCriteriaBuilder();
			
		var criteria = builder.createQuery(Sala.class);
		var root = criteria.from(Sala.class);

        // Fazendo o fetch de Sala com Predio e PerfilSalaTurma
        root.fetch("predio", JoinType.INNER);
        root.fetch("perfilSalaTurma", JoinType.INNER);

		var predicates = new ArrayList<Predicate>();
			
		if (StringUtils.hasText(nome)) {
			predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
		}
        
        if(predioId != null){
            predicates.add(builder.equal(root.get("predio").get("id"), predioId));
        }

        // Adicionando ordenação
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));
        
        criteria.where(predicates.toArray(new Predicate[0]));

		var query = manager.createQuery(criteria);
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		List<Sala> salas = query.getResultList();

		Page<Sala> prediosPage = new PageImpl<>(salas, 
                pageable, 
                getTotalCountByNomeAndPredioId(builder, predicates.toArray(new Predicate[0])));

		return prediosPage;
    }

    private Long getTotalCountByNomeAndPredioId(CriteriaBuilder criteriaBuilder, Predicate... predicateArray) {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Sala> root = criteriaQuery.from(Sala.class);

        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(predicateArray);

        return manager.createQuery(criteriaQuery).getSingleResult();
    }
    
}
