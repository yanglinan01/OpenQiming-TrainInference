package com.ctdi.cnos.llm.config;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.ContentType;
import feign.form.MultipartFormContentProcessor;
import feign.form.spring.SpringFormEncoder;
import feign.form.spring.SpringManyMultipartFilesWriter;
import feign.form.spring.SpringSingleMultipartFileWriter;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SpringMultipartEncoder extends SpringFormEncoder {

	public SpringMultipartEncoder(Encoder delegate) {
		super(delegate);
		MultipartFormContentProcessor processor = (MultipartFormContentProcessor) getContentProcessor(ContentType.MULTIPART);
		processor.addWriter(new SpringSingleMultipartFileWriter());
		processor.addWriter(new SpringManyMultipartFilesWriter());
	}

	@Override
	public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
		// 修复多个List<MultipartFile>文件上传feign覆盖问题
		if (this.isMultipartFileCollection(object)) {
			Iterable<?> iterable = (Iterable)object;
			HashMap<String, Object> data = new HashMap<>();
			Iterator var13 = iterable.iterator();
			while(var13.hasNext()) {
				Object item = var13.next();
				MultipartFile file = (MultipartFile)item;
				if (data.get(file.getName()) != null && data.get(file.getName()) instanceof List) {
					List list = (List) data.get(file.getName());
					list.add(file);
				} else {
					List list = new ArrayList();
					list.add(file);
					data.put(file.getName(), list);
				}
			}
			super.encode(data, MAP_STRING_WILDCARD, template);
			return;
		}
		super.encode(object, bodyType, template);
	}

	private boolean isMultipartFileCollection(Object object) {
		if (!(object instanceof Iterable)) {
			return false;
		} else {
			Iterable<?> iterable = (Iterable)object;
			Iterator<?> iterator = iterable.iterator();
			return iterator.hasNext() && iterator.next() instanceof MultipartFile;
		}
	}
}
