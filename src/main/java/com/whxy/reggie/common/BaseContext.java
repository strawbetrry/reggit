package com.whxy.reggie.common;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void set(Long id){
        threadLocal.set(id);
    }
    public static Long get(){
        return threadLocal.get();
    }
    public static void remove(){
        threadLocal.remove();
    }
}
