package br.ufrn.ct.cronos.domain.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServidorDTO {

    private String nome;
	private Long idServidor;
    
}
