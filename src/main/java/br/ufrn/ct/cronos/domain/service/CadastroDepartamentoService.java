package br.ufrn.ct.cronos.domain.service;

import br.ufrn.ct.cronos.domain.exception.DepartamentoNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Departamento;
import br.ufrn.ct.cronos.domain.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CadastroDepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public Set<Long> getAllIdsSigaa(){
        return departamentoRepository.getMapAllWithIdSigaaKey().keySet();
    }

    public Departamento getByIdSigaa(Long idSigaa){
        return departamentoRepository.getMapAllWithIdSigaaKey().get(idSigaa);
    }

    public Departamento buscar(Long departamentoId) {
		return departamentoRepository.findById(departamentoId)
			.orElseThrow(() -> new DepartamentoNaoEncontradoException(departamentoId));
	}
    
}
