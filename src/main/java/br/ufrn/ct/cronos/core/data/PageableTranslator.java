package br.ufrn.ct.cronos.core.data;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BeanPropertyBindingResult;

import org.springframework.validation.FieldError;

import br.ufrn.ct.cronos.core.validations.ValidacaoException;

public class PageableTranslator {

    private static final String PROPRIEDADE_INVALIDA_ORDENACAO 
            = "A propriedade '%s' não existe ou é inválida para ordenação.";
    
    public static Pageable translate(Pageable pageable, Map<String, String> fieldsMapping) {
        verificarCamposDeOrdenacaoInvalidos(pageable, fieldsMapping);
        
		var orders = pageable.getSort().stream()
			.filter(order -> fieldsMapping.containsKey(order.getProperty()))
			.map(order -> new Sort.Order(order.getDirection(), 
					fieldsMapping.get(order.getProperty())))
			.collect(Collectors.toList());
							
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(orders));
	}

    private static void verificarCamposDeOrdenacaoInvalidos(Pageable pageable, Map<String, String> fieldsMapping) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(pageable, "pageable");
        pageable.getSort().stream()
                .forEach(order -> { 
                    if(!fieldsMapping.containsKey(order.getProperty())){
                        FieldError fieldError = new FieldError(order.getProperty(), order.getProperty(), String.format(PROPRIEDADE_INVALIDA_ORDENACAO, order.getProperty()));
                        bindingResult.addError(fieldError);
                    }
                });
        if(bindingResult.hasErrors()){
            throw new ValidacaoException(bindingResult);
        }
    }

}
