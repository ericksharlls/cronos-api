package br.ufrn.ct.cronos.infrastructure.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import br.ufrn.ct.cronos.domain.model.Predio;

// Fábrica de Specifications de Predio
public class PredioSpecs {

    public static Specification<Predio> porNome(String nome) {
		return (root, query, builder) -> 
            builder.like(root.get("nome"), "%" + nome + "%");
	}
	
	public static Specification<Predio> porDescricao(String descricao) {
		return (root, query, builder) ->
            builder.like(root.get("descricao"), "%" + descricao + "%");
	}
    
}
