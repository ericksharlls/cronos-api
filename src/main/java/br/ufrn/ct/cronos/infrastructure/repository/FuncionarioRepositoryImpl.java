package br.ufrn.ct.cronos.infrastructure.repository;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.util.StringUtils;
import br.ufrn.ct.cronos.domain.model.Funcionario;
import br.ufrn.ct.cronos.domain.repository.CustomizedFuncionarioRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class FuncionarioRepositoryImpl  implements CustomizedFuncionarioRepository{

    @PersistenceContext 
    private EntityManager manager;

    

    @Override
    public Page<Funcionario> findByNomeEIdTipo(String nome, Long idTipo, Pageable pageable) {
        
       var jpql = new StringBuilder();

            jpql.append("FROM Funcionario where 0 = 0");

            var parametros = new HashMap<String,Object>();

            if(StringUtils.hasText(nome)){
                jpql.append("AND nome LIKE :nome");
                parametros.put("nome", "%" +nome + "%");
            }

            if(idTipo != null){
                jpql.append("AND idTipo = :idTipo ");
                parametros.put("idTipo",idTipo);
            }

            TypedQuery<Funcionario> query = manager.createQuery(jpql.toString(),Funcionario.class);

            parametros.forEach((chave, valor) -> query.setParameter(chave, valor));

            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());

            List<Funcionario> funcionarios = query.getResultList();

            Page<Funcionario> funcionariosPage = new PageImpl<>(
                funcionarios,
                pageable,
                getTotalCountFuncionarios(nome, idTipo)
            );


       return funcionariosPage;
    }



    private Long getTotalCountFuncionarios(String nome, Long idTipo) {
        
        var jpql = new StringBuilder();

            jpql.append("SELECT COUNT(f.id) FROM Funcionario f where 0 = 0");

            var parametros = new HashMap<String,Object>();

            if(StringUtils.hasText(nome)){
                jpql.append("AND nome LIKE :nome");
                parametros.put("nome", "%" +nome + "%");
            }

            if(idTipo != null){
                jpql.append("AND idTipo = :idTipo ");
                parametros.put("idTipo",idTipo);
            }

            TypedQuery<Long> query = manager.createQuery(jpql.toString(),Long.class);

            parametros.forEach((chave, valor) -> query.setParameter(chave, valor));

           
        return query.getSingleResult();
    }
       
    
}
