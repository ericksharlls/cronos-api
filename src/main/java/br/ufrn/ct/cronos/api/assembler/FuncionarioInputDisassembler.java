package br.ufrn.ct.cronos.api.assembler;

import org.springframework.stereotype.Component;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectInputDisassembler;
import br.ufrn.ct.cronos.api.model.input.FuncionarioInput;
import br.ufrn.ct.cronos.domain.model.Funcionario;

@Component
public class FuncionarioInputDisassembler extends ObjectInputDisassembler<FuncionarioInput, Funcionario>{

}
