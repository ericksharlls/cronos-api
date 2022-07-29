package br.ufrn.ct.cronos.domain.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.ct.cronos.domain.model.Departamento;
import br.ufrn.ct.cronos.domain.repository.DepartamentoRepository;

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
    
}
