package com.ctdi.cnos.llm.advise;

import com.ctdi.cnos.llm.exception.DataValidationException;
import com.ctdi.cnos.llm.exception.MyRuntimeException;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.MessageUtils;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;


/**
 * <h1>全局异常处理</h1>
 *
 *
 */
@RestControllerAdvice("com.ctdi.cnos.llm")
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public OperateResult<String> handleException(Exception ex, HttpServletRequest request){
        //String message = ExceptionUtil.stacktraceToString(ex);
        // log.error(" GlobalExceptionAdviceError =>",ex);
        // return CommonResponseUtil.failure(ex.getMessage());
        log.error("Unhandled exception from URL [" + request.getRequestURI() + "]", ex);
        return OperateResult.error(ErrorCodeEnum.UNHANDLED_EXCEPTION, ex.getMessage());
    }

    /**
     * FeignException异常。
     *
     * @param ex       异常对象。
     * @param request  http请求。
     * @return 应答对象。
     */
    // @ExceptionHandler(value = FeignException.class)
    public OperateResult<String> feignExceptionHandle(FeignException ex, HttpServletRequest request) {
        log.error("FeignException exception from URL [" + request.getRequestURI() + "]", ex);
        return OperateResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, MessageUtils.getMessage(ex.getMessage()));
    }

    /**
     * 自定义运行时异常。
     *
     * @param ex       异常对象。
     * @param request  http请求。
     * @return 应答对象。
     */
    @ExceptionHandler(value = MyRuntimeException.class)
    public OperateResult<String> myRuntimeExceptionHandle(Exception ex, HttpServletRequest request) {
        log.error("MyRuntimeException exception from URL [" + request.getRequestURI() + "]", ex);
        return OperateResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, ex.getMessage());
    }

    /**
     * 处理数据验证失败异常。
     *
     * @param ex       异常对象。
     * @param request  http请求。
     * @return 应答对象。
     */
    @ExceptionHandler(value = DataValidationException.class)
    public OperateResult<String> handleMethodArgumentNotValid(DataValidationException ex, HttpServletRequest request) {
        log.error("DataValidationException exception from URL [" + request.getRequestURI() + "]", ex);
        return OperateResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, ex.getMessage());
    }


    /**
     * 处理绑定异常。
     *
     * @param ex       异常对象。
     * @param request  http请求。
     * @return 应答对象。
     */
    @ExceptionHandler(value = BindException.class)
    public OperateResult<String> handleBindException(BindException ex, HttpServletRequest request) {
        log.error("BindException exception from URL [" + request.getRequestURI() + "]", ex);
        String errorMessages = ex.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return OperateResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessages);
    }

    /**
     * 处理方法参数无效异常。
     *
     * @param ex       异常对象。
     * @param request  http请求。
     * @return 应答对象。
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public OperateResult<String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("MethodArgumentNotValidException exception from URL [" + request.getRequestURI() + "]", ex);
        String errorMessages = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return OperateResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessages);
    }
}