package br.ufrn.ct.cronos.infrastructure.repository.spec;

import java.util.ArrayList;

import br.ufrn.ct.cronos.domain.filter.TurmaFilter;
import br.ufrn.ct.cronos.domain.model.Turma;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class TurmaSpecs {

    public static Specification<Turma> usandoFiltro(TurmaFilter filtro) {
		return (root, query, builder) -> {
			// Como o Spring Data JPA usa a Specification tanto para o select quanto para o count da paginação, se esse
			// if não for adicionado, dará erro ao fazer o count, pois o count não funciona junto com o fetch
			if (Turma.class.equals(query.getResultType())) {
				root.fetch("periodo");
            	root.fetch("departamento");
				root.fetch("predio");
				root.fetch("perfil");
				root.fetch("docentes", JoinType.LEFT).fetch("tipoFuncionario", JoinType.LEFT);
			}
			
			query.distinct(true);

            var predicates = new ArrayList<Predicate>();
			 
			if (filtro.getIdPeriodo() != null) {
				predicates.add(builder.equal(root.get("periodo"), filtro.getIdPeriodo()));
			}

			if (filtro.getIdPredio() != null) {
				predicates.add(builder.equal(root.get("predio"), filtro.getIdPredio()));
			}

            if (filtro.getIdDepartamento() != null) {
				predicates.add(builder.equal(root.get("departamento"), filtro.getIdDepartamento()));
			}

            if (StringUtils.hasText(filtro.getCodigoComponenteCurricular())) {
				predicates.add(builder.like(root.get("codigoDisciplina"), "%" + filtro.getCodigoComponenteCurricular() + "%"));
			}

            if (StringUtils.hasText(filtro.getNomeDocente())) {
				predicates.add(builder.like(root.join("docentes").get("nome"), "%" + filtro.getNomeDocente() + "%"));
			}

            if (StringUtils.hasText(filtro.getNomeComponenteCurricular())) {
				predicates.add(builder.like(root.get("nomeDisciplina"), "%" + filtro.getNomeComponenteCurricular() + "%"));
			}

            return builder.and(predicates.toArray(new Predicate[0]));
        };
	}
    
}
