package br.ufrn.ct.cronos.api.model.input;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReexecucaoImportacaoTurmasInput {

    @NotNull
    private Long idPeriodo;

    @NotNull
    @Size(min = 1)
    private Set<String> siglasNivelEnsino;
    
}
