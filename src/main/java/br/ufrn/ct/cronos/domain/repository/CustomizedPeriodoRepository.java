package br.ufrn.ct.cronos.domain.repository;

import java.time.LocalDate;

public interface CustomizedPeriodoRepository {

	Boolean verificarIntervaloDatasJaExiste(LocalDate dataInicio, LocalDate dataTermino);
	
}
