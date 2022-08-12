package br.ufrn.ct.cronos.core.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { RangeForYearValidator.class })
public @interface RangeForYear {
    
	String message() default "{CampoAno.foraDoIntervaloValido}";
	
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
	
}
