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
    // O Spring vai tentar resolver a dependencia "PredioRepository", e vai tentar instanciar 
    // sua implementação "PredioRepositoryImpl", ocasionando o erro (sem o uso da anotação @Lazy)
    @Autowired @Lazy
	private PredioRepository predioRepository;

    @Override
    public List<Predio> find(String nome, String descricao) {
        var jpql = new StringBuilder();

		jpql.append("from Predio where 0 = 0 ");
		
		var parametros = new HashMap<String, Object>();
		
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
    public List<Predio> buscar(String nome, String descricao) {
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
    
}
