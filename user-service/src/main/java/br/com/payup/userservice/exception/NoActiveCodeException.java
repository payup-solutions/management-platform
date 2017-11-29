package br.com.payup.userservice.exception;

public class NoActiveCodeException extends Exception {

	private static final long serialVersionUID = -5739520866459502460L;

	public String getMessage() {
		return "Não há código ativo.";
	}
	
	public String getCode() {
		return "NO_ACTIVE_CODE";
	}
	
}
