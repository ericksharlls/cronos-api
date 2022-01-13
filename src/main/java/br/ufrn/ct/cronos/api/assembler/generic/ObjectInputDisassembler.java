package br.ufrn.ct.cronos.api.assembler.generic;

import java.lang.reflect.ParameterizedType;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;

public abstract class ObjectInputDisassembler<InputApiModel, DomainModel> {
    
    @Autowired
	@Getter
	protected ModelMapper modelMapper;
	
	private final Class<DomainModel> domainModelClass;

    @SuppressWarnings("unchecked")
	public ObjectInputDisassembler() {
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		this.domainModelClass = (Class<DomainModel>) type.getActualTypeArguments()[1];
	}

    public DomainModel toDomainObject(InputApiModel inputApiModel) {
		return modelMapper.map(inputApiModel, domainModelClass);
	}

    public void copyToDomainObject(InputApiModel inputApiModel, DomainModel domainObject) {
		modelMapper.map(inputApiModel, domainObject);
	}

}
