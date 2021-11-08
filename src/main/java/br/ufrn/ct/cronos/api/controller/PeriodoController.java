package br.ufrn.ct.cronos.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping(value = "/periodos")
public class PeriodoController {

	@Autowired
	private PeriodoRepository periodoRepository;
	@Autowired
	private CadastroPeriodoService periodoService;

	@GetMapping
	public List<Periodo> listar() {
		return periodoRepository.findAll();
	}

	@GetMapping("/{id_periodo}")
	public ResponseEntity<Periodo> buscarPorId(@PathVariable Long id_periodo) {
		Optional<Periodo> periodo = periodoRepository.findById(id_periodo);

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

	@PutMapping("/{id_periodo}")
	public ResponseEntity<Periodo> atualizar(@PathVariable Long id_periodo, @Valid @RequestBody Periodo periodo) {
		if (!periodoRepository.existsById(id_periodo)) {
			return ResponseEntity.notFound().build();
		}
		// no corpo da requisicao o id nao e passado, por isso o id e setado para forcar
		// a atualizacao
		periodo.setId_periodo(id_periodo);
		periodo = periodoService.cadastrar(periodo);

		return ResponseEntity.ok(periodo);
	}

	@DeleteMapping("/{id_periodo}")
	public ResponseEntity<?> deletar(@PathVariable Long id_periodo) {
		if (!periodoRepository.existsById(id_periodo)) {
			return ResponseEntity.notFound().build();
		}
		
		periodoService.deletar(id_periodo);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
