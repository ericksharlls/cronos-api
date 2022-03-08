package br.ufrn.ct.cronos.api.assembler;

import org.springframework.stereotype.Component;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectInputDisassembler;
import br.ufrn.ct.cronos.api.model.FeriadoModel;
import br.ufrn.ct.cronos.domain.model.Feriado;

@Component
public class FeriadoModelDisassembler extends  ObjectInputDisassembler<FeriadoModel, Feriado>{

}
