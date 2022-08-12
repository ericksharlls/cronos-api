package br.ufrn.ct.cronos.domain.service;

import br.ufrn.ct.cronos.domain.model.StatusImportacaoTurmas;
import br.ufrn.ct.cronos.domain.repository.StatusImportacaoTurmasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastroStatusImportacaoTurmasService {

    @Autowired
    private StatusImportacaoTurmasRepository statusImportacaoTurmasRepository;

    public StatusImportacaoTurmas getByIdentificador(String identificador){
        return statusImportacaoTurmasRepository.getMapAllWithIdentificadorKey().get(identificador);
    }
    
}
