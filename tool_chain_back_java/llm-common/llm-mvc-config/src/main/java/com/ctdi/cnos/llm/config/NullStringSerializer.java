package com.ctdi.cnos.llm.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class NullStringSerializer extends StdSerializer<Object> {
	private static final long serialVersionUID = -2341904193151745296L;
	public final static NullStringSerializer instance = new NullStringSerializer();

	private NullStringSerializer() {
		super(Object.class);
	}

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString("");
	}

}
