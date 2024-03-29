package br.ufrn.ct.cronos.infrastructure.repository;

import br.ufrn.ct.cronos.domain.model.Funcionario;
import br.ufrn.ct.cronos.domain.repository.CustomizedFuncionarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;

@Repository
public class FuncionarioRepositoryImpl  implements CustomizedFuncionarioRepository{

    @PersistenceContext 
    private EntityManager manager;
    

    @Override
    public Page<Funcionario> findByNomeAndIdTipoFuncionario(String nome, Long idTipoFuncionario, Pageable pageable) {
        
       var jpql = new StringBuilder();

            jpql.append("FROM Funcionario where 0 = 0");

            var parametros = new HashMap<String,Object>();

            if(StringUtils.hasText(nome)){
                jpql.append(" AND nome LIKE :nome");
                parametros.put("nome", "%" +nome + "%");
            }

            if(idTipoFuncionario != null){
                jpql.append(" AND tipoFuncionario.id = :idTipoFuncionario ");
                parametros.put("idTipoFuncionario",idTipoFuncionario);
            }

            TypedQuery<Funcionario> query = manager.createQuery(jpql.toString(),Funcionario.class);

            parametros.forEach((chave, valor) -> query.setParameter(chave, valor));

            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());

            List<Funcionario> funcionarios = query.getResultList();

            Page<Funcionario> funcionariosPage = new PageImpl<>(
                funcionarios,
                pageable,
                getTotalCountFuncionarios(nome, idTipoFuncionario)
            );


       return funcionariosPage;
    }

    private Long getTotalCountFuncionarios(String nome, Long idTipoFuncionario) {
        
        var jpql = new StringBuilder();

            jpql.append("SELECT COUNT(f.id) FROM Funcionario f where 0 = 0");

            var parametros = new HashMap<String,Object>();

            if(StringUtils.hasText(nome)){
                jpql.append(" AND nome LIKE :nome");
                parametros.put("nome", "%" +nome + "%");
            }

            if(idTipoFuncionario != null){
                jpql.append(" AND tipoFuncionario.id = :idTipoFuncionario ");
                parametros.put("idTipoFuncionario", idTipoFuncionario);
            }

            TypedQuery<Long> query = manager.createQuery(jpql.toString(),Long.class);

            parametros.forEach((chave, valor) -> query.setParameter(chave, valor));

           
        return query.getSingleResult();
    }
       
    
}
