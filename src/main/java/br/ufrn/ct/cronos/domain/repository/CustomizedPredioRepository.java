package br.ufrn.ct.cronos.domain.repository;

import java.util.List;

import br.ufrn.ct.cronos.domain.model.Predio;

// Interface criada para representar queries customizadas do Repositório de Prédio
// Uma nomenclatura bem utilizada, o nome da classe poderia ser CustomizedPredioRepository
public interface CustomizedPredioRepository {

    // Sua implementação é com JPQL
    List<Predio> find(String nome, String descricao);

    // Sua implementação é com Criteria API
    List<Predio> buscar(String nome, String descricao);
    
}
