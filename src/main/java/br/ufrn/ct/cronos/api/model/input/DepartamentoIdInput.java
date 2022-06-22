package br.ufrn.ct.cronos.api.model.input;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartamentoIdInput {

    @NotNull
    Long id;
}
