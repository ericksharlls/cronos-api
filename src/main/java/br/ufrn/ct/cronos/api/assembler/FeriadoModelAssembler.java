package br.ufrn.ct.cronos.api.assembler;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectModelAssembler;
import br.ufrn.ct.cronos.api.model.FeriadoModel;
import br.ufrn.ct.cronos.domain.model.Feriado;
import org.springframework.stereotype.Component;

// classe que transforma um objeto da entidade Feriado em um DTO
@Component
public class FeriadoModelAssembler extends ObjectModelAssembler<FeriadoModel, Feriado> {

}
