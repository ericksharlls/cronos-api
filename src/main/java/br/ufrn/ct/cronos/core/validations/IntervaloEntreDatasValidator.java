package br.ufrn.ct.cronos.core.validations;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import org.springframework.beans.BeanUtils;

public class IntervaloEntreDatasValidator implements ConstraintValidator<IntervaloEntreDatas, Object> {

    private String dataInicialField;
	private String dataFinalField;
	
	@Override
	public void initialize(IntervaloEntreDatas constraint) {
		this.dataInicialField = constraint.dataInicialField();
		this.dataFinalField = constraint.dataFinalField();
	}

    @Override
    public boolean isValid(Object objetoParaValidar, ConstraintValidatorContext context) {
        try {
            LocalDate dataInicio = (LocalDate) BeanUtils.getPropertyDescriptor(objetoParaValidar.getClass(), dataInicialField)
                                .getReadMethod().invoke(objetoParaValidar);
            LocalDate dataTermino = (LocalDate) BeanUtils.getPropertyDescriptor(objetoParaValidar.getClass(), dataFinalField)
                            .getReadMethod().invoke(objetoParaValidar);
            
            if (dataInicio != null && dataTermino != null && dataInicio.isBefore(dataTermino)) {
                return true;
            }
        } catch (Exception e) {
            throw new ValidationException(e);
        }
        
        return false;
    }
    
}
