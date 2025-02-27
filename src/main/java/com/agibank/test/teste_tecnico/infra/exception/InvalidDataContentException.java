package com.agibank.test.teste_tecnico.infra.exception;


import org.springframework.web.bind.annotation.ExceptionHandler;

public class CpfJaCadastradoException extends RuntimeException{

    public CpfJaCadastradoException (String message) {
        super(message);
    }

}
