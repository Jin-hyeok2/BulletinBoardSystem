package com.personal.bulletinboardsystem.user;

public class ExistsEmailException extends RuntimeException{
    public ExistsEmailException(String s) {
        super(s);
    }
}
