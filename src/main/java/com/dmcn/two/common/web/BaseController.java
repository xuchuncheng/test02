package com.dmcn.two.common.web;


import com.dmcn.two.common.Constants;
import com.dmcn.two.common.exception.BizException;
import com.dmcn.two.common.exception.CustomException;
import com.dmcn.two.common.utils.JsonUtil;
import com.dmcn.two.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.NamedThreadLocal;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.text.SimpleDateFormat;
import java.util.Set;

/**
 * Controller基类(针对ajax使用的api做了异常处理的优化)
 */
public abstract class BaseController {
    protected static final NamedThreadLocal<Integer> errCode = new NamedThreadLocal<Integer>(Constants.ERR_CODE);
    protected static final NamedThreadLocal<String> errMsg = new NamedThreadLocal<String>(Constants.ERR_MSG);
    /**
     * 日期格式
     */
    protected static final String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式
     */
    protected static final String TIME_PATTERN = "HH:mm:ss";
    /**
     * 24小时制日期时间格式
     */
    protected static final String DATETIME_PATTERN_24H = "yyyy-MM-dd HH:mm:ss";
    /**
     * html类型响应头信息
     */
    protected static final String HTML_PRODUCE_TYPE = "text/html;charset=UTF-8";
    /**
     * json类型响应头信息
     */
    protected static final String JSON_PRODUCE_TYPE = "application/json;charset=UTF-8";
    /**
     * 记录日志
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 页面提交后的参数转换<br>
     * 字符串日期类型转换为java.util.Date类型
     * 字符串日期时间类型转换为java.sql.Timestamp类型
     *
     * @param binder
     */
    @InitBinder
    protected void initDateBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        dateFormat.setLenient(true);
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));

        SimpleDateFormat datetimeFormat = new SimpleDateFormat(DATETIME_PATTERN_24H);
        datetimeFormat.setLenient(true);
        binder.registerCustomEditor(java.sql.Timestamp.class, new CustomDateEditor(datetimeFormat, true));

        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_PATTERN);
        timeFormat.setLenient(false);
        binder.registerCustomEditor(java.sql.Time.class, new CustomDateEditor(timeFormat, true));
    }

    /**
     * 根据相对路径取得绝对路径
     *
     * @param request
     * @param relativePath
     * @return
     */
    protected String getRealPath(HttpServletRequest request, String relativePath) {
        return request.getSession().getServletContext().getRealPath(relativePath);
    }

    /**
     * 默认处理查询结果的返回值
     *
     * @param checkObject
     * @param result
     * @return
     */
    protected String dealQueryResult(Object checkObject, Object result) {
        if (StringUtils.isEmpty(checkObject)){

            return JsonUtil.getResponseJsonNotEmpty(-1, "查无信息", null);
        }
        else{

            return JsonUtil.getResponseJson(0, "查询成功", result);
        }
    }

    /**
     * 默认处理增删改操作的结果返回值
     *
     * @param msg
     * @param result
     * @return
     */
    protected String dealSuccessResutl(String msg, Object result) {
        return JsonUtil.getResponseJson(0, msg, result);
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public String customException(CustomException e) {
        Integer code = e.getCode();
        String msg = e.getMsg();
        if (code == null) code = 2000;
        if (msg == null) msg = "运行异常";
        return JsonUtil.getResponseJsonNotEmpty(e.getCode(), e.getMsg(), null);
    }

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public String bizException(BizException e) {
        Integer code = e.getCode();
        String msg = e.getMsg();
        if (code == null) code = 2000;
        if (msg == null) msg = "业务异常";
        logger.error("code===>{},msg===>{}", code, msg);
        logger.error(e.getMessage(), e);
        return JsonUtil.getResponseJsonNotEmpty(e.getCode(), e.getMsg(), null);
    }

    /**
     * 异常处理：缺少请求参数
     * required_parameter_is_not_present
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public String missingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error("缺少请求参数", e);
        return JsonUtil.getResponseJsonNotEmpty(3000, "缺少请求参数", null);
    }

    /**
     * 异常处理：不支持当前请求方法
     * request_method_not_supported
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public String httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("不支持当前请求方法", e);
        return JsonUtil.getResponseJsonNotEmpty(3002, "不支持当前请求方法", null);
    }

    /**
     * 异常处理：不支持当前媒体类型
     * content_type_not_supported
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public String httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        logger.error("不支持当前媒体类型", e);
        return JsonUtil.getResponseJsonNotEmpty(3004, "不支持当前媒体类型", null);
    }

    /**
     * 异常处理：参数解析失败
     * could_not_read_json
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public String httpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("参数解析失败", e);
        return JsonUtil.getResponseJsonNotEmpty(4002, "参数解析失败", null);
    }

    /**
     * 异常处理：参数验证失败
     * validation_exception
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public String validationException(ValidationException e) {
        logger.error("参数验证失败", e);
        return JsonUtil.getResponseJsonNotEmpty(4004, "参数验证失败", null);
    }

    /**
     * 异常处理：参数验证失败
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public String constraintViolationException(ConstraintViolationException e) {
        logger.error("参数验证失败", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        return JsonUtil.getResponseJsonNotEmpty(4006, "参数验证失败:" + message, null);
    }

    /**
     * 异常处理：参数验证失败
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public String methodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("参数验证失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return JsonUtil.getResponseJsonNotEmpty(4008, "参数验证失败:" + message, null);
    }

    /**
     * 异常处理：参数类型错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public String methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.error("参数类型错误", e);
        return JsonUtil.getResponseJsonNotEmpty(4000, "参数类型错误", null);
    }

    /**
     * 操作数据库出现异常:名称重复，外键关联
     *
     * @param e
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public String dataIntegrityViolationException(DataIntegrityViolationException e) {
        logger.error("操作数据库出现异常:名称重复，外键关联", e);
        return JsonUtil.getResponseJsonNotEmpty(2002, "操作数据库出现异常:名称重复，外键关联", null);
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exception(Exception e) throws Exception {
        String msg = errMsg.get();
        Integer code = errCode.get();
        if (msg == null) msg = "系统异常";
        if (code == null) code = 2000;
        errMsg.remove();
        errCode.remove();
        logger.error(msg + ":" + e.getMessage(), e);
        return JsonUtil.getResponseJsonNotEmpty(code, msg, e.getMessage());
    }


}
