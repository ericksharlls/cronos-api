package br.ufrn.ct.cronos.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Departamento;
import br.ufrn.ct.cronos.domain.repository.CustomizedDepartamentoRepository;

@Repository
public class DepartamentoRepositoryImpl implements CustomizedDepartamentoRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Map<Long, Departamento> getMapAllWithIdSigaaKey() {
        var jpql = "SELECT d FROM Departamento d WHERE d.idSigaa IS NOT NULL AND d.idSigaa != 0";
		List<Departamento> lista = manager.createQuery(jpql, Departamento.class)
				.getResultList();
        Map<Long, Departamento> retorno = new HashMap<>();
        retorno = lista
                    .stream()
                        .collect(Collectors.toMap(Departamento::getIdSigaa, Function.identity()));
		return retorno;
    }
    
}
