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

import br.ufrn.ct.cronos.api.assembler.FeriadoInputDisassembler;
import br.ufrn.ct.cronos.api.assembler.FeriadoModelAssembler;
import br.ufrn.ct.cronos.api.model.FeriadoModel;
import br.ufrn.ct.cronos.api.model.input.FeriadoInput;
import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.exception.PeriodoNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Feriado;
import br.ufrn.ct.cronos.domain.repository.FeriadoRepository;
import br.ufrn.ct.cronos.domain.service.CadastroFeriadoService;

@RestController
@RequestMapping(value = "/feriados")
public class FeriadoController {
	
	@Autowired
	private FeriadoRepository feriadoRepository;
	
	@Autowired
	private CadastroFeriadoService feriadoService;
	
	@Autowired
	private FeriadoInputDisassembler feriadoInputDisassembler;
	
	@Autowired
	private FeriadoModelAssembler feriadoModelAssembler;
	
	@GetMapping
	public Page<FeriadoModel> listar (@PageableDefault(size = 10) Pageable pageable) {
		Page<Feriado> feriadosPage = feriadoRepository.findAll(pageable);
		Page<FeriadoModel> feriadosModelPage = new PageImpl<>(
                feriadoModelAssembler.toCollectionModel(feriadosPage.getContent()),
                pageable,
                feriadosPage.getTotalElements()
            );
		return feriadosModelPage;
	} 
	
	@GetMapping("/{idFeriado}")
	public FeriadoModel buscarPorId(@PathVariable Long idFeriado) {
		Feriado feriado = feriadoService.buscar(idFeriado);
		
		return feriadoModelAssembler.toModel(feriado);
	}
	
	@GetMapping("/Por-Periodo/{periodoId}")
	public Page<FeriadoModel> FeriadosPorPeriodo(@PathVariable Long periodoId,@PageableDefault(size = 10) Pageable pageable){
		Page<Feriado> feriadosPage = feriadoRepository.findByPeriodo(periodoId, pageable);
	
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
	        feriadoAtual = feriadoService.atualizar(feriadoAtual);
			
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
