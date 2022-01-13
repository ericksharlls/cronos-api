package br.ufrn.ct.cronos.api.assembler;

import org.springframework.stereotype.Component;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectInputDisassembler;
import br.ufrn.ct.cronos.api.model.input.PredioInput;
import br.ufrn.ct.cronos.domain.model.Predio;

@Component
public class PredioInputDisassembler extends ObjectInputDisassembler<PredioInput, Predio> {
    
}
