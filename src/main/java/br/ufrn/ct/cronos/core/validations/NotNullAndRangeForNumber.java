package br.ufrn.ct.cronos.core.validations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { })
@NotNull
@Range
public @interface NotNullAndRangeForNumber {

	@OverridesAttribute(constraint = Range.class, name = "message")
	String message() default "{CampoInteiro.foraDoIntervaloValido}";
	
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
	
	@OverridesAttribute(constraint = Range.class, name = "min")
	long min();

	@OverridesAttribute(constraint = Range.class, name = "max")
	long max();
    
}
