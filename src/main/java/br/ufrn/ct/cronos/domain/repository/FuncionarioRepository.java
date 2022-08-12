package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.model.Funcionario;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends CustomJpaRepository<Funcionario,Long> , CustomizedFuncionarioRepository {

    Funcionario findByIdSigaaFuncionario(Long idSigaaFuncionario);
    
}

