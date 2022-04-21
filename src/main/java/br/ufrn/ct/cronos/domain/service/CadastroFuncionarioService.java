package br.ufrn.ct.cronos.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.FeriadoNaoEncontradoException;
import br.ufrn.ct.cronos.domain.exception.FuncionarioNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Funcionario;
import br.ufrn.ct.cronos.domain.model.TipoFuncionario;
import br.ufrn.ct.cronos.domain.repository.FuncionarioRepository;

@Service
public class CadastroFuncionarioService {
	
	private static final String MSG_FUNCIONARIO_EM_USO = "O Funcionário de id %d não pode ser removido, pois está em uso.";
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private CadastroTipoFuncionarioService tipoFuncionarioService;
	
	@Transactional
	public Funcionario buscarPorId(Long idFuncionario) {
		
		return funcionarioRepository.findById(idFuncionario).orElseThrow(() -> new FuncionarioNaoEncontradoException(idFuncionario));
	}
	
	@Transactional
	public Page<Funcionario> buscarPorNomeETipo(String nome,Long idTipo,Pageable pageable) {
		return funcionarioRepository.findByNomeEIdTipo(nome, idTipo, pageable);
	}
	
	@Transactional
	public Funcionario salvar(Funcionario funcionario) {
		funcionarioRepository.detach(funcionario);
		
		Long idTipo = funcionario.getTipoFuncionario().getId();
		TipoFuncionario tipo = tipoFuncionarioService.buscar(idTipo);
		
		funcionario.setTipoFuncionario(tipo);

		tratarValoresDosAtributosAntesDaPersistencia(funcionario);
		
		return funcionarioRepository.save(funcionario);
	}
	
	@Transactional
	public void excluir(Long funcionarioId) {
		try {
			funcionarioRepository.deleteById(funcionarioId);
			funcionarioRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new FeriadoNaoEncontradoException(funcionarioId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_FUNCIONARIO_EM_USO, funcionarioId));
		}
	}
	
	private void tratarValoresDosAtributosAntesDaPersistencia(Funcionario funcionario) {
		/*
		 ** Se o CPF não for informado, ele será definido com uma string vazia.
		 ** Se o CPF for informado, ele passará normalmente pela validação da Constraint @CPF.
		*/ 
		if (funcionario.getCpf() == null) {
			funcionario.setCpf("");
		} else {
			/*
			 ** Como a Constraint @CPF permite informar os separadores '.' e '-' e tais separadores não são armazenados 
			 ** no banco de dados, qualquer ocorrência deles é eliminada da String.
		    */
			funcionario.setCpf(funcionario.getCpf().replaceAll("[.-]", ""));
		}
		/*
		 ** Se o E-mail não for informado, ele será definido com uma string vazia.
		 ** Se o E-mail for informado, ele passará normalmente pela validação da Constraint @Email.
		*/ 
		if (funcionario.getEmail() == null) {
			funcionario.setEmail("");
		}
		
		/*
		 ** Se o valor de cada campo abaixo não tiver sido informado, o valor será definido com uma String vazia.
		*/
		if (funcionario.getMatricula() == null) {
			funcionario.setMatricula("");
		}
		 
		if (funcionario.getTelefone() == null) {
			funcionario.setTelefone("");
		}
		
		if (funcionario.getRamal() == null) {
			funcionario.setRamal("");
		}
	}

}
	