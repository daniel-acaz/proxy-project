package br.com.mercadolivre.proxy.error;

public class ExceededRequestException extends Exception {

    public ExceededRequestException() {
        super("You have exceeded the requests limit");
    }
}
