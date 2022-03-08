package br.ufrn.ct.cronos.api.assembler;

import org.springframework.stereotype.Component;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectModelAssembler;
import br.ufrn.ct.cronos.api.model.FeriadoModel;
import br.ufrn.ct.cronos.domain.model.Feriado;

// classe que transforma um objeto da entidade Feriado em um DTO
@Component
public class FeriadoModelAssembler extends ObjectModelAssembler<FeriadoModel, Feriado> {

}
