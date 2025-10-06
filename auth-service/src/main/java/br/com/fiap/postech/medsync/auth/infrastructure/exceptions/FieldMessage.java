package br.com.fiap.postech.medsync.auth.infrastructure.exceptions;

public record FieldMessage(String fieldName, String message) {

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}