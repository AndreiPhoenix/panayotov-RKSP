package ru.rksp.panayotov.processor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rksp.panayotov.processor.dto.EventDTO;
import ru.rksp.panayotov.processor.entity.PostgreSQLEvent;
import ru.rksp.panayotov.processor.repository.PostgreSQLRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQListener {

    private final PostgreSQLRepository postgreSQLRepository;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "events.raw")
    @Transactional
    public void processEvent(EventDTO eventDTO) {
        try {
            log.info("Получено событие из RabbitMQ. Номер заказа: {}", eventDTO.getOrderNumber());

            PostgreSQLEvent event = new PostgreSQLEvent();
            event.setOrderNumber(eventDTO.getOrderNumber());
            event.setCustomerPhone(eventDTO.getCustomerPhone());
            event.setOrderDescription(eventDTO.getOrderDescription());
            event.setEventDate(eventDTO.getEventDate());

            PostgreSQLEvent savedEvent = postgreSQLRepository.save(event);

            log.info("Событие сохранено в PostgreSQL. ID: {}, Номер заказа: {}",
                    savedEvent.getId(), savedEvent.getOrderNumber());

        } catch (Exception e) {
            log.error("Ошибка при обработке события: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка обработки события", e);
        }
    }
}