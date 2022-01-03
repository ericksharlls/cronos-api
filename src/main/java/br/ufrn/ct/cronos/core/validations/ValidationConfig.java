package br.ufrn.ct.cronos.core.validations;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidationConfig {

    /* LocalValidatorFactoryBean é uma classe q faz a configuração e integração do BeanValidation com o Spring.
    * Então, na hora q o BeanValidaton for resolver as mensagens, ele vai usar apenas o messageSource do Spring, 
    * q é o messageSource q o Spring vai passar como parâmetro nesse método.
    *
    */
	@Bean
	public LocalValidatorFactoryBean validator(MessageSource messageSource) {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);
		return bean;
	}

	
}