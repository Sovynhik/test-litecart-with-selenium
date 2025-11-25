package ru.rsreu.sovynhik.calculation;

// Листинг 3. Класс обработки исключения
public class NotSuchMarkException extends Exception {
    public NotSuchMarkException() {
    }
    public NotSuchMarkException(String message) {
        super(message);
    }
}