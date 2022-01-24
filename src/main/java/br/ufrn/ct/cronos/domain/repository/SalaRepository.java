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

    @Query("from Sala WHERE nome like %:nome% AND predio.id = :id")
	Page<Sala> findByNomeAndPredio(String nome, @Param("id") Long predioId, Pageable pageable);

}
