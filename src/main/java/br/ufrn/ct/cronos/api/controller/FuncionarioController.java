package br.ufrn.ct.cronos.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.ct.cronos.api.assembler.FuncionarioInputDisassembler;
import br.ufrn.ct.cronos.api.assembler.FuncionarioModelAssembler;
import br.ufrn.ct.cronos.api.model.FuncionarioModel;
import br.ufrn.ct.cronos.api.model.input.FuncionarioInput;
import br.ufrn.ct.cronos.domain.exception.FuncionarioNaoEncontradoException;
import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.exception.TipoFuncionarioNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Funcionario;
import br.ufrn.ct.cronos.domain.service.CadastroFuncionarioService;

@RestController
@RequestMapping(value = "/funcionarios")
public class FuncionarioController {
	
	@Autowired
	private CadastroFuncionarioService funcionarioService;
	
	@Autowired
	private FuncionarioInputDisassembler funcionarioInputDisassembler;
	
	@Autowired
	private FuncionarioModelAssembler funcionarioModelAssembler;
	
	@GetMapping("/{idFuncionario}")
	public FuncionarioModel buscarPorId(@PathVariable Long idFuncionario) {
		Funcionario funcionario = funcionarioService.buscarPorId(idFuncionario);
		
		return funcionarioModelAssembler.toModel(funcionario);
	
	}
	
	@GetMapping
	public Page<FuncionarioModel> buscarFuncionarioPorTipoENome (Long tipoFuncionarioId, String nome, @PageableDefault(size = 10) Pageable pageable) {
		Page<Funcionario> funcionariosPage = funcionarioService.buscarPorNomeETipo(nome, tipoFuncionarioId, pageable);
		
		Page<FuncionarioModel> funcionarioModelPage = new PageImpl<>(
				funcionarioModelAssembler.toCollectionModel(funcionariosPage.getContent()),
															pageable,
															funcionariosPage.getTotalElements()
															);
		return funcionarioModelPage;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FuncionarioModel cadastrar (@RequestBody @Valid FuncionarioInput funcionarioInput) {
		try {
			Funcionario funcionario = funcionarioInputDisassembler.toDomainObject(funcionarioInput);
			funcionario = funcionarioService.salvar(funcionario);
			
			return funcionarioModelAssembler.toModel(funcionario);
			
		} catch (TipoFuncionarioNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@PutMapping("/{idFuncionario}")
    public FuncionarioModel atualizar(@PathVariable Long idFuncionario, @RequestBody @Valid FuncionarioInput funcionarioInput) {
        Funcionario funcionarioAtual = funcionarioService.buscarPorId(idFuncionario);
		
        funcionarioInputDisassembler.copyToDomainObject(funcionarioInput, funcionarioAtual);
        try {
	        funcionarioAtual = funcionarioService.salvar(funcionarioAtual);
			
	        return funcionarioModelAssembler.toModel(funcionarioAtual);
	        
        } catch (TipoFuncionarioNaoEncontradoException e) {
        	throw new NegocioException(e.getMessage(), e);
        } 
    }
	
	@DeleteMapping("/{idFuncionario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long idFuncionario) {
        funcionarioService.excluir(idFuncionario);
    }
}
