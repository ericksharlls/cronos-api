package br.ufrn.ct.cronos.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import br.ufrn.ct.cronos.domain.model.Funcionario;

@Repository
public interface FuncionarioRepository {

	Page<Funcionario> findByIdTipo(Long idTipo,Pageable pageable);
	
	Optional<Funcionario> findByNome(String nome);
}
