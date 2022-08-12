package br.ufrn.ct.cronos.api.assembler;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectInputDisassembler;
import br.ufrn.ct.cronos.api.model.input.FeriadoInput;
import br.ufrn.ct.cronos.domain.model.Feriado;
import org.springframework.stereotype.Component;

// Classe que transforma um DTO num objeto da entidade Feriado
@Component
public class FeriadoInputDisassembler extends ObjectInputDisassembler<FeriadoInput, Feriado>{

}
