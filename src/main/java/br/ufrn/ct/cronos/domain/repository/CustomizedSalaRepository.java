package br.ufrn.ct.cronos.domain.repository;

import java.util.List;

import br.ufrn.ct.cronos.domain.model.Sala;

public interface CustomizedSalaRepository {
    
    public List<Sala> consultarPorPredio(Long predioId);

}
