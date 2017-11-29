package br.com.payup.userservice.exception;

public class InactiveCodeException extends Exception {

	private static final long serialVersionUID = -5739520866459502460L;

	public String getMessage() {
		return "Código inativo.";
	}
	
	public String getCode() {
		return "INACTIVE_CODE";
	}
	
}
