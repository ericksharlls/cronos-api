package br.ufrn.ct.cronos.api.model.input;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.ufrn.ct.cronos.core.validations.NotBlankAndSizeForString;
import br.ufrn.ct.cronos.core.validations.NotNullAndRangeForNumber;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SalaInput {

    @NotBlankAndSizeForString(max = 50)
    private String nome;

    @NotBlankAndSizeForString(max = 100)
    private String descricao;

    @NotNullAndRangeForNumber(min = 1, max = 500)
    private Integer capacidade;

    @NotBlankAndSizeForString(max = 30)
    private String tipoQuadro;

    @NotNull
    private Boolean utilizarNaDistribuicao;

    @NotNull
    private Boolean utilizarNoAgendamento;

    @NotNull
    private Boolean distribuir;

    @Valid
	@NotNull
	private PredioIdInput predio;

    @Valid
	@NotNull
	private PerilSalaTurmaIdInput perfilSalaTurma;
    
}
