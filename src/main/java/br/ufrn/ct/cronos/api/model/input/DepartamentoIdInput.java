package br.ufrn.ct.cronos.api.model.input;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartamentoIdInput {

    @NotNull
    private Long id;
}
