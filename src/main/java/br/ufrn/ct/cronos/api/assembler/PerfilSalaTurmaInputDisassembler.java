package br.ufrn.ct.cronos.api.assembler;

import org.springframework.stereotype.Component;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectInputDisassembler;
import br.ufrn.ct.cronos.api.model.input.PerfilSalaTurmaInput;
import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;

@Component
public class PerfilSalaTurmaInputDisassembler extends ObjectInputDisassembler<PerfilSalaTurmaInput, PerfilSalaTurma> {
    
}
