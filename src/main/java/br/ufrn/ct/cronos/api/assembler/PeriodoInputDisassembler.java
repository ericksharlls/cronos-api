package br.ufrn.ct.cronos.api.assembler;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectInputDisassembler;
import br.ufrn.ct.cronos.api.model.input.PeriodoInput;
import br.ufrn.ct.cronos.domain.model.Periodo;
import org.springframework.stereotype.Component;

@Component
public class PeriodoInputDisassembler extends ObjectInputDisassembler<PeriodoInput, Periodo> {
    
}
