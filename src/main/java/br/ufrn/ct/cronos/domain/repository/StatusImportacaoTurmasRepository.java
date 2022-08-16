package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.model.StatusImportacaoTurmas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusImportacaoTurmasRepository extends JpaRepository<StatusImportacaoTurmas, Long>, CustomizedStatusImportacaoTurmasRepository {

    
}
