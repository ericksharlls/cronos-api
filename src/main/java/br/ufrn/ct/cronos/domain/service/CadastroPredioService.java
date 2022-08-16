package br.ufrn.ct.cronos.domain.service;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.exception.PredioNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Predio;
import br.ufrn.ct.cronos.domain.repository.PredioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CadastroPredioService {

    private static final String MSG_PREDIO_EM_USO 
        = "Prédio de id %d não pode ser removido, pois está em uso.";

    @Autowired
    private PredioRepository predioRepository;

    @Transactional
    public Predio salvar(Predio predio) {
        predioRepository.detach(predio);
		
		Optional<Predio> predioExistente = predioRepository.findByNome(predio.getNome());
		
		if (predioExistente.isPresent() && !predioExistente.get().equals(predio)) {
			throw new NegocioException(
					String.format("Já existe um Prédio cadastrado com o nome \'%s\'", predio.getNome()));
		}
        return predioRepository.save(predio);
    }

    @Transactional
    public void excluir(Long predioId) {
        try {
            predioRepository.deleteById(predioId);
            predioRepository.flush();
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
