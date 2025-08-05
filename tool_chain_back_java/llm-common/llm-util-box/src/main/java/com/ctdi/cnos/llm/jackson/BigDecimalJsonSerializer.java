package com.ctdi.cnos.llm.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * BigDecimal json转换
 */
public class BigDecimalJsonSerializer extends StdSerializer<BigDecimal> {

	private static final long serialVersionUID = -1604448468160356771L;
	public final static BigDecimalJsonSerializer instance = new BigDecimalJsonSerializer();

	public BigDecimalJsonSerializer() {
		super(BigDecimal.class);
	}

	@Override
	public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(value.toPlainString());
	}

}
