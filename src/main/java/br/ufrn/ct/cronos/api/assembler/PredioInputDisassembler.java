package br.ufrn.ct.cronos.api.assembler;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectInputDisassembler;
import br.ufrn.ct.cronos.api.model.input.PredioInput;
import br.ufrn.ct.cronos.domain.model.Predio;
import org.springframework.stereotype.Component;

@Component
public class PredioInputDisassembler extends ObjectInputDisassembler<PredioInput, Predio> {
    
}
