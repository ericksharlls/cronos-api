package br.ufrn.ct.cronos.api.model.input;

import br.ufrn.ct.cronos.core.validations.IntervaloEntreDatas;
import br.ufrn.ct.cronos.core.validations.NotBlankAndSizeForString;
import br.ufrn.ct.cronos.core.validations.NotNullAndRangeForNumber;
import br.ufrn.ct.cronos.core.validations.RangeForYear;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@IntervaloEntreDatas(dataInicialField = "dataInicio", dataFinalField = "dataTermino")
@Setter
@Getter
public class PeriodoInput {
    
    @NotBlankAndSizeForString(max = 30)
	private String nome;
	
	@NotBlankAndSizeForString(max = 50)
	private String descricao;
	
	@NotNull
	private LocalDate dataInicio;
	
	@NotNull
	private LocalDate dataTermino;
	
	@NotNull
	private Boolean isPeriodoLetivo;
	
	@RangeForYear
	private Short ano;

	@NotNullAndRangeForNumber(min = 1, max = 10)
	private Short numero;

}
