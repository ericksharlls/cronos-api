package br.ufrn.ct.cronos.domain.repository;

import java.time.LocalDate;
import java.util.List;

import br.ufrn.ct.cronos.domain.model.Periodo;

//Interface que contem as assinaturas de metodos customizados.

public interface CustomizedPeriodoRepository {
	Boolean verificarIntervaloDatasJaExiste(LocalDate dataInicio, LocalDate dataTermino);
	
	List<Periodo> findByIntervalo(LocalDate dataInicio, LocalDate dataTermino);
	
}
