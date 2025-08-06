package com.ctdi.cnos.llm.controller.reason;


import com.ctdi.cnos.llm.feign.reason.DomainServiceClientFeign;

import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 域控制类
 *
 *
 *
 */
@SuppressWarnings({ "rawtypes" })
@Api(tags = { "域操作接口" })
@RestController
@RequestMapping(value = "/domain")
@Slf4j
public class DomainController {

	@Autowired
	private DomainServiceClientFeign domainServiceClientFeign;

	/**
	 * 查询域[rows:数据清单,total:总条数]
	 */
	@ApiOperation(value="查询域[rows:数据清单,total:总条数]")
	@PostMapping(value = "query")
	public OperateResult<Map<String, Object>> query(@RequestBody(required = false) Map param) {
		try {
			Map<String, Object> retMap = domainServiceClientFeign.query(param);
			return new OperateResult<>(true, null, retMap);
		} catch (Exception ex) {
			log.error("资源码查询异常", ex);
			return new OperateResult<>(false, ex.getMessage(), null);
		}
	}
}