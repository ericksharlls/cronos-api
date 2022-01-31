package br.ufrn.ct.cronos.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long>, CustomizedSalaRepository {

    @Query("from Sala s WHERE s.nome like %:nome% AND s.predio.id = :predioId")
	Page<Sala> findByNomeAndPredio(String nome, @Param("predioId") Long predioId, Pageable pageable);

}
