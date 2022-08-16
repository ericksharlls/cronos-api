package br.ufrn.ct.cronos.domain.service;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.FeriadoNaoEncontradoException;
import br.ufrn.ct.cronos.domain.exception.FuncionarioNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Funcionario;
import br.ufrn.ct.cronos.domain.model.TipoFuncionario;
import br.ufrn.ct.cronos.domain.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public Funcionario salvar(Funcionario funcionario) {
		funcionarioRepository.detach(funcionario);
		
		Long idTipo = funcionario.getTipoFuncionario().getId();
		TipoFuncionario tipo = tipoFuncionarioService.buscar(idTipo);
		
		funcionario.setTipoFuncionario(tipo);
		
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

}
	