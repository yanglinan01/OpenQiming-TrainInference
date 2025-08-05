package com.ctdi.cnos.llm.response;

/**
 */

public enum HttpStatusCodeEnum {
	OK(200,"OK","请求已经成功处理"),
	BAD_REQUEST(400,"Bad Request","请求错误，请修正请求"),
	NOT_FONUD(404,"Not Found","资源未找到"),
	INTERNAL_SERVER_ERROR(500,"Internal Server Error","服务器内部错误");
	HttpStatusCodeEnum(int code, String enMsg, String zhMsg) {
	}
}
