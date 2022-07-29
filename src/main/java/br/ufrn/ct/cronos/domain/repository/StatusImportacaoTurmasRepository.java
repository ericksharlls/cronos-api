package br.ufrn.ct.cronos.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.StatusImportacaoTurmas;

@Repository
public interface StatusImportacaoTurmasRepository extends JpaRepository<StatusImportacaoTurmas, Long>, CustomizedStatusImportacaoTurmasRepository {

    
}
