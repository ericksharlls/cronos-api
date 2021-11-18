package br.ufrn.ct.cronos.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.repository.PeriodoRepository;
import br.ufrn.ct.cronos.domain.service.CadastroPeriodoService;

@RestController
@RequestMapping(value = "/periodos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PeriodoController {

	@Autowired
	private PeriodoRepository periodoRepository;
	@Autowired
	private CadastroPeriodoService periodoService;

	@GetMapping
	public List<Periodo> listar() {
		return periodoRepository.findAll();
	}

	@GetMapping("/{idPeriodo}")
	public ResponseEntity<Periodo> buscarPorId(@PathVariable Long idPeriodo) {
		Optional<Periodo> periodo = periodoRepository.findById(idPeriodo);

		if (!(periodo.isEmpty())) {
			return ResponseEntity.ok(periodo.get());
		}
		
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Periodo cadastrar(@Valid @RequestBody Periodo periodo) {
		return periodoService.cadastrar(periodo);
	}

	@PutMapping("/{idPeriodo}")
	public ResponseEntity<Periodo> atualizar(@PathVariable Long idPeriodo, @Valid @RequestBody Periodo periodo) {
		if (!periodoRepository.existsById(idPeriodo)) {
			return ResponseEntity.notFound().build();
		}
		// no corpo da requisicao o id nao e passado, por isso o id e setado para forcar
		// a atualizacao
		periodo.setIdPeriodo(idPeriodo);
		periodo = periodoService.cadastrar(periodo);
			
		return ResponseEntity.ok(periodo);
	}

	@DeleteMapping("/{idPeriodo}")
	public ResponseEntity<?> deletar(@PathVariable Long idPeriodo) {
		if (!periodoRepository.existsById(idPeriodo)) {
			return ResponseEntity.notFound().build();
		}
		
		periodoService.deletar(idPeriodo);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
