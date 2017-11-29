package br.com.payup.userservice.exception;

public class ExistingCodeException extends Exception {

	private static final long serialVersionUID = -5739520866459502460L;

	public String getMessage() {
		return "Código existente.";
	}
	
	public String getCode() {
		return "EXISTING_CODE";
	}
	
}
