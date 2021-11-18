package br.ufrn.ct.cronos.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.repository.PeriodoRepository;

@Service
public class CadastroPeriodoService {
	@Autowired
	private PeriodoRepository periodoRepository;
	
	public Periodo cadastrar(Periodo periodo) {
		return periodoRepository.save(periodo); 
	}
	
	public void deletar(Long idPeriodo) {
		periodoRepository.deleteById(idPeriodo);
	}
	
}
