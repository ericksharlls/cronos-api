package br.ufrn.ct.cronos.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.ct.cronos.api.assembler.PerfilSalaTurmaInputDisassembler;
import br.ufrn.ct.cronos.api.assembler.PerfilSalaTurmaModelAssembler;
import br.ufrn.ct.cronos.api.model.PerfilSalaTurmaModel;
import br.ufrn.ct.cronos.api.model.input.PerfilSalaTurmaInput;

import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.repository.PerfilSalaTurmaRepository;
import br.ufrn.ct.cronos.domain.service.PerfilSalaTurmaService;

@RestController
@RequestMapping(value = "/perfilSalaTurma", produces = MediaType.APPLICATION_JSON_VALUE)
public class PerfilSalaTurmaController{
	
	@Autowired
	private PerfilSalaTurmaRepository perfilSalaTurmaRepository;
	
	@Autowired
	private PerfilSalaTurmaService cadastroPerfilSalaTurma;

	@Autowired
	private PerfilSalaTurmaModelAssembler perfilSalaTurmaModelAssembler;
	
	@Autowired
	private PerfilSalaTurmaInputDisassembler perfilSalaTurmaInputDisassembler;
	
	@GetMapping
	public Page<PerfilSalaTurmaModel> listar(@PageableDefault(size = 10) Pageable pageable){
		Page<PerfilSalaTurma> perfisPage = perfilSalaTurmaRepository.findAll(pageable);
        Page<PerfilSalaTurmaModel> perfisModelPage = new PageImpl<>(
                perfilSalaTurmaModelAssembler.toCollectionModel(perfisPage.getContent()),
                pageable,
                perfisPage.getTotalElements()
            );
        
		return perfisModelPage;
	}
	
	@GetMapping("/{perfilSalaTurmaId}")
	public PerfilSalaTurmaModel buscar(@PathVariable Long perfilSalaTurmaId) {
        PerfilSalaTurma perfilSalaTurma = cadastroPerfilSalaTurma.buscar(perfilSalaTurmaId);
        
        return perfilSalaTurmaModelAssembler.toModel(perfilSalaTurma);
    }
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PerfilSalaTurmaModel adcionar(@RequestBody PerfilSalaTurmaInput perfilSalaTurmaInput){
		PerfilSalaTurma perfilSalaTurma = perfilSalaTurmaInputDisassembler.toDomainObject(perfilSalaTurmaInput);
        
        perfilSalaTurma = cadastroPerfilSalaTurma.salvar(perfilSalaTurma);

        return perfilSalaTurmaModelAssembler.toModel(perfilSalaTurma);
	}
	
	@PutMapping("/{perfilSalaTurmaId}")
	public PerfilSalaTurmaModel atualizar(@PathVariable Long perfilSalaTurmaId, @RequestBody @Valid PerfilSalaTurmaInput perfilSalaTurmaInput) {
        PerfilSalaTurma perfilSalaTurmaAtual = cadastroPerfilSalaTurma.buscar(perfilSalaTurmaId);
		
        perfilSalaTurmaInputDisassembler.copyToDomainObject(perfilSalaTurmaInput, perfilSalaTurmaAtual);
        perfilSalaTurmaAtual = cadastroPerfilSalaTurma.salvar(perfilSalaTurmaAtual);
		
        return perfilSalaTurmaModelAssembler.toModel(perfilSalaTurmaAtual);
    }
	
	@DeleteMapping("/{perfilSalaTurmaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long perfilSalaTurmaId) {
        cadastroPerfilSalaTurma.excluir(perfilSalaTurmaId);
    }

	@GetMapping("/por-nome")
	public Page<PerfilSalaTurmaModel> consultarPorNome(String nome,@PageableDefault(size = 10) Pageable pageable){
		Page<PerfilSalaTurma> perfisPage = perfilSalaTurmaRepository.findByNome(nome, pageable);

		Page<PerfilSalaTurmaModel> perfilModelPage = new PageImpl<>(
				perfilSalaTurmaModelAssembler.toCollectionModel(perfisPage.getContent()),
				pageable,
				perfisPage.getTotalElements()
			);

		return perfilModelPage;
	}

}
