package br.com.mercadolivre.statistic.error;

public class StatisticNotFoundException extends RuntimeException {

    public StatisticNotFoundException(String message) {
        super(message);
    }
}
