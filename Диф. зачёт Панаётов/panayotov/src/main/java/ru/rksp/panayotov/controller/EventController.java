package ru.rksp.panayotov.controller;

import ru.rksp.panayotov.dto.EventRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Events", description = "API для работы с событиями заказов")
public class EventController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping
    @Operation(
            summary = "Создать новое событие заказа",
            description = "Принимает событие заказа и отправляет его в очередь RabbitMQ events.raw"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Событие успешно отправлено",
                    content = @Content(mediaType = "text/plain")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса",
                    content = @Content(mediaType = "text/plain")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "text/plain")
            )
    })
    public ResponseEntity<String> createEvent(
            @Parameter(description = "Данные события заказа", required = true)
            @Valid @RequestBody EventRequest request) {

        try {
            log.info("Получено событие заказа: {}", request.getOrderNumber());

            // Отправляем в RabbitMQ
            rabbitTemplate.convertAndSend("events.raw", request);

            log.info("Событие отправлено в RabbitMQ очередь events.raw");

            return ResponseEntity.ok("Событие успешно отправлено в очередь events.raw");

        } catch (Exception e) {
            log.error("Ошибка при обработке события: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при отправке события: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Проверка здоровья сервиса", description = "Возвращает статус работы сервиса")
    @ApiResponse(responseCode = "200", description = "Сервис работает")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Ingest Service работает исправно");
    }
}