package com.example.laborator7.Validators;


import com.example.laborator7.Domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        validateFirstName(entity.getFirstName());
        validateLastName(entity.getLastName());
        validateEmail(entity.getEmail());
        validateAge(entity.getAge());
    }
    private void validateFirstName(String firstName) throws ValidationException {
        if(firstName == null)
            throw new ValidationException("Numele nu trebuie sa fie null!");
        else if(firstName.length() >= 100)
            throw new ValidationException("Numele e prea lung!");
        else if(firstName.isEmpty())
            throw new ValidationException("Numele nu trebuie sa fie gol!");
        else if(! Character.isAlphabetic(firstName.charAt(0)))
            throw new ValidationException("Primul caracter din nume trebuie sa fie o litera!");
    }
    private void validateLastName(String lastName) throws ValidationException {
        if(lastName == null)
            throw new ValidationException("Prenumele nu trebuie sa fie null!");
        else if(lastName.length() >= 100)
            throw new ValidationException("Prenumele e prea lung!");
        else if(lastName.isEmpty())
            throw new ValidationException("Prenumele nu trebuie sa fie gol!");
        else if(! Character.isAlphabetic(lastName.charAt(0)))
            throw new ValidationException("Primul caracter din prenume trebuie sa fie o litera!");
    }
    private void validateEmail(String email) throws ValidationException {
        if(email == null)
            throw new ValidationException("Email nu trebuie sa fie null!");
        else if(email.length() >= 100)
            throw new ValidationException("Email prea lung!");
        else if(email.isEmpty())
            throw new ValidationException("Email nu trebuie sa fie gol!");
        else if(email.chars().filter(ch -> ch == '@').count() != 1)
            throw new ValidationException("Email gresit!");
    }

    private void validateAge(Integer age) throws ValidationException{
        if(age==null)
            throw new ValidationException("Varsta nu trebuie sa fie null!");
        else if(age>100)
            throw new ValidationException("Varsta invalida!");
        else if(age<0)
            throw new ValidationException("Varsta nu poate fi negativa!");
    }
}

