package br.ufrn.ct.cronos.domain.service;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.FeriadoNaoEncontradoException;
import br.ufrn.ct.cronos.domain.exception.FuncionarioNaoEncontradoException;
import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.model.Funcionario;
import br.ufrn.ct.cronos.domain.model.TipoFuncionario;
import br.ufrn.ct.cronos.domain.repository.FuncionarioRepository;


@Service
public class CadastroFuncionarioService {

	private static final String MSG_CPF_OU_MATRICULA_NECESSARIO = "É necessário informar a matrícula ou o CPF do Funcionário.";	
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

		if(StringUtils.hasText(funcionario.getEmail())) {
			System.out.println("ENtROU NO EMAIL!!!");
			validarEmail(funcionario.getEmail());
		}
		verificarSeExisteCPFouMatricula(funcionario);
		
		if(StringUtils.hasText(funcionario.getCpf())) {
			validarCPF(funcionario.getCpf());
		}
	
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
			throw new EntidadeEmUsoException(String.format(MSG_FUNCIONARIO_EM_USO,funcionarioId));
		}
	}
	
	private void verificarSeExisteCPFouMatricula(Funcionario funcionario) {
		String cpf = funcionario.getCpf();
		String matricula = funcionario.getMatricula();

		if(!StringUtils.hasText(cpf) && !StringUtils.hasText(matricula)) {
			
			throw new NegocioException(MSG_CPF_OU_MATRICULA_NECESSARIO);
		}
	}

	@Email
	private String validarEmail(@Email String email){
		System.out.println("#### ENTROU NA VALIDAÇÂO de EMAIL!!!");
		return email;
	}

	private void validarCPF(@CPF String cpf){
		System.out.println("#### ENTROU NA VALIDAÇÂO de CPF!!!");
	}

}
	