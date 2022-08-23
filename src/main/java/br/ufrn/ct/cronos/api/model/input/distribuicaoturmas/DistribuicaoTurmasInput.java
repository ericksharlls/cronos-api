package br.ufrn.ct.cronos.api.model.input.distribuicaoturmas;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DistribuicaoTurmasInput {

    @NotNull
    private Long idPeriodo;

    @NotNull
    private Long idPredio;
    
}
