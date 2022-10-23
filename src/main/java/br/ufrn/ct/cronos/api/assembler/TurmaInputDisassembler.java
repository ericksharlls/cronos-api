package br.ufrn.ct.cronos.api.assembler;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectInputDisassembler;
import br.ufrn.ct.cronos.api.model.input.TurmaInput;
import br.ufrn.ct.cronos.domain.model.Funcionario;
import br.ufrn.ct.cronos.domain.model.Turma;

import java.util.HashSet;

import org.springframework.stereotype.Component;

@Component
public class TurmaInputDisassembler extends ObjectInputDisassembler<TurmaInput, Turma>  {

    @Override
    public void copyToDomainObject(TurmaInput turmaInput, Turma turma) {
        turma.setDocentes(new HashSet<Funcionario>());
        
		modelMapper.map(turmaInput, turma);
	}

}
