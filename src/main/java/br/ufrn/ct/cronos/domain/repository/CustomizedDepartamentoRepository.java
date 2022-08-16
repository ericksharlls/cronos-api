package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.model.Departamento;
import org.springframework.cache.annotation.Cacheable;

import java.util.Map;

public interface CustomizedDepartamentoRepository {
    
    @Cacheable(key = "'getMapWithAllDepartamentos'", value = "DepartamentoRepository.getMapAllWithIdSigaaKey")
    public Map<Long, Departamento> getMapAllWithIdSigaaKey();

}
