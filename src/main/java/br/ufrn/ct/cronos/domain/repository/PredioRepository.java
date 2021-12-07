package br.ufrn.ct.cronos.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Predio;

@Repository
public interface PredioRepository extends JpaRepository<Predio, Long>, CustomizedPredioRepository {

    // Usando recurso de Query Methods do Spring Data JPA e fazendo paginação
    // O valor do atributo "nome" não pode chegar nulo aqui
    Page<Predio> findByNomeContaining(String nome, Pageable pageable);

    Page<Predio> findByNomeContainingAndDescricaoContaining(String nome, String descricao, Pageable pageable);

    // Usando anotação @Query do Spring Data JPA e fazendo paginação
    @Query(value = "from Predio p where p.nome like %:nome% AND p.descricao like %:descricao%", 
         countQuery = "select count(p.id) from Predio p where p.nome like %:nome% AND p.descricao like %:descricao%")
    Page<Predio> consultarPorNomeDescricao(@Param("nome") String nome, @Param("descricao") String descricao, Pageable pageable);
    
}
