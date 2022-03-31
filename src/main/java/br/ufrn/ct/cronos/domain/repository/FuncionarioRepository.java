package br.ufrn.ct.cronos.domain.repository;


import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Funcionario;




@Repository
public interface FuncionarioRepository extends CustomJpaRepository<Funcionario,Long> , CustomizedFuncionarioRepository {

	
}
