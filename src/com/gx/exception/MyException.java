package com.gx.exception;

/**
 * 自定义异常
 * Checked异常
 */
public class MyException extends Exception{
    public MyException(String message) {
        super(message);
    }
}
