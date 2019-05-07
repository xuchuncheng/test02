package com.dmcn.two.common.exception;

/**
 * <p>CustomException.java</p>
 * <p>description:自定义RuntimeException异常，用于异常原因不可恢复的情景</p>
 *
 * @author simon
 * @version 1.1  20170701
 */
public class CustomException extends RuntimeException
{

    /**
     * 默认serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    private Integer code;
    private String msg;

    /**
     * 构造函数
     */
    public CustomException()
    {
    }

    /**
     * 构造函数
     *
     * @param message 异常信息
     */
    public CustomException(String message)
    {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param cause 可抛出的异常类
     */
    public CustomException(Throwable cause)
    {
        super(cause);
    }

    /**
     * 构造函数
     *
     * @param message 异常信息
     * @param cause   可抛出的异常类
     */
    public CustomException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * 构造函数
     *
     * @param code  异常编码
     * @param msg   异常信息
     * @param cause 可抛出的异常类
     */
    public CustomException(Integer code, String msg, Throwable cause)
    {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 构造函数
     *
     * @param code 异常编码
     * @param msg  异常信息
     */
    public CustomException(Integer code, String msg)
    {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}
