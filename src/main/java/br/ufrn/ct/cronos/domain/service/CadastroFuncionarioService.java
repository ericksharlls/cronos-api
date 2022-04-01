package br.ufrn.ct.cronos.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.ct.cronos.domain.model.Funcionario;
import br.ufrn.ct.cronos.domain.model.TipoFuncionario;
import br.ufrn.ct.cronos.domain.repository.FuncionarioRepository;
import br.ufrn.ct.cronos.domain.repository.TipoFuncionarioRepository;
import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.FeriadoNaoEncontradoException;
import br.ufrn.ct.cronos.domain.exception.NegocioException;


@Service
public class CadastroFuncionarioService {

	private static final String MSG_CPF_OU_MATRICULA_NECESSARIO = "É necessario informar a matricula ou o CPF do funcionario";	
	private static final String MSG_FUNCIONARIO_EM_USO = "O funcionario de id %d não pode ser removido, pois esta em uso";
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private TipoFuncionarioRepository tipoFuncionarioRepository;
	
	@Transactional
	public Page<Funcionario> buscar(String nome,Long idTipo,Pageable pageable) {
		return funcionarioRepository.findByNomeEIdTipo(nome, idTipo, pageable);
	}
	
	
	@Transactional
	public Funcionario salvar(Funcionario funcionario) {
		funcionarioRepository.detach(funcionario);
		
		Long idTipo = funcionario.getTipoFuncionario().getId();
		TipoFuncionario tipo = tipoFuncionarioRepository.getById(idTipo);
		
		funcionario.setTipoFuncionario(tipo);
		
		verificarSeExisteCPFouMatricula(funcionario);
		
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
		
		if(cpf.isEmpty() && matricula.isEmpty()) {
			throw new NegocioException(MSG_CPF_OU_MATRICULA_NECESSARIO);
		}
	}
}
	