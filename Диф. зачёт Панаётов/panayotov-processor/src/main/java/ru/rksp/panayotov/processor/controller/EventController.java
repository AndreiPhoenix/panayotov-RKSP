package ru.rksp.panayotov.processor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rksp.panayotov.processor.service.EventService;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Processor Service API", description = "API для обработки событий заказов")
public class EventController {

    private final EventService eventService;

    @PostMapping("/count")
    @Operation(
            summary = "Подсчитать и сохранить количество событий",
            description = "Подсчитывает количество записей в PostgreSQL и сохраняет агрегат в ClickHouse"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Количество успешно подсчитано и сохранено",
                    content = @Content(mediaType = "text/plain")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "text/plain")
            )
    })
    public ResponseEntity<String> countAndSaveEvents() {
        try {
            long count = eventService.countAndSaveEvents();
            return ResponseEntity.ok(String.format(
                    "Количество записей в PostgreSQL: %d. Данные успешно сохранены в ClickHouse.",
                    count
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Ошибка при подсчете событий: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    @Operation(
            summary = "Получить статистику из ClickHouse",
            description = "Возвращает статистику агрегированных данных из ClickHouse"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Статистика успешно получена",
            content = @Content(mediaType = "text/plain")
    )
    public ResponseEntity<String> getClickHouseStats() {
        try {
            String stats = eventService.getClickHouseStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Ошибка при получении статистики: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    @Operation(
            summary = "Проверка здоровья сервиса",
            description = "Возвращает статус работы сервиса"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Сервис работает",
            content = @Content(mediaType = "text/plain")
    )
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Processor Service работает исправно");
    }
}