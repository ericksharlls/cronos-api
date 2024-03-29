package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilSalaTurmaRepository extends CustomJpaRepository<PerfilSalaTurma, Long>{
    
	@Query(value = "FROM PerfilSalaTurma pst WHERE :nome is null OR pst.nome like %:nome%",
			countQuery = "SELECT count(pst.id) FROM PerfilSalaTurma pst WHERE :nome is null OR pst.nome like %:nome%")
	Page<PerfilSalaTurma> findByNome(@Param("nome") String nome, Pageable pageable);

	Optional<PerfilSalaTurma> findByNome(@Param("nome") String nome);
}
