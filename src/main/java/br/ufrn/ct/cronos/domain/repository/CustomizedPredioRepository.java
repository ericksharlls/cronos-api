package br.ufrn.ct.cronos.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.ufrn.ct.cronos.domain.model.Predio;

// Interface criada para representar queries customizadas do Repositório de Prédio
// Uma nomenclatura bem utilizada, o nome da classe poderia ser CustomizedPredioRepository
public interface CustomizedPredioRepository {

    // Sua implementação é com JPQL
    List<Predio> findComJPQL(String nome, String descricao);

    // Sua implementação é com Criteria API
    List<Predio> findComCriteria(String nome, String descricao);

    // Paginação sendo implementada com Criteria API
    public Page<Predio> findPaginadoComCriteria(String nome, String descricao, Pageable pageable);
    
}
