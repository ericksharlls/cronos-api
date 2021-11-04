package br.ufrn.ct.cronos.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.repository.PeriodoRepository;

@RestController
@RequestMapping(value = "/periodo")
public class PeriodoController {
	@Autowired
	private PeriodoRepository periodoRepository;
	
	@GetMapping
	public List<Periodo> listar() {
		return periodoRepository.findAll();
	}
	
	@GetMapping("/{id_periodo}")
	public Optional<Periodo> buscarPorId(@PathVariable Long id_periodo) {
		return periodoRepository.findById(id_periodo);
	}
	
	
}
