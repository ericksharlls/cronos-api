package br.ufrn.ct.cronos.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Setter
@Getter
public class ImportacaoTurmasInput {
    
    @NotNull
    private Long idPeriodo;

    @NotNull
    @Size(min = 1)
    private Set<Long> idsUnidades;

    @NotNull
    @Size(min = 1)
    private Set<String> siglasNivelEnsino;
    
}
