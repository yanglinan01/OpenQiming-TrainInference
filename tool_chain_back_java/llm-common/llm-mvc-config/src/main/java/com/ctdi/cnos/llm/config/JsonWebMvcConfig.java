package com.ctdi.cnos.llm.config;


import com.ctdi.cnos.llm.jackson.BigDecimalJsonSerializer;
import com.ctdi.cnos.llm.jackson.CustomerBeanSerializerModifier;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

public abstract class JsonWebMvcConfig implements WebMvcConfigurer {
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
		Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
		while (iterator.hasNext()) {
			HttpMessageConverter<?> converter = iterator.next();
			if (converter instanceof MappingJackson2HttpMessageConverter) {
				MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converter;
				ObjectMapper objectMapper = jackson2HttpMessageConverter.getObjectMapper();
				JsonSerializer<Object> nullValueSerializer = getNullValueSerializer();
				if (nullValueSerializer == null) {
					nullValueSerializer = NullSerializer.instance;
				}
				// 将Null指改为空字符串
				objectMapper.getSerializerProvider().setNullValueSerializer(nullValueSerializer);
				objectMapper.setSerializerFactory(objectMapper.getSerializerFactory()
						.withSerializerModifier(new CustomerBeanSerializerModifier()));
				// 格式化Date类型的数据
				objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
				/**
				 * 序列换成json时,将所有的long变成string 因为js中得数字类型不能包含所有的java long值
				 */
				SimpleModule simpleModule = new SimpleModule();
				simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
				simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
				simpleModule.addSerializer(BigDecimal.class, BigDecimalJsonSerializer.instance);
				simpleModule.addSerializer(BigInteger.class,ToStringSerializer.instance);
				objectMapper.registerModule(simpleModule);
			}
		}
	}

	public abstract JsonSerializer<Object> getNullValueSerializer();

}
