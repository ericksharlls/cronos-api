package br.ufrn.ct.cronos.api.assembler;

import org.springframework.stereotype.Component;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectInputDisassembler;
import br.ufrn.ct.cronos.api.model.input.SalaInput;
import br.ufrn.ct.cronos.domain.model.Sala;

@Component
public class SalaInputDisassembler extends ObjectInputDisassembler<SalaInput, Sala> {
    
}
