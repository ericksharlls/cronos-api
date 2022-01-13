package br.ufrn.ct.cronos.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.PredioNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Predio;
import br.ufrn.ct.cronos.domain.repository.PredioRepository;

@Service
public class CadastroPredioService {
    
    private static final String MSG_PREDIO_EM_USO 
        = "Prédio de id %d não pode ser removido, pois está em uso.";

    @Autowired
    private PredioRepository predioRepository;

    public Predio salvar(Predio predio) {
        return predioRepository.save(predio);
    }

    public void excluir(Long predioId) {
        try {
            predioRepository.deleteById(predioId);   
        } catch (EmptyResultDataAccessException e){
            throw new PredioNaoEncontradoException(predioId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                String.format(MSG_PREDIO_EM_USO, predioId)
            );
        }
    }

    public Predio buscar(Long predioId) {
		return predioRepository.findById(predioId)
			.orElseThrow(() -> new PredioNaoEncontradoException(predioId));
	}

}
