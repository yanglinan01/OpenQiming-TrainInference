package com.ctdi.cnos.llm.util;

import com.ctdi.cnos.llm.exception.JsonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * json工具类
 * 
 * @author lw
 *
 */
public class JsonUtils {
	// 定义jackson对象
	private static final ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * 将对象转换成json字符串。
	 * 
	 * @param data
	 * @return
	 */
	public static String objectToJson(Object data) {
		try {
			String string = mapper.writeValueAsString(data);
			return string;
		} catch (JsonProcessingException e) {
			throw new JsonException("对象转换成json异常：:" + e.getMessage(), e);
		}
	}

	/**
	 * 将json结果集转化为对象
	 * 
	 * @param jsonData json数据
	 * @param beanType 对象中的object类型
	 * @return
	 */
	public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
		if (StringUtils.isEmpty(jsonData)) {
			return null;
		}
		try {
			T t = mapper.readValue(jsonData, beanType);
			return t;
		} catch (Exception e) {
			throw new JsonException("json结果集转化为对象异常：:" + e.getMessage() + " beanType = " + beanType.getName(), e);
		}
	}

	/**
	 * 将json数据转换成pojo对象list
	 * 
	 * @param jsonData
	 * @param beanType
	 * @return
	 */
	public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanType);
		try {
			List<T> list = mapper.readValue(jsonData, javaType);
			return list;
		} catch (Exception e) {
			throw new JsonException("将json数据转换成pojo对象list异常：:" + e.getMessage(), e);
		}

	}
}
