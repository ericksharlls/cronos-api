package br.ufrn.ct.cronos.api.controller;

import br.ufrn.ct.cronos.api.assembler.PeriodoInputDisassembler;
import br.ufrn.ct.cronos.api.assembler.PeriodoModelAssembler;
import br.ufrn.ct.cronos.api.model.PeriodoModel;
import br.ufrn.ct.cronos.api.model.input.PeriodoInput;
import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.repository.PeriodoRepository;
import br.ufrn.ct.cronos.domain.service.CadastroPeriodoService;
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
@RequestMapping(value = "/periodos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PeriodoController {

	@Autowired
	private PeriodoRepository periodoRepository;
	
	@Autowired
	private CadastroPeriodoService periodoService;

	@Autowired
	private PeriodoModelAssembler periodoModelAssembler;
	
	@Autowired
	private PeriodoInputDisassembler periodoInputDisassembler;
	
	@GetMapping
	public Page<PeriodoModel> listar(@PageableDefault(size = 10) Pageable pageable) {
		Page<Periodo> periodosPage = periodoRepository.findAll(pageable);
		Page<PeriodoModel> periodosModelPage = new PageImpl<>(
                periodoModelAssembler.toCollectionModel(periodosPage.getContent()),
                pageable,
                periodosPage.getTotalElements()
            );

		return periodosModelPage;
	}

	@GetMapping("/{idPeriodo}")
	public PeriodoModel buscarPorId(@PathVariable Long idPeriodo) {
		Periodo periodo = periodoService.buscar(idPeriodo);
		
		return periodoModelAssembler.toModel(periodo);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PeriodoModel cadastrar(@Valid @RequestBody PeriodoInput periodoInput) {
		Periodo periodo = periodoInputDisassembler.toDomainObject(periodoInput);
		periodo = periodoService.salvar(periodo);
		
		return periodoModelAssembler.toModel(periodo);
	}

	@PutMapping("/{periodoId}")
    public PeriodoModel atualizar(@PathVariable Long periodoId, @RequestBody @Valid PeriodoInput periodoInput) {
        Periodo periodoAtual = periodoService.buscar(periodoId);
		
        periodoInputDisassembler.copyToDomainObject(periodoInput, periodoAtual);
        periodoAtual = periodoService.salvar(periodoAtual);
		
        return periodoModelAssembler.toModel(periodoAtual);
    }

	@DeleteMapping("/{periodoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long periodoId) {
        periodoService.excluir(periodoId);
    }

	@GetMapping("/por-nome")
	public Page<PeriodoModel> periodosPorNome(String nome, @PageableDefault(size = 10) Pageable pageable) {
		Page<Periodo> periodosPage = periodoRepository.findByNome(nome, pageable);

        Page<PeriodoModel> periodosModelPage = new PageImpl<>(
                periodoModelAssembler.toCollectionModel(periodosPage.getContent()),
                pageable,
                periodosPage.getTotalElements()
            );
		
        return periodosModelPage;
	}

}
