package br.ufrn.ct.cronos.domain.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Feriado;

@Repository
public interface FeriadoRepository extends CustomJpaRepository<Feriado, Long>, CustomizedFeriadoRepository {
	
//	@Query("from Feriado where periodo.id like %:periodoId%")
//	Page<Feriado> findByPeriodo(@Param("periodoId") Long periodoId,Pageable pageable);

	Optional<Feriado> findByData(LocalDate data);
}


