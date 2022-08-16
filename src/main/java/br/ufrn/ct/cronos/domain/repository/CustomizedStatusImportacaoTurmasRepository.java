package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.model.StatusImportacaoTurmas;
import org.springframework.cache.annotation.Cacheable;

import java.util.Map;

public interface CustomizedStatusImportacaoTurmasRepository {
   
    @Cacheable(key = "'getMapWithAllStatusImportacaoTurmas'", value = "StatusImportacaoTurmasRepository.getMapAllWithIdentificadorKey")
    public Map<String, StatusImportacaoTurmas> getMapAllWithIdentificadorKey();

}
