package br.ufrn.ct.cronos.api.controller;

import br.ufrn.ct.cronos.api.assembler.FeriadoInputDisassembler;
import br.ufrn.ct.cronos.api.assembler.FeriadoModelAssembler;
import br.ufrn.ct.cronos.api.model.FeriadoModel;
import br.ufrn.ct.cronos.api.model.input.FeriadoInput;
import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.exception.PeriodoNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Feriado;
import br.ufrn.ct.cronos.domain.service.CadastroFeriadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/feriados")
public class FeriadoController {
	
	@Autowired
	private CadastroFeriadoService feriadoService;
	
	@Autowired
	private FeriadoInputDisassembler feriadoInputDisassembler;
	
	@Autowired
	private FeriadoModelAssembler feriadoModelAssembler;
	
	@GetMapping("/{idFeriado}")
	public FeriadoModel buscarPorId(@PathVariable Long idFeriado) {
		Feriado feriado = feriadoService.buscar(idFeriado);
		
		return feriadoModelAssembler.toModel(feriado);
	}
	
	@GetMapping
	public Page<FeriadoModel> feriadosPorPeriodo(Long periodoId, @PageableDefault(size = 10) Pageable pageable){
		Page<Feriado> feriadosPage = feriadoService.buscarPorPeriodo(periodoId, pageable);
	
		Page<FeriadoModel> feriadoModelPage = new PageImpl<>(
				feriadoModelAssembler.toCollectionModel(feriadosPage.getContent()),
				pageable,
				feriadosPage.getTotalElements()
				);
		
		return feriadoModelPage;
	}
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FeriadoModel cadastrar(@RequestBody @Valid FeriadoInput feriadoInput) {
		try {
			Feriado feriado = feriadoInputDisassembler.toDomainObject(feriadoInput);
			feriado = feriadoService.salvar(feriado);
			
			return feriadoModelAssembler.toModel(feriado);
			
		} catch (PeriodoNaoEncontradoException e ) {
			throw new NegocioException(e.getMessage(), e);
		}
		
	}

	@PutMapping("/{idFeriado}")
    public FeriadoModel atualizar(@PathVariable Long idFeriado, @RequestBody @Valid FeriadoInput feriadoInput) {
        Feriado feriadoAtual = feriadoService.buscar(idFeriado);
		
        feriadoInputDisassembler.copyToDomainObject(feriadoInput, feriadoAtual);
        try {
	        feriadoAtual = feriadoService.salvar(feriadoAtual);
			
	        return feriadoModelAssembler.toModel(feriadoAtual);
        } catch (PeriodoNaoEncontradoException e) {
        	throw new NegocioException(e.getMessage(), e);
        } 
    }
	
	@DeleteMapping("/{idFeriado}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long idFeriado) {
        feriadoService.excluir(idFeriado);
    }

}
