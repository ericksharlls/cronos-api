package br.ufrn.ct.cronos.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import java.io.IOException;

// Basta a anotação @JsonComponent e estender a classe JsonSerializer para o Spring entender o objeto 
// como um serializador
@JsonComponent
public class PageJsonSerializer extends JsonSerializer<Page<?>> {

	@Override
	public void serialize(Page<?> page, JsonGenerator gen, 
			SerializerProvider serializers) throws IOException {
		
        // Começa a escrita do objeto serializado
		gen.writeStartObject();
		
		gen.writeObjectField("content", page.getContent());
		gen.writeNumberField("size", page.getSize());
		gen.writeNumberField("totalElements", page.getTotalElements());
		gen.writeNumberField("totalPages", page.getTotalPages());
		gen.writeNumberField("page", page.getNumber());
		
        // Termina a escrita do objeto serializado
		gen.writeEndObject();
	}

}