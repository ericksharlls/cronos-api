package br.ufrn.ct.cronos.domain.repository;

import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import br.ufrn.ct.cronos.domain.model.Departamento;

public interface CustomizedDepartamentoRepository {
    
    @Cacheable(key = "'getMapWithAllDepartamentos'", value = "DepartamentoRepository.getMapAllWithIdSigaaKey")
    public Map<Long, Departamento> getMapAllWithIdSigaaKey();

}
