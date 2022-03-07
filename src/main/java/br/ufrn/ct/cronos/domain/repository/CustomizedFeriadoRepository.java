package br.ufrn.ct.cronos.domain.repository;


import java.time.LocalDate;
import java.util.List;

import br.ufrn.ct.cronos.domain.model.Feriado;




public interface CustomizedFeriadoRepository {

	Boolean verificarSeADataDoFeriadoCorrespondeAoPeriodoInformado(Long periodoId,LocalDate data);
	
	Boolean verificarSeJaHaUmFeriadoComMesmaData(LocalDate data);
	
	List<Feriado> consultarPorPeriodoId(Long periodoId);
	
	

}
