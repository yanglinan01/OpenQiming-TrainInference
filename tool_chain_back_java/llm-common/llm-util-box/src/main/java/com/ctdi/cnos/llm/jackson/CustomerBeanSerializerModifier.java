package com.ctdi.cnos.llm.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * json序列化
 *
 */
public class CustomerBeanSerializerModifier extends BeanSerializerModifier {
	private JsonSerializer<Object> nullStringJsonSerializer = new NullStringJsonSerializer();
	private JsonSerializer<Object> nullArrayJsonSerializer = new NullArrayJsonSerializer();

	@Override
	public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
			List<BeanPropertyWriter> beanProperties) {
		// 循环所有的beanPropertyWriter
		for (int i = 0; i < beanProperties.size(); i++) {
			BeanPropertyWriter writer = beanProperties.get(i);
			// 判断字段的类型，如果是array，list，set则注册nullSerializer
			if (isStringType(writer)) {
				// 给writer注册一个自己的nullSerializer
				writer.assignNullSerializer(this.nullStringJsonSerializer);
			} else if (isArrayType(writer)) {
				writer.assignNullSerializer(this.nullArrayJsonSerializer);
			}
		}
		return beanProperties;
	}

	private boolean isStringType(BeanPropertyWriter writer) {
		JavaType clazz = writer.getType();
		Class c = clazz.getRawClass();
		return c.equals(String.class);
	}

	private boolean isNumberType(BeanPropertyWriter writer) {
		JavaType clazz = writer.getType();
		Class c = clazz.getRawClass();
		return c.equals(Double.class) || c.equals(Integer.class) || c.equals(Long.class) || c.equals(Short.class)
				|| c.equals(Byte.class) || c.equals(BigDecimal.class);
	}

	private boolean isArrayType(BeanPropertyWriter writer) {
		JavaType clazz = writer.getType();
		Class c = clazz.getRawClass();
		return c.isArray() || Collection.class.isAssignableFrom(c);
	}

	private boolean isBooType(BeanPropertyWriter writer) {
		JavaType clazz = writer.getType();
		Class c = clazz.getRawClass();
		return c.equals(boolean.class) || c.equals(Boolean.class);
	}

	private boolean isDateType(BeanPropertyWriter writer) {
		JavaType clazz = writer.getType();
		Class c = clazz.getRawClass();
		return c.equals(Date.class);
	}
}