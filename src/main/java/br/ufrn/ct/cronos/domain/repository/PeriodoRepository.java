package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.model.Periodo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeriodoRepository extends CustomJpaRepository<Periodo, Long>, CustomizedPeriodoRepository{
	
	@Query(value = "FROM Periodo p WHERE :nome is null OR p.nome like %:nome%",
		       countQuery = "select count(p.id) FROM Periodo p where :nome is null OR p.nome like %:nome%")
	Page<Periodo> findByNome(@Param("nome") String nome, Pageable pgb);
	
	Optional<Periodo> findByNome(String nome);
}
