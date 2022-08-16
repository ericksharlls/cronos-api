package br.ufrn.ct.cronos.api.assembler;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectInputDisassembler;
import br.ufrn.ct.cronos.api.model.input.FuncionarioInput;
import br.ufrn.ct.cronos.domain.model.Funcionario;
import br.ufrn.ct.cronos.domain.model.TipoFuncionario;
import org.springframework.stereotype.Component;

@Component
public class FuncionarioInputDisassembler extends ObjectInputDisassembler<FuncionarioInput, Funcionario>{
    @Override
    public void copyToDomainObject(FuncionarioInput funcionarioInput, Funcionario funcionario) {
        
        
        funcionario.setTipoFuncionario(new TipoFuncionario());

        modelMapper.map(funcionarioInput, funcionario);
    }
}
