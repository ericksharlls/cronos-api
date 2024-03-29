package br.ufrn.ct.cronos.api.model.input;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

@Setter
@Getter
public class ReexecucaoImportacaoTurmasInput {

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
