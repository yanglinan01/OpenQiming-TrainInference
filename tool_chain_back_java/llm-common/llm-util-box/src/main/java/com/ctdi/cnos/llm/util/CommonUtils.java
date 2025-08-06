package com.ctdi.cnos.llm.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.FileCopyUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CommonUtils {
	public static String MD5_SALT = "Ai@0Ss@RM3.0";

	/**
	 * 获取md5值
	 * 
	 * @param source
	 * @return
	 */
	public static String md5(String source) {
		return CryptographyUtil.md5(source, MD5_SALT);
	}

	public static String md5(String str, String salt) {
		return CryptographyUtil.md5(str, salt);
	}

	public static String getUuId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	@SuppressWarnings("rawtypes")
	public static Map getJsonMap(String info) throws Exception {
		if (StrUtil.isEmpty(info)) {
			return null;
		}
		info = info.replaceAll("\r|\n", "");
		return getObjectFromString(info, Map.class);
	}

	public static <T> T getObjectFromString(String jsonData, Class<T> valueType) {
		return JsonUtils.jsonToPojo(jsonData, valueType);
	}

	public static String converObject2String(Object obj) {
		if (obj == null) {
			obj = "";
		}
		return JsonUtils.objectToJson(obj);
	}

	public static String getContentFromResource(Resource resource) {
		try {
			EncodedResource encRes = new EncodedResource(resource);
			// ② 这样才能正确读取文件的内容，而不会出现乱码
			String content = FileCopyUtils.copyToString(encRes.getReader());
			String result = content.replace("\r\n", "").replaceAll(" +", "");
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void copyMapProperties(Map source, Map target, String... ignoreProperties) throws BeansException {
		if (source == null) {
			return;
		}
		if (target == null) {
			return;
		}
		List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);
		if (ignoreList == null || ignoreList.size() == 0) {
			source.putAll(target);
		} else {
			target.forEach((key, value) -> {
				if (!ignoreList.contains(key)) {
					source.put(key, value);
				}
			});
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object o) throws IllegalArgumentException {
		if (o == null)
			return true;
		if ((o instanceof String)) {
			if (((String) o).length() == 0)
				return true;
		} else if ((o instanceof Collection)) {
			if (((Collection) o).isEmpty())
				return true;
		} else if (o.getClass().isArray()) {
			if (Array.getLength(o) == 0)
				return true;
		} else if ((o instanceof Map)) {
			if (((Map) o).isEmpty())
				return true;
		}
		return false;
	}

	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}

	public static boolean isBasicType(Class<?> type) {
		if (type.isPrimitive())
			return true;

		if (type == String.class) {
			return true;
		} else if (type == Integer.class) {
			return true;
		} else if (type == Long.class) {
			return true;
		} else if (type == Double.class) {
			return true;
		} else if (type == Float.class) {
			return true;
		} else if (type == Short.class) {
			return true;
		} else if (type == Byte.class) {
			return true;
		} else if (type == Boolean.class) {
			return true;
		}

		return false;
	}

	public static <T> T getObjFromStr(String value, Class<T> type) {
		if (type == String.class) {
			return type.cast(value);
		} else if (type == Integer.class) {
			return type.cast(Integer.parseInt(value));
		} else if (type == Long.class) {
			return type.cast(Long.parseLong(value));
		} else if (type == Double.class) {
			return type.cast(Double.parseDouble(value));
		} else if (type == Float.class) {
			return type.cast(Float.parseFloat(value));
		} else if (type == Short.class) {
			return type.cast(Short.parseShort(value));
		} else if (type == Boolean.class) {
			return type.cast(Boolean.parseBoolean(value));
		} else {
			return getObjectFromString(value, type);
		}
	}

	public static String getStrFromObj(Object value) {
		if (isBasicType(value.getClass())) {
			return value.toString();
		}
		return converObject2String(value);
	}

	/**
	 * 利用正则表达式判断字符串是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		System.out.println(md5("12345"));
	}


	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
//putIfAbsent方法添加键值对，如果map集合中没有该key对应的值，则直接添加，并返回null，如果已经存在对应的值，则依旧为原来的值。
//如果返回null表示添加数据成功(不重复)，不重复(null==null :TRUE)
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;

	}
}
