package com.valdomiro.curso.services.exceptions;

public class JWTAuthorizationException extends RuntimeException {

private static final long serialVersionUID = 1L;
	
	public JWTAuthorizationException(String msg) {
		super(msg);
	}
}
