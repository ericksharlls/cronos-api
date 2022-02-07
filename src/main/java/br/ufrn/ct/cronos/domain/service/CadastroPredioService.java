package br.ufrn.ct.cronos.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.exception.PredioNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Predio;
import br.ufrn.ct.cronos.domain.repository.PredioRepository;

@Service
public class CadastroPredioService {
    
    private static final String MSG_PREDIO_JA_EXISTENTE 
        = "Já existe um Prédio cadastrado com o nome.";

    private static final String MSG_PREDIO_EM_USO 
        = "Prédio de id %d não pode ser removido, pois está em uso.";

    @Autowired
    private PredioRepository predioRepository;

    @Transactional
    public Predio salvar(Predio predio) {
        if(predioRepository.existsPredioByNome(predio.getNome()).equals(true)){
            throw new NegocioException(MSG_PREDIO_JA_EXISTENTE);
        }

        return predioRepository.save(predio);
        //predioRepository.detach(predio);
		
		//Optional<Predio> predioExistente = predioRepository.findByNome(predio.getNome());
		
		//if (predioExistente.isPresent() && !predioExistente.get().equals(predio)) {
		//	throw new NegocioException(
		//			String.format("Já existe um Prédio cadastrado com o nome %s", predio.getNome()));
		//}
    }

    @Transactional
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
