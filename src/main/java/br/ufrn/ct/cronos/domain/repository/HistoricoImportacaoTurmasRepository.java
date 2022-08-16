package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.model.HistoricoImportacaoTurmas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoImportacaoTurmasRepository extends JpaRepository<HistoricoImportacaoTurmas, Long> {

    @Query(value = "SELECT h FROM HistoricoImportacaoTurmas h JOIN FETCH h.importacaoTurmas i " + 
                            "JOIN FETCH h.status s WHERE h.importacaoTurmas.id = :idImportacaoTurmas",
            countQuery = "SELECT count(h.id) FROM HistoricoImportacaoTurmas h JOIN h.importacaoTurmas i " + 
                            "JOIN h.status s WHERE h.importacaoTurmas.id = :idImportacaoTurmas")
    List<HistoricoImportacaoTurmas> findByImportacaoTurmas(@Param("idImportacaoTurmas") Long idImportacaoTurmas);
    
}
