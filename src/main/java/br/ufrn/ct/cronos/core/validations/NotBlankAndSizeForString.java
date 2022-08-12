package br.ufrn.ct.cronos.core.validations;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { })
@NotBlank
@Size
public @interface NotBlankAndSizeForString {

    @OverridesAttribute(constraint = Size.class, name = "message")
	String message() default "{CampoString.tamanhoExcedido}";
	
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };

	@OverridesAttribute(constraint = Size.class, name = "max")
	int max();
    
}
