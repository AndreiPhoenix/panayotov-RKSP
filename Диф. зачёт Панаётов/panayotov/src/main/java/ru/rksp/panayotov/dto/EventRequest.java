package ru.rksp.panayotov.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание события заказа")
public class EventRequest {

    @NotBlank(message = "Номер заказа обязателен")
    @Schema(description = "Номер заказа", example = "ORD-001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNumber;

    @NotBlank(message = "Номер телефона обязателен")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Некорректный формат номера телефона")
    @Schema(description = "Номер телефона покупателя", example = "+79123456789", requiredMode = Schema.RequiredMode.REQUIRED)
    private String customerPhone;

    @Schema(description = "Описание заказа", example = "Тестовый заказ")
    private String orderDescription;

    @NotBlank(message = "Дата события обязательна")
    @Schema(description = "Дата и время события в формате ISO", example = "2024-12-20T10:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private String eventDate;
}