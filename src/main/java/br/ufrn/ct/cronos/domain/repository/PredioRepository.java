package br.ufrn.ct.cronos.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Predio;

@Repository
public interface PredioRepository extends JpaRepository<Predio, Long> {

    // Usando recurso de Query Methods do Spring Data JPA e fazendo paginação
    // O valor do atributo "nome" não pode chegar nulo aqui, senão dará erro
    Page<Predio> findByNomeContaining(String nome, Pageable pageable);

    // Usando anotação @Query do Spring Data JPA e fazendo paginação
    // O valor do atributo "nome" não pode chegar nulo aqui, senão dará erro
    @Query(value = "from Predio p where p.nome like %:nome%", 
         countQuery = "select count(p.id) from Predio p where p.nome like %:nome%")
    Page<Predio> consultarPorNome(@Param("nome") String nome, Pageable pageable);
    
}
