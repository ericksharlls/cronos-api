package br.ufrn.ct.cronos.domain.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.ct.cronos.domain.exception.TipoFuncionarioNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.TipoFuncionario;
import br.ufrn.ct.cronos.domain.repository.TipoFuncionarioRepository;

@Service
public class CadastroTipoFuncionarioService {
    
    @Autowired 
    TipoFuncionarioRepository tipoFuncionarioRepository;



    @Transactional
    public TipoFuncionario buscar(Long idTipo){
        return tipoFuncionarioRepository.findById(idTipo).orElseThrow(() -> new TipoFuncionarioNaoEncontradoException(idTipo));
    }

}
