package br.ufrn.ct.cronos.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.PeriodoNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.repository.PeriodoRepository;

@Service
public class CadastroPeriodoService {
	
	private static final String MSG_PERIODO_EM_USO 
        = "Período de id %d não pode ser removido, pois está em uso.";
	
	@Autowired
	private PeriodoRepository periodoRepository;
	
	public Periodo salvar(Periodo periodo) {
		return periodoRepository.save(periodo); 
	}
	
	public void excluir(Long periodoId) {
        try {
            periodoRepository.deleteById(periodoId);   
        } catch (EmptyResultDataAccessException e){
            throw new PeriodoNaoEncontradoException(periodoId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                String.format(MSG_PERIODO_EM_USO, periodoId)
            );
        }
    }

	public Periodo buscar(Long periodoId) {
		return periodoRepository.findById(periodoId)
			.orElseThrow(() -> new PeriodoNaoEncontradoException(periodoId));
	}
	
}
