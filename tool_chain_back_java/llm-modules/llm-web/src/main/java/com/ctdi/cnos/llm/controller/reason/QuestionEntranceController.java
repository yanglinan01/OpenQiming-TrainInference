package com.ctdi.cnos.llm.controller.reason;


import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.beans.log.MmLog;
import com.ctdi.cnos.llm.cache.annotation.CtgCacheCounter;
import com.ctdi.cnos.llm.feign.log.LogServiceClientFeign;
import com.ctdi.cnos.llm.feign.reason.CallRemoteInterfaceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 域控制类
 *
 *
 *
 */
@SuppressWarnings({ "rawtypes" })
@Api(tags = { "推理接口" })
@RestController
@RequestMapping(value = "/question")
@Slf4j
public class QuestionEntranceController {

	private static final Logger logger = LoggerFactory.getLogger(QuestionEntranceController.class);

	@Autowired
	private CallRemoteInterfaceClientFeign callRemoteInterfaceClientFeign;

	@Autowired
	private LogServiceClientFeign logServiceClientFeign;

	@Value("${prompt.top_p}")
	private Double topP;

	@Value("${prompt.top_k}")
	private Double topK;

	@Value("${prompt.temperature}")
	private Double temperature;

	@Value("${prompt.max_tokens}")
	private int maxTokens;

	@Value("#{'${prompt.stop_tokenIds}'.split(',')}")
	private List<Integer> stopTokenIds;

	/**
	 * prompt推理
	 */
	@ApiOperation(value="prompt推理")
	@PostMapping(value = "entrance")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "question", value = "question 内容",required = false, paramType = "body"),
			@ApiImplicitParam(name = "url", value = "url",required = false, paramType = "body"),
	})
	@CtgCacheCounter(cacheName = "question_entrance",key = "entrance",limit = 1L,expireTime = 100)
	public OperateResult<Map<String, Object>> entrance(@ApiIgnore @RequestBody(required =false) Map<String,String> map) {
		MmLog mmLog = logServiceClientFeign.dataAssembly("", "","推理接口");
		try {
			String question = MapUtil.getStr(map,"question","");
			String url = MapUtil.getStr(map,"url","");
			long stime = System.currentTimeMillis();
			Map<String, Object> resultMap = callRemoteInterfaceClientFeign.thirdPartyInterfaces(url, question);
			long etime = System.currentTimeMillis();
			String result = resultMap.get("result").toString();
			mmLog.setDuration(etime-stime);
			mmLog.setRequestParams(resultMap.get("paramJson").toString());
			mmLog.setResponseParams(result);
			mmLog.setResponseTime(new Date());
			mmLog.setStatusCode("0");
			mmLog.setInterfaceUrl(url);
			logServiceClientFeign.addLog(mmLog);
			String searchString = "assistant\\n";
			int lastIndexOfAssistant = result.lastIndexOf(searchString);
			String optimizeResult = "";
			if (lastIndexOfAssistant != -1) {
				int startIndex = lastIndexOfAssistant + searchString.length();

				int lastIndexOfEnd = result.lastIndexOf("<|im_end|>");

				if (lastIndexOfEnd != -1 && lastIndexOfEnd > startIndex) {
					optimizeResult = result.substring(startIndex, lastIndexOfEnd);
				} else {
					// "<|im_end|>" 没有找到，或者它在 "assistant\n" 之前
					// 处理这种情况（例如，抛出一个异常或记录一个错误）
					logger.error("解析推理接口返回结果失败,失败原因为在返回结果里面没有找到<|im_end|>字符串");
				}
			}else {
				// "assistant\n" 没有找到
				// 处理这种情况（例如，抛出一个异常或记录一个错误）
				logger.error("解析推理接口返回结果失败,失败原因为在返回结果里面没有找到assistant\\n字符串");
			}
			return new OperateResult(true, "调用成功", optimizeResult);
		} catch (Exception ex) {
			log.error("问答功能异常", ex);
			mmLog.setErrorMessage(ex.getMessage());
			mmLog.setStatusCode("500");
			logServiceClientFeign.addLog(mmLog);
			return new OperateResult(false, ex.getMessage(), null);
		}
	}

	/**
	 * prompt优化
	 * @param map
	 * @return
	 */
	@ApiOperation(value="调用大模型优化接口")
	@PostMapping(value = "optimize")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "prompt", value = "prompt 内容",required = false, paramType = "body"),
			@ApiImplicitParam(name = "url", value = "url",required = false, paramType = "body"),
	})
	public OperateResult<String> optimize(@ApiIgnore @RequestBody(required =false) Map<String,String> map){
		MmLog mmLog = logServiceClientFeign.dataAssembly("", "","优化接口");
		try {
			String prompt = MapUtil.getStr(map,"prompt","");
			String url = MapUtil.getStr(map,"url","");
			String promptStr = "你是一位prompt工程师，" +
					"能够参考用户输入生成优化的prompt模板，" +
					"辅助用户使用大模型。" +
					"优化后的prompt模板可包含变量，" +
					"用{}标识prompt模板中变量。" +
					"用户能够参考优化后的prompt模板生成新的输入内容，" +
					"进而获得更好模型回答。请仅提供优化后的prompt模板，" +
					"不输出其他额外信息。用户的输入为：" + prompt;
			long stime = System.currentTimeMillis();
			Map<String, Object> resultMap = callRemoteInterfaceClientFeign.thirdPartyInterfaces(url, promptStr);
			long etime = System.currentTimeMillis();
			String result = resultMap.get("result").toString();
			logger.info("接口调用返回结果为:" + result);
			mmLog.setDuration(etime-stime);
			mmLog.setRequestParams(resultMap.get("paramJson").toString());
			mmLog.setResponseParams(result);
			mmLog.setResponseTime(new Date());
			mmLog.setStatusCode("0");
			mmLog.setInterfaceUrl(url);
			logServiceClientFeign.addLog(mmLog);
			String searchString = "assistant\\n";
			int lastIndexOfAssistant = result.lastIndexOf(searchString);
			String optimizeResult = "";
			if (lastIndexOfAssistant != -1) {
				int startIndex = lastIndexOfAssistant + searchString.length();

				int lastIndexOfEnd = result.lastIndexOf("<|im_end|>");

				if (lastIndexOfEnd != -1 && lastIndexOfEnd > startIndex) {
					optimizeResult = result.substring(startIndex, lastIndexOfEnd);
				} else {
					// "<|im_end|>" 没有找到，或者它在 "assistant\n" 之前
					// 处理这种情况（例如，抛出一个异常或记录一个错误）
					logger.error("解析优化接口返回结果失败,失败原因为在返回结果里面没有找到<|im_end|>字符串");
				}
			}else {
				// "assistant\n" 没有找到
				// 处理这种情况（例如，抛出一个异常或记录一个错误）
				logger.error("解析优化接口返回结果失败,失败原因为在返回结果里面没有找到assistant\\n字符串");
			}
			return new OperateResult(true, "调用成功", optimizeResult);
		} catch (Exception ex) {
			log.error("优化功能异常", ex);
			mmLog.setErrorMessage(ex.getMessage());
			mmLog.setStatusCode("500");
			logServiceClientFeign.addLog(mmLog);
			return new OperateResult(false, ex.getMessage(), null);
		}
	}
}