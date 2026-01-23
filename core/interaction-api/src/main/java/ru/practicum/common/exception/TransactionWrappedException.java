package ru.practicum.common.exception;

public class TransactionWrappedException extends RuntimeException {

    public TransactionWrappedException(Exception message) {
        super(message);
    }
}