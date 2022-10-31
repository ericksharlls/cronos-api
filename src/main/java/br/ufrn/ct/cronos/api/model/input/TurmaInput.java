package br.ufrn.ct.cronos.api.model.input;

import br.ufrn.ct.cronos.core.validations.NotBlankAndSizeForString;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.Set;

@Getter
@Setter
public class TurmaInput {

    @NotBlankAndSizeForString(max=50)
    private String horario;

    @NotBlankAndSizeForString(max=100)
    private String nomeDisciplina;

    @NotBlankAndSizeForString(max=20)
    private String codigoDisciplina;

    @NotNull
    private Integer capacidade;

    @NotBlankAndSizeForString(max=5)
    private String numero;

    private Integer alunosMatriculados;

    @NotNull
    private Boolean distribuir;

    @Valid
    @NotNull
    private PerfilSalaTurmaIdInput perfil;

    @Valid
    @NotNull
    private PredioIdInput predio;

    @Valid
    @NotNull
    private PeriodoIdInput periodo;

    @Valid
    @NotNull
    private DepartamentoIdInput departamento;

    @Valid
    private Set<FuncionarioIdInput> docentes;
}
