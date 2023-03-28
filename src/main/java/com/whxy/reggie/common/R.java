package com.whxy.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务端响应的数据最终都会封装成此对象
 * @param <T>
 */
@Data
public class R<T> {

    private Integer code;

    private String msg;

    private T data;

    private Map map = new HashMap();

    public static <T> R<T> success(T object){
        R<T> r = new R<>();
        r.data = object;
        r.code = 1;
        return r;
    }
    public static <T> R<T> error(String msg){
        R<T> r = new R<>();
        r.msg = msg;
        r.code = 0;
        return r;
    }
    public R<T> add(String key,Object value){
        this.map.put(key,value);
        return this;
    }
}
