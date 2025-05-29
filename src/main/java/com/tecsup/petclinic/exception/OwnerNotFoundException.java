package com.tecsup.petclinic.exception;


public class OwnerNotFoundException extends Exception { // Hereda de Exception (checked exception)


    private static final long serialVersionUID = 1L;


    public OwnerNotFoundException(String message) {
        super(message); // Pasa el mensaje al constructor de la clase padre (Exception)
    }
}