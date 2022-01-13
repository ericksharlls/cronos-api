package br.ufrn.ct.cronos.core.validations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { RangeForYearValidator.class })
public @interface RangeForYear {
    
	String message() default "{CampoAno.foraDoIntervaloValido}";
	
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
	
}
