package br.ufrn.ct.cronos.api.model.input;

import br.ufrn.ct.cronos.core.validations.NotBlankAndSizeForString;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PredioInput {
    
    @NotBlankAndSizeForString(max = 50)
    private String nome;
    
    @NotBlankAndSizeForString(max = 100)
    private String descricao;

}
