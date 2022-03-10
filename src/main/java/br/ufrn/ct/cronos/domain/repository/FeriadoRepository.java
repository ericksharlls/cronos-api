package br.ufrn.ct.cronos.domain.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.ufrn.ct.cronos.domain.model.Feriado;

public interface FeriadoRepository extends CustomJpaRepository<Feriado, Long>, CustomizedFeriadoRepository {
	
	@Query("from Feriado where periodo.id like %:periodoId%")
	Page<Feriado> findByPeriodo(@Param("periodoId") Long periodoId,Pageable pageable);

	Optional<Feriado> findByData(LocalDate data);
}


