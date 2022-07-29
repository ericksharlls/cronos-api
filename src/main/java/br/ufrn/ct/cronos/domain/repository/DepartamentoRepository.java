package br.ufrn.ct.cronos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Departamento;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long>, CustomizedDepartamentoRepository {

    @Query("SELECT d.idSigaa FROM Departamento d")
    public List<Long> getAllIdsSigaa();

}