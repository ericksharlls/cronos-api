package br.ufrn.ct.cronos.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.ufrn.ct.cronos.domain.model.Sala;

public interface CustomizedSalaRepository {
    
    public Page<Sala> findByNomeAndPredioId(String nome, Long predioId, Pageable pageable);

}
