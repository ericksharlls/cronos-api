package br.ufrn.ct.cronos.api.assembler;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectInputDisassembler;
import br.ufrn.ct.cronos.api.model.input.SalaInput;
import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.model.Predio;
import br.ufrn.ct.cronos.domain.model.Sala;
import org.springframework.stereotype.Component;

@Component
public class SalaInputDisassembler extends ObjectInputDisassembler<SalaInput, Sala> {
    
    @Override
    public void copyToDomainObject(SalaInput salaInput, Sala sala) {
        // Uma instancia de uma entidade gerenciada pelo JPA, não pode ter o id alterado
        // Para evitar org.hibernate.HibernateException: identifier of an instance of 
		// br.ufrn.ct.cronos.domain.model.PerfilSalaTurma was altered from 1 to 2
        sala.setPerfilSalaTurma(new PerfilSalaTurma());

        // Para evitar org.hibernate.HibernateException: identifier of an instance of 
		// br.ufrn.ct.cronos.domain.model.Predio was altered from 1 to 2
        sala.setPredio(new Predio());
        
		modelMapper.map(salaInput, sala);
	}

}
