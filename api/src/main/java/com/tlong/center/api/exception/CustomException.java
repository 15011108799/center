package com.tlong.center.api.exception;

/**
 * 自定义异常
 */
public class CustomException extends RuntimeException{
    private static final long serialVersionUID = -5122679181700079266L;

    public CustomException(){
    }

    public CustomException(String s){
        super(s);
    }

}
