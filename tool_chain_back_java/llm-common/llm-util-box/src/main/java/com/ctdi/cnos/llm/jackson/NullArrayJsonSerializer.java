package com.ctdi.cnos.llm.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 空数组序列为[]
 *
 */
public class NullArrayJsonSerializer extends JsonSerializer<Object> {
	public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		if (value == null) {
			jgen.writeStartArray();
			jgen.writeEndArray();
		} else {
			jgen.writeObject(value);
		}
	}

}
