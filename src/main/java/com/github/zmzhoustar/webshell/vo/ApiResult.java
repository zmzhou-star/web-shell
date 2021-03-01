package com.github.zmzhoustar.webshell.vo;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import lombok.Data;

/**
 * 返回值对象
 * @author zmzhou
 * @date 2020/07/02 17:11
 */
@Data
public class ApiResult<T> implements Serializable {
	/** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** 消息状态码 */
    private int code = HttpStatus.OK.value();
    /** 消息内容 */
    private String msg = HttpStatus.OK.getReasonPhrase();
    /** 列表数据 */
    private transient T data;
    /**
     * 构造器
     * @author zmzhou
     * @date 2020/9/12 11:33
     */
    public ApiResult() {
    	// 构造器
    }

    /**
     * 错误请求
     * @author zmzhou
     * @date 2020/07/03 14:12
     */
    public static <T> ApiResult<T> badRequest() {
        ApiResult<T> result = new ApiResult<>();
        result.setCode(HttpStatus.BAD_REQUEST.value());
        result.setMsg(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return result;
    }
    public static <T> ApiResult<T> builder() {
        return new ApiResult<>();
    }

    /**
     * 请求错误设置失败信息
     * @param code 消息状态码
     * @param errorMsg 错误信息
     * @return ApiResult<T>
     * @author zmzhou
     * @date 2020/07/08 14:05
     */
    public ApiResult<T> error(int code, String errorMsg) {
        setCode(code);
        if (StringUtils.isNotBlank(errorMsg)) {
            setMsg(errorMsg);
        }
		return this;
	}
    /**
     * 请求成功设置返回信息
     * @param msg 返回信息
     * @return ApiResult<T>
     * @author zmzhou
     * @date 2020/07/08 14:05
     */
    public ApiResult<T> info(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            setMsg(msg);
        }
		return this;
	}
	/**
	 * 设置返回数据
	 * @param data 返回数据
	 * @return ApiResult<T>
	 * @author zmzhou
	 * @date 2020/9/9 22:17
	 */
    public ApiResult<T> data(T data) {
        if (null != data) {
            setData(data);
        }
		return this;
	}
}
