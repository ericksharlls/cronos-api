package br.ufrn.ct.cronos.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

// a anotação abaixo instrui o Jackson a incluir na representacao JSON apenas propriedades não-nulas
@JsonInclude(Include.NON_NULL)
@Getter
@Builder
public class Problem {
    
    private Integer status;
    private String type;
    private String title;
    private String detail;

    private String userMessage;
    private OffsetDateTime timestamp;
    private List<Field> validations;

    @Getter
	@Builder
	public static class Field {
		
		private String name;
		private String userMessage;
		
	}

}
