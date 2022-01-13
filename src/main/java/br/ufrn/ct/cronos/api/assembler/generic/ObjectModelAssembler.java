package br.ufrn.ct.cronos.api.assembler.generic;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ObjectModelAssembler<ApiModel, DomainModel> {

    @Autowired
	protected ModelMapper modelMapper;

	private final Class<ApiModel> apiModelClass;

	@SuppressWarnings("unchecked")
	public ObjectModelAssembler() {
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		this.apiModelClass = (Class<ApiModel>) type.getActualTypeArguments()[0];
	}

	public ApiModel toModel(DomainModel domainModel) {
		return modelMapper.map(domainModel, apiModelClass);
	}

	public List<ApiModel> toCollectionModel(List<DomainModel> domainModelList) {
		return domainModelList
                .stream()
				    .map(r -> toModel(r))
				    .collect(Collectors.toList());
	}
    
}
