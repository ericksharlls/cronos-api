package br.ufrn.ct.cronos.api.assembler;

import org.springframework.stereotype.Component;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectModelAssembler;
import br.ufrn.ct.cronos.api.model.FuncionarioModel;
import br.ufrn.ct.cronos.domain.model.Funcionario;

@Component
public class FuncionarioModelAssembler extends ObjectModelAssembler<FuncionarioModel, Funcionario>{

}
