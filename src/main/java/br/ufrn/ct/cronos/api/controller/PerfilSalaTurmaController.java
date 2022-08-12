package br.ufrn.ct.cronos.api.controller;

import br.ufrn.ct.cronos.api.assembler.PerfilSalaTurmaInputDisassembler;
import br.ufrn.ct.cronos.api.assembler.PerfilSalaTurmaModelAssembler;
import br.ufrn.ct.cronos.api.model.PerfilSalaTurmaModel;
import br.ufrn.ct.cronos.api.model.input.PerfilSalaTurmaInput;
import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.repository.PerfilSalaTurmaRepository;
import br.ufrn.ct.cronos.domain.service.CadastroPerfilSalaTurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/perfilSalaTurma", produces = MediaType.APPLICATION_JSON_VALUE)
public class PerfilSalaTurmaController{
	
	@Autowired
	private PerfilSalaTurmaRepository perfilSalaTurmaRepository;
	
	@Autowired
	private CadastroPerfilSalaTurmaService cadastroPerfilSalaTurma;

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
	public PerfilSalaTurmaModel adicionar(@RequestBody @Valid PerfilSalaTurmaInput perfilSalaTurmaInput){
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
