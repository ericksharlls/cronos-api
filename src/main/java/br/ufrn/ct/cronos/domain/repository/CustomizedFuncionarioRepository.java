package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.model.Funcionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomizedFuncionarioRepository {

    Page<Funcionario> findByNomeAndIdTipoFuncionario(String nome,Long idTipoFuncionario, Pageable pageable);
    
}
