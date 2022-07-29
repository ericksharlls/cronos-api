package br.ufrn.ct.cronos.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.ImportacaoTurmas;

@Repository
public interface ImportacaoTurmasRepository extends JpaRepository<ImportacaoTurmas, Long> {

    @Query(value = "SELECT i FROM ImportacaoTurmas i JOIN FETCH i.departamento JOIN FETCH i.status ORDER BY i.horarioUltimaOperacao DESC",
            countQuery = "SELECT COUNT(i.id) FROM ImportacaoTurmas i JOIN i.departamento JOIN i.status")
    public Page<ImportacaoTurmas> findAll(Pageable pageable);

    @Query("SELECT i FROM ImportacaoTurmas i JOIN FETCH i.departamento JOIN FETCH i.status WHERE i.id = :idImportacaoTurmas")
    public Optional<ImportacaoTurmas> findById(@Param("idImportacaoTurmas") Long idImportacaoTurmas);
    
}
