package br.ufrn.ct.cronos.core.validations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE }) // TYPE: pode ser usada em classe, interface e enum
@Retention(RUNTIME)
@Constraint(validatedBy = { IntervaloEntreDatasValidator.class})
public @interface IntervaloEntreDatas {

    String message() default "{IntervaloEntreDatas.invalido}";
	
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };

    String dataInicialField();
    String dataFinalField();

}
