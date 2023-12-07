package com.example.laborator7.Validators;

import com.example.laborator7.Domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getUser1()==entity.getUser2()){
            throw new ValidationException("Nu te poti adauga pe tine ca si prieten!");
        }
    }
}

