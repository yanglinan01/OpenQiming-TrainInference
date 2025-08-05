package com.ctdi.cnos.llm.config;


import com.ctdi.cnos.llm.jackson.BigDecimalJsonSerializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.MediaType;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FeignObjectFactory implements ObjectFactory<HttpMessageConverters> {
	private HttpMessageConverters httpMessageConverters = null;

	public FeignObjectFactory() {
		httpMessageConverters = new HttpMessageConverters(getMessageConverters());
	}

	@SuppressWarnings("deprecation")
	private Collection<HttpMessageConverter<?>> getMessageConverters() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(
				customObjectMapper());
		List<MediaType> list = new ArrayList<MediaType>();
		list.add(MediaType.APPLICATION_JSON_UTF8);
		jacksonConverter.setSupportedMediaTypes(list);
		jacksonConverter.setDefaultCharset(Charset.forName("UTF-8"));
		messageConverters.add(new ByteArrayHttpMessageConverter());
		messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
		messageConverters.add(new ResourceHttpMessageConverter());
		messageConverters.add(new ResourceRegionHttpMessageConverter());
		messageConverters.add(jacksonConverter);
		try {
			messageConverters.add(new SourceHttpMessageConverter<>());
		} catch (Throwable ex) {
		}
		messageConverters.add(new AllEncompassingFormHttpMessageConverter());
		return messageConverters;
	}

	private ObjectMapper customObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(BigDecimal.class, BigDecimalJsonSerializer.instance);
		objectMapper.registerModule(simpleModule);
		return objectMapper;
	}

	@Override
	public HttpMessageConverters getObject() throws BeansException {
		return httpMessageConverters;
	}

}
