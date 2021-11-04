package br.ufrn.ct.cronos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Periodo;

@Repository
public interface PeriodoRepository extends JpaRepository<Periodo, Long>{

	List<Periodo> findByNomeContaining(String nome);
	
	List<Periodo> findByPeriodoContaining(String periodo);
	
}
