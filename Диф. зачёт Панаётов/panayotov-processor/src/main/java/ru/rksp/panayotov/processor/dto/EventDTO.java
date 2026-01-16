package ru.rksp.panayotov.processor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "DTO события заказа из RabbitMQ")
public class EventDTO {

    @Schema(description = "Номер заказа", example = "ORD-001")
    private String orderNumber;

    @Schema(description = "Номер телефона покупателя", example = "+79123456789")
    private String customerPhone;

    @Schema(description = "Описание заказа", example = "Тестовый заказ")
    private String orderDescription;

    @Schema(description = "Дата события", example = "2024-12-20T10:30:00")
    private LocalDateTime eventDate;
}