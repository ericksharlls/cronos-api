package br.ufrn.ct.cronos.api.controller;

import br.ufrn.ct.cronos.api.assembler.PredioInputDisassembler;
import br.ufrn.ct.cronos.api.assembler.PredioModelAssembler;
import br.ufrn.ct.cronos.api.model.PredioModel;
import br.ufrn.ct.cronos.api.model.input.PredioInput;
import br.ufrn.ct.cronos.domain.model.Predio;
import br.ufrn.ct.cronos.domain.repository.PredioRepository;
import br.ufrn.ct.cronos.domain.service.CadastroPredioService;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Prédios", description = "Gerencia os Prédios")
@RestController
@RequestMapping(value = "/predios", produces = MediaType.APPLICATION_JSON_VALUE)
public class PredioController {
    
    @Autowired
    private PredioRepository predioRepository;

    @Autowired
    private CadastroPredioService cadastroPredio;

    @Autowired
	private PredioModelAssembler predioModelAssembler;
	
	@Autowired
	private PredioInputDisassembler predioInputDisassembler;

    @GetMapping
    public Page<PredioModel> listar(@PageableDefault(size = 10) Pageable pageable) {
        Page<Predio> prediosPage = predioRepository.findAll(pageable);
        // Criar uma implementação de Page para retorno.. 3 parâmetros são recebidos
        Page<PredioModel> prediosModelPage = new PageImpl<>(
                //1 Parâmetro é a lista q vem do banco.. (O método findAll retorna um objeto Page)
                predioModelAssembler.toCollectionModel(prediosPage.getContent()),
                //prediosPage.getContent(),
                //2 Parâmetro é um objeto pageable com as informações setadas do cliente (exs: size, page, sort)
                pageable,
                //3 Parâmetro: total de elementos da lista
                prediosPage.getTotalElements()
            );
        
		return prediosModelPage;
    }
    
    @GetMapping("/{predioId}")
    public PredioModel buscar(@PathVariable Long predioId) {
        Predio predio = cadastroPredio.buscar(predioId);
        
        return predioModelAssembler.toModel(predio);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PredioModel adicionar(@RequestBody @Valid PredioInput predioInput) {
        Predio predio = predioInputDisassembler.toDomainObject(predioInput);
        
        predio = cadastroPredio.salvar(predio);

        return predioModelAssembler.toModel(predio);
    }

    @PutMapping("/{predioId}")
    public PredioModel atualizar(@PathVariable Long predioId, @RequestBody @Valid PredioInput predioInput) {
        Predio predioAtual = cadastroPredio.buscar(predioId);
		
        predioInputDisassembler.copyToDomainObject(predioInput, predioAtual);
        predioAtual = cadastroPredio.salvar(predioAtual);
		
        return predioModelAssembler.toModel(predioAtual);
    }

    @DeleteMapping("/{predioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long predioId) {
        cadastroPredio.excluir(predioId);
    }

    @GetMapping("/por-nome")
	public Page<PredioModel> prediosPorNome(String nome, @PageableDefault(size = 10) Pageable pageable) {
        Page<Predio> prediosPage = predioRepository.findByNome(nome, pageable);
        
        Page<PredioModel> prediosModelPage = new PageImpl<>(
                predioModelAssembler.toCollectionModel(prediosPage.getContent()),
                pageable,
                prediosPage.getTotalElements()
            );
        
        return prediosModelPage;
	}

    /*@GetMapping("/por-nome-e-descricao")
	public Page<Predio> prediosPorNomeDescricao(String nome, String descricao, @PageableDefault(size = 10) Pageable pageable) {
        // Criar uma implementação de Page para retorno.. 3 parâmetros são recebidos
        Page<Predio> prediosPage = new PageImpl<>(
                predioRepository.findByNomeContainingAndDescricaoContaining(nome, descricao, pageable).getContent(), 
                pageable,
                predioRepository.findByNomeContainingAndDescricaoContaining(nome, descricao, pageable).getTotalElements()
            );
        return prediosPage;
	}*/

    /*
    @GetMapping("/por-nome-e-descricao")
	public Page<Predio> prediosPorNomeDescricao(String nome, String descricao, @PageableDefault(size = 10) Pageable pageable) {
        return predioRepository.consultarPorNomeDescricao(nome, descricao, pageable);
	}
    */
    
    /*
    @GetMapping("/por-nome-e-descricao")
	public List<Predio> prediosPorNomeDescricao(String nome, String descricao) {
        return predioRepository.findComJPQL(nome, descricao);
	}*/

    /*
    @GetMapping("/por-nome-e-descricao")
	public Page<Predio> prediosPorNomeDescricao(String nome, String descricao, @PageableDefault(size = 10) Pageable pageable) {
        return predioRepository.findPaginadoComCriteria(nome, descricao, pageable);
	}*/

}
