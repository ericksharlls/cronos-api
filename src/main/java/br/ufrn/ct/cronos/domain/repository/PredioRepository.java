package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.model.Predio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PredioRepository 
        extends CustomJpaRepository<Predio, Long>, CustomizedPredioRepository {
    
    Optional<Predio> findByNome(String nome);

    // Usando recurso de Query Methods do Spring Data JPA e fazendo paginação
    // O valor do atributo "nome" não pode chegar nulo aqui, senão dará erro
    Page<Predio> findByNomeContaining(String nome, Pageable pageable);

    @Query(value = "FROM Predio p WHERE :nome is null OR p.nome like %:nome%",
        countQuery = "select count(p.id) FROM Predio p where :nome is null OR p.nome like %:nome%")
    Page<Predio> findByNome(@Param("nome") String nome, Pageable pageable);

    // Usando anotação @Query do Spring Data JPA e fazendo paginação
    // O valor do atributo "nome" não pode chegar nulo aqui, senão dará erro
    @Query(value = "from Predio p where p.nome like %:nome%", 
         countQuery = "select count(p.id) from Predio p where p.nome like %:nome%")
    Page<Predio> consultarPorNome(@Param("nome") String nome, Pageable pageable);

    // Os 2 métodos acima possuem o mesmo efeito

    @Query("from Predio where nome like %:nome% AND descricao like %:descricao%")
    List<Predio> consultar(String nome, String descricao);

    

}
