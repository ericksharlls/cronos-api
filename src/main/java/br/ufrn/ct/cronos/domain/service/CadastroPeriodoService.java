package br.ufrn.ct.cronos.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.exception.PeriodoNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.repository.PeriodoRepository;

@Service
public class CadastroPeriodoService {
	
	private static final String MSG_PERIODO_EM_USO 
        = "Período de id %d não pode ser removido, pois está em uso.";
    
    private static final String MSG_INTERVALO_DATAS_EM_USO 
        = "Já existe um Período contemplando o intervalo de datas informado.";
	
	@Autowired
	private PeriodoRepository periodoRepository;
	
    @Transactional
	public Periodo cadastrar(Periodo periodo) {
        if(periodoRepository.verificarIntervaloDatasJaExiste(periodo.getDataInicio(), periodo.getDataTermino())){
            throw new NegocioException(MSG_INTERVALO_DATAS_EM_USO);
        }
        
		return periodoRepository.save(periodo); 
	}
	
    @Transactional
    public Periodo atualizar(Periodo periodo) {
        List<Periodo> periodos = periodoRepository.findByIntervalo(periodo.getDataInicio(), periodo.getDataTermino());

        if (periodos.size() > 0) {
            periodos.stream()
                .forEach((p) -> {
                    if (!p.getId().equals(periodo.getId())) {
                        throw new NegocioException(MSG_INTERVALO_DATAS_EM_USO);
                     }
                });
        }
        
		return periodoRepository.save(periodo); 
	}

    @Transactional
	public void excluir(Long periodoId) {
        try {
            periodoRepository.deleteById(periodoId);  
            periodoRepository.flush();
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
