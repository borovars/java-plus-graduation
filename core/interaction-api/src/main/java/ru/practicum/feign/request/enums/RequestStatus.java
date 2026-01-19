package ru.practicum.feign.request.enums;

public enum RequestStatus {
    /**
     * Подтверждена
     */
    CONFIRMED,

    /**
     * Отклонена
     */
    REJECTED,

    /**
     * На рассмотрении
     */
    PENDING,

    /**
     * Отменена
     */
    CANCELED
}
