package br.com.fiap.postech.medsync.auth.infrastructure.exceptions;

public class BusinessException extends RuntimeException{

    public BusinessException(String msg) {
        super(msg);
    }
}