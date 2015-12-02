package com.verint.kata.mutation.util;

public interface ICreditCardValidator {

    public ICreditCardValidator getInstance();

    public Boolean validate(String cardNumber);

}
