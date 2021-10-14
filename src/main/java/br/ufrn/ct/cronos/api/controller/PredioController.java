package br.ufrn.ct.cronos.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
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

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.EntidadeNaoEncontradaException;
import br.ufrn.ct.cronos.domain.model.Predio;
import br.ufrn.ct.cronos.domain.repository.PredioRepository;
import br.ufrn.ct.cronos.domain.service.CadastroPredioService;

@RestController
@RequestMapping(value = "/predios", produces = MediaType.APPLICATION_JSON_VALUE)
public class PredioController {
    
    @Autowired
    private PredioRepository predioRepository;

    @Autowired
    private CadastroPredioService cadastroPredio;

    @GetMapping
    public List<Predio> listar() {
        return predioRepository.findAll();
    }
    
    @GetMapping("/{predioId}")
    public ResponseEntity<Predio> buscar(@PathVariable Long predioId) {
        Optional<Predio> predio = predioRepository.findById(predioId);
        if (predio.isPresent()) {
            return ResponseEntity.ok(predio.get());
        }
        //return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Predio adicionar(@RequestBody Predio predio) {
        return cadastroPredio.salvar(predio);
    }

    @PutMapping("/{predioId}")
    public ResponseEntity<Predio> atualizar(@PathVariable Long predioId, @RequestBody Predio predio) {
        Optional<Predio> predioAtual = predioRepository.findById(predioId);
        
        if(predioAtual.isPresent()) {
            BeanUtils.copyProperties(predio, predioAtual.get(), "id");
            Predio predioSalvo = cadastroPredio.salvar(predioAtual.get());
            return ResponseEntity.ok(predioSalvo);
        }
        
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{predioId}")
    public ResponseEntity<?> remover(@PathVariable Long predioId) {
        try {
            cadastroPredio.excluir(predioId);
            return ResponseEntity.noContent().build();
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        } catch (EntidadeEmUsoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        
    }

}
