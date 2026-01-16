package ru.rksp.panayotov.processor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rksp.panayotov.processor.repository.PostgreSQLRepository;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final PostgreSQLRepository postgreSQLRepository;

    @Qualifier("clickHouseJdbcTemplate")
    private final JdbcTemplate clickHouseJdbcTemplate;

    @Transactional(readOnly = true)
    public long countAndSaveEvents() {
        try {
            long count = postgreSQLRepository.count();
            log.info("Найдено записей в PostgreSQL: {}", count);

            saveToClickHouse(count);

            return count;
        } catch (Exception e) {
            log.error("Ошибка при подсчете событий: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при подсчете событий", e);
        }
    }

    private void saveToClickHouse(long count) {
        String sql = "INSERT INTO aggregates_db.агрегаты_событий_заказов (количество_записей) VALUES (?)";

        try {
            clickHouseJdbcTemplate.update(sql, count);
            log.info("Сохранено в ClickHouse: {} записей", count);
        } catch (Exception e) {
            log.error("Ошибка при сохранении в ClickHouse: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при сохранении в ClickHouse", e);
        }
    }

    public String getClickHouseStats() {
        String sql = "SELECT COUNT() as total_aggregates FROM aggregates_db.агрегаты_событий_заказов";

        try {
            Integer count = clickHouseJdbcTemplate.queryForObject(sql, Integer.class);
            return "Всего агрегатов в ClickHouse: " + (count != null ? count : 0);
        } catch (Exception e) {
            return "Статистика недоступна: " + e.getMessage();
        }
    }
}