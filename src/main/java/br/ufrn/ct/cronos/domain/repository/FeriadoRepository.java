package br.ufrn.ct.cronos.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.ufrn.ct.cronos.domain.model.Feriado;

public interface FeriadoRepository extends CustomJpaRepository<Feriado, Long>, CustomizedFeriadoRepository {
	
	@Query("from Feriado where periodo.id like %:periodoId%")
	List<Feriado> findByPeriodo(@Param("periodoId") Long periodoId);

	Optional<Feriado> findByData(LocalDate data);
}


