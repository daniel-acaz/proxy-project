package br.com.mercadolivre.proxy.error;

public class ParameterNotFoundException extends RuntimeException {

    public ParameterNotFoundException() {
        super("There is no Parameters to Available Requests");
    }
}
