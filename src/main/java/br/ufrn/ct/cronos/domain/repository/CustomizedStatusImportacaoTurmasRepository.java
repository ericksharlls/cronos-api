package br.ufrn.ct.cronos.domain.repository;

import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import br.ufrn.ct.cronos.domain.model.StatusImportacaoTurmas;

public interface CustomizedStatusImportacaoTurmasRepository {
   
    @Cacheable(key = "'getMapWithAllStatusImportacaoTurmas'", value = "StatusImportacaoTurmasRepository.getMapAllWithIdentificadorKey")
    public Map<String, StatusImportacaoTurmas> getMapAllWithIdentificadorKey();

}
