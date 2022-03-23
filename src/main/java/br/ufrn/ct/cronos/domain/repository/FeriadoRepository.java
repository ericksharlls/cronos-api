package br.ufrn.ct.cronos.domain.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Feriado;

@Repository
public interface FeriadoRepository extends CustomJpaRepository<Feriado, Long>{
	
	@Query(value = "SELECT f FROM Feriado f JOIN FETCH f.periodo" +
					" WHERE :periodoId IS NULL OR f.periodo.id  = :periodoId",
			countQuery = "SELECT count(f.id) FROM Feriado f JOIN f.periodo" +
					" WHERE :periodoId IS NULL OR f.periodo.id  = :periodoId")
	Page<Feriado> findByPeriodo(@Param("periodoId") Long periodoId, Pageable pageable);
	
	Optional<Feriado> findByData(LocalDate data);
}


