package br.ufrn.ct.cronos.api.assembler;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectInputDisassembler;
import br.ufrn.ct.cronos.api.model.input.TurmaInput;
import br.ufrn.ct.cronos.domain.model.Turma;
import org.springframework.stereotype.Component;

@Component
public class TurmaInputDisassembler extends ObjectInputDisassembler<TurmaInput, Turma>  {

}
