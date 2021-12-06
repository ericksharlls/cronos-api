package br.ufrn.ct.cronos.infrastructure.repository;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

import br.ufrn.ct.cronos.domain.model.Predio;
import br.ufrn.ct.cronos.domain.repository.PredioRepository;
import br.ufrn.ct.cronos.domain.repository.CustomizedPredioRepository;

// A nomenclatura tem q ter o nome do Repositório e o final "impl" para o Spring entender q se trata 
// da implementação customizada de um Repositório
@Repository
public class PredioRepositoryImpl implements CustomizedPredioRepository {

    @PersistenceContext
	private EntityManager manager;

    // Se não colocar o @Lazy dá erro de referência circular (uma coisa q depende de outra coisa, 
    // e essa outra coisa depende dela mesmo). 
    // Colocando o @Lazy, só vai instanciar essa dependencia quando for preciso.
    @Autowired @Lazy
	private PredioRepository predioRepository;

    @Override
    public List<Predio> findComJPQL(String nome, String descricao) {
        // é melhor concatenar String com StringBuilder
        var jpql = new StringBuilder();

		jpql.append("from Predio where 0 = 0 ");
		
		var parametros = new HashMap<String, Object>();
		
        // StringUtils.hasLength() => verifica se a string passada não tá nula,
        // e se não tá vazia (se o tamanho da string é maior q zero)
		if (StringUtils.hasText(nome)) {
			jpql.append("and nome like :nome ");
			parametros.put("nome", "%" + nome + "%");
		}

        if (StringUtils.hasText(descricao)) {
			jpql.append("and nome like :descricao ");
			parametros.put("descricao", "%" + descricao + "%");
		}
        
        // Como o método createQuery() recebe uma String e não um StringBuilder, 
        // o método toString() do StringBuilder é chamado
        TypedQuery<Predio> query = manager
				.createQuery(jpql.toString(), Predio.class);
		
        // Faz um for no mapa: para cada iteração no mapa, pega o par chave-valor,
        // e seta na query a chave como um parâmetro, e o valor como o valor do parâmetro
		parametros.forEach((chave, valor) -> query.setParameter(chave, valor));

		return query.getResultList();
    }

    @Override
    public List<Predio> findComCriteria(String nome, String descricao) {
        // CriteriaBuilder é uma fábrica pra construir elementos necessários para fazer consulta, 
        // como os critérios e a propria CriteriaQuery
        CriteriaBuilder builder = manager.getCriteriaBuilder();
		
        // CriteriaQuery é um construtor de cláusulas
		CriteriaQuery<Predio> criteria = builder.createQuery(Predio.class);
		// Usando a cláusula 'from'
		Root<Predio> root = criteria.from(Predio.class);;
		// O root acima representa a raiz dom 'from', q é a entidade Predio

        // um Predicado é um filtro
        var predicates = new ArrayList<Predicate>();

        if (StringUtils.hasText(nome)) {
            // O método root.get("nome") pega a representacao da propriedade 'nome' dentro do Root 'Predio'
			predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
		}

        if (StringUtils.hasText(descricao)) {
			predicates.add(builder.like(root.get("descricao"), "%" + descricao + "%"));
		}
        
        // predicates.toArray(new Predicate[0]): essa chamada retorna uma instancia de um array 
        // preenchido com todos os predicates q tá na lista 'predicates'
        criteria.where(predicates.toArray(new Predicate[0]));
        // Nesse where ele faz um AND entre os filtros/predicados

		TypedQuery<Predio> query = manager.createQuery(criteria);
		return query.getResultList();
    }

    @Override
    public Page<Predio> findPaginadoComCriteria(String nome, String descricao, Pageable pageable) {
        var builder = manager.getCriteriaBuilder();
			
		var criteria = builder.createQuery(Predio.class);
		var root = criteria.from(Predio.class);

		var predicates = new ArrayList<Predicate>();
			
		if (StringUtils.hasText(nome)) {
			predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
		}
        if (StringUtils.hasText(descricao)) {
			predicates.add(builder.like(root.get("descricao"), "%" + descricao + "%"));
		}
        // Adicionando ordenação
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));
        
        criteria.where(predicates.toArray(new Predicate[0]));

		var query = manager.createQuery(criteria);
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		List<Predio> predios = query.getResultList();

                //alterei esta linha, para não só passar a lista de prédios, bem como a interface pageable e a chamada a um método que criei, para retornar os totais para a entidade Restaurante.
		Page<Predio> prediosPage = new PageImpl<>(predios, 
                pageable, 
                getTotalCountCriteria(builder, predicates.toArray(new Predicate[0])));

		return prediosPage;
    }

    private Long getTotalCountCriteria(CriteriaBuilder criteriaBuilder, Predicate... predicateArray) {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Predio> root = criteriaQuery.from(Predio.class);

        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(predicateArray);

        return manager.createQuery(criteriaQuery).getSingleResult();
    }
    
}
