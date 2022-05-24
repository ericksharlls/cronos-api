package br.ufrn.ct.cronos.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import br.ufrn.ct.cronos.domain.model.Funcionario;

public interface CustomizedFuncionarioRepository {

    Page<Funcionario> findByNomeAndIdTipoFuncionario(String nome,Long idTipoFuncionario,Pageable pageable);
    
}
