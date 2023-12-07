package com.example.laborator7.Validators;

public interface Validator<E> {
    void validate(E Entity) throws ValidationException;
}
