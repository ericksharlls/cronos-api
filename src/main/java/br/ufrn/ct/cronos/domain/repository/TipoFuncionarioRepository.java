package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.model.TipoFuncionario;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoFuncionarioRepository extends CustomJpaRepository<TipoFuncionario,Long>{

}
