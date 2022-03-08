package br.ufrn.ct.cronos.api.assembler;

import org.springframework.stereotype.Component;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectInputDisassembler;
import br.ufrn.ct.cronos.api.model.input.FeriadoInput;
import br.ufrn.ct.cronos.domain.model.Feriado;

// Classe que transforma um DTO num objeto da entidade Feriado
@Component
public class FeriadoInputDisassembler extends  ObjectInputDisassembler<FeriadoInput, Feriado>{

}
