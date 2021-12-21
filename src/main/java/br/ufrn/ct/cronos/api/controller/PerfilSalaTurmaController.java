package br.ufrn.ct.cronos.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.EntidadeNaoEncontradaException;
import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.repository.PerfilSalaTurmaRepository;
import br.ufrn.ct.cronos.domain.service.PerfilSalaTurmaService;

@RestController
@RequestMapping(value = "/perfilSalaTurma", produces = MediaType.APPLICATION_JSON_VALUE)
public class PerfilSalaTurmaController{
	
	@Autowired
	private PerfilSalaTurmaRepository perfilSalaTurmaRepository;
	
	@Autowired
	private PerfilSalaTurmaService perfilSalaTurmaService;
	
	@GetMapping
	public List<PerfilSalaTurma> listar(){
		return perfilSalaTurmaRepository.findAll();
	}
	
	@GetMapping("/por-nome")
	public Page<PerfilSalaTurma> listar(String nome,@PageableDefault(size = 10) Pageable pageable){
		
		Page<PerfilSalaTurma> perfilSalaTurmaPage = new PageImpl<>(
				perfilSalaTurmaRepository.findByNome(nome, pageable).getContent(),
				pageable,
				perfilSalaTurmaRepository.findByNome(nome, pageable).getTotalElements()
				);
		return perfilSalaTurmaPage;
	}
	
	@GetMapping("/{perfilSalaTurmaId}")
	public ResponseEntity<PerfilSalaTurma> buscar(@PathVariable Long perfilSalaturmaId){
		Optional<PerfilSalaTurma> perfilSalaTurma = perfilSalaTurmaRepository.findById(perfilSalaturmaId);
		if(perfilSalaTurma.isPresent()) {
			
			return ResponseEntity.ok(perfilSalaTurma.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PerfilSalaTurma adcionar(@RequestBody PerfilSalaTurma perfilSalaTurma){
		return perfilSalaTurmaService.salvar(perfilSalaTurma);
	}
	
	@PutMapping("/{perfilSalaTurmaId}")
	public ResponseEntity<PerfilSalaTurma> atualizar(@PathVariable Long perfilSalaTurmaId,@RequestBody PerfilSalaTurma perfilSalaTurma){
		Optional<PerfilSalaTurma> perfilSalaTurmaAtual = perfilSalaTurmaRepository.findById(perfilSalaTurmaId);
		
		if(perfilSalaTurmaAtual.isPresent()) {
			BeanUtils.copyProperties(perfilSalaTurma, perfilSalaTurmaAtual.get(), "id");
			PerfilSalaTurma perfilSalaTurmaSalvo = perfilSalaTurmaService.salvar(perfilSalaTurmaAtual.get());
			return ResponseEntity.ok(perfilSalaTurmaSalvo);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{perfilSalaTurmaId}")
	public ResponseEntity<?> remover(@PathVariable Long perfilSalaTurmaId){
		try {
			perfilSalaTurmaService.excluir(perfilSalaTurmaId);
			return ResponseEntity.noContent().build();
		} catch (EntidadeNaoEncontradaException e) {
			 return ResponseEntity.notFound().build();
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}   
}
