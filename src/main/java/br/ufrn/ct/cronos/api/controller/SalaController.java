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

import br.ufrn.ct.cronos.api.assembler.SalaInputDisassembler;
import br.ufrn.ct.cronos.api.assembler.SalaModelAssembler;
import br.ufrn.ct.cronos.api.model.SalaModel;
import br.ufrn.ct.cronos.api.model.input.SalaInput;

import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.exception.PerfilSalaTurmaNaoEncontradoException;
import br.ufrn.ct.cronos.domain.exception.PredioNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Sala;
import br.ufrn.ct.cronos.domain.repository.SalaRepository;
import br.ufrn.ct.cronos.domain.service.CadastroSalaService;

@RestController
@RequestMapping(value = "/salas")
public class SalaController {

    @Autowired
	private SalaRepository salaRepository;

	@Autowired
	private CadastroSalaService cadastroSala;

	@Autowired
	private SalaModelAssembler salaModelAssembler;

	@Autowired
	private SalaInputDisassembler salaInputDisassembler;
	
	@GetMapping
	public Page<SalaModel> listar(@PageableDefault(size = 10) Pageable pageable) {
		Page<Sala> salasPage = salaRepository.findAll(pageable);
        // Criar uma implementação de Page para retorno.. 3 parâmetros são recebidos
        Page<SalaModel> prediosModelPage = new PageImpl<>(
                //1 Parâmetro é a lista q vem do banco.. (O método findAll retorna um objeto Page)
                salaModelAssembler.toCollectionModel(salasPage.getContent()),
                //prediosPage.getContent(),
                //2 Parâmetro é um objeto pageable com as informações setadas do cliente (exs: size, page, sort)
                pageable,
                //3 Parâmetro: total de elementos da lista
                salasPage.getTotalElements()
            );
        
		return prediosModelPage;
	}

	@GetMapping("/{salaId}")
    public SalaModel buscar(@PathVariable Long salaId) {
		Sala sala = cadastroSala.buscarOuFalhar(salaId);
		
        return salaModelAssembler.toModel(sala);
    }

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SalaModel adicionar(@RequestBody @Valid SalaInput salaInput) {
		try {
			Sala sala = salaInputDisassembler.toDomainObject(salaInput);
			sala = cadastroSala.salvar(sala);
			return salaModelAssembler.toModel(sala);
		} catch (PredioNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		} catch (PerfilSalaTurmaNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@PutMapping("/{salaId}")
	public SalaModel atualizar(@PathVariable Long salaId, @RequestBody @Valid SalaInput salaInput) {
		Sala salaAtual = cadastroSala.buscarOuFalhar(salaId);
		
		salaInputDisassembler.copyToDomainObject(salaInput, salaAtual);
		
		try {
			salaAtual = cadastroSala.salvar(salaAtual);	
			return salaModelAssembler.toModel(salaAtual);
		} catch (PredioNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		} catch (PerfilSalaTurmaNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@DeleteMapping("/{salaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long salaId) {
        cadastroSala.excluir(salaId);
    }

	@GetMapping("/por-nome")
	public Page<SalaModel> salasPorNome(String nome, Long predioId, @PageableDefault(size = 10) Pageable pageable) {
        Page<Sala> salasPage = salaRepository.findByNomeAndPredio(nome, predioId, pageable);

        Page<SalaModel> salasModelPage = new PageImpl<>(
                salaModelAssembler.toCollectionModel(salasPage.getContent()),
                pageable,
                salasPage.getTotalElements()
            );

		return salasModelPage;
	}
    
}
