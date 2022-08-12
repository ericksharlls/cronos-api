package br.ufrn.ct.cronos.api.assembler;

import br.ufrn.ct.cronos.api.assembler.generic.ObjectInputDisassembler;
import br.ufrn.ct.cronos.api.model.FeriadoModel;
import br.ufrn.ct.cronos.domain.model.Feriado;
import org.springframework.stereotype.Component;

@Component
public class FeriadoModelDisassembler extends  ObjectInputDisassembler<FeriadoModel, Feriado>{

}
