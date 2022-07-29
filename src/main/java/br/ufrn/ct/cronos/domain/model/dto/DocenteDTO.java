package br.ufrn.ct.cronos.domain.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DocenteDTO {

    private String nome;
	private Long idTurma, idServidor, chDedicada;
    
}
