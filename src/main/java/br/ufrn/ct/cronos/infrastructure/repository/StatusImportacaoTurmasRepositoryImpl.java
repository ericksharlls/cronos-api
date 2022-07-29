package br.ufrn.ct.cronos.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.StatusImportacaoTurmas;
import br.ufrn.ct.cronos.domain.repository.CustomizedStatusImportacaoTurmasRepository;

@Repository
public class StatusImportacaoTurmasRepositoryImpl implements CustomizedStatusImportacaoTurmasRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Map<String, StatusImportacaoTurmas> getMapAllWithIdentificadorKey() {
        var jpql = "SELECT s FROM StatusImportacaoTurmas s";
		List<StatusImportacaoTurmas> lista = manager.createQuery(jpql, StatusImportacaoTurmas.class)
				.getResultList();
        Map<String, StatusImportacaoTurmas> retorno = new HashMap<>();
        retorno = lista
                    .stream()
                        .collect(Collectors.toMap(StatusImportacaoTurmas::getIdentificador, Function.identity()));
		return retorno;
    }
    
}
