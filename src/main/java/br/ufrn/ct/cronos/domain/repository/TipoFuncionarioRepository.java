package br.ufrn.ct.cronos.domain.repository;

import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.TipoFuncionario;

@Repository
public interface TipoFuncionarioRepository extends CustomJpaRepository<TipoFuncionario,Long>{

}
