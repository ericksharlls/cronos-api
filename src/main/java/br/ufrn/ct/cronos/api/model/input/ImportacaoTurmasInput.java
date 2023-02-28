package br.ufrn.ct.cronos.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Setter
@Getter
public class ImportacaoTurmasInput {
    
    @Schema(example = "100")
    @NotNull
    private Long idPeriodo;

    @Schema(example = "[1000, 1001, 1002]")
    @NotNull
    @Size(min = 1)
    private Set<Long> idsUnidades;

    @Schema(example = "['G', 'L', 'S', 'D', 'E']")
    @NotNull
    @Size(min = 1)
    private Set<String> siglasNivelEnsino;

    @Schema(example = "1")
    @NotNull
    private Long idPredioPadrao;
}
