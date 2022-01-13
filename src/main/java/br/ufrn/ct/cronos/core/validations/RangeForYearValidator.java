package br.ufrn.ct.cronos.core.validations;

import java.time.Year;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

public class RangeForYearValidator implements ConstraintValidator<RangeForYear, Number> {

    @Override
    public boolean isValid(Number valor, ConstraintValidatorContext context) {
        Integer anoAnterior = Year.now().minusYears(1).getValue();
        Integer anoPosterior = Year.now().plusYears(2).getValue();
        
        if (valor != null && valor.intValue() >= anoAnterior && valor.intValue() <= anoPosterior) {
            return true;
        } else if (valor == null) {
        	context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("O campo Ano não pode ser NULO.")
            .addConstraintViolation();
        	return false;
        } else {
            ((ConstraintValidatorContextImpl) context)
                .addMessageParameter("anoAnterior", anoAnterior)
                .addMessageParameter("anoPosterior", anoPosterior);
            return false;
        }
    }
    
}
