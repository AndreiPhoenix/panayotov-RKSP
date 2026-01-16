# Экзаменационная работа - Система обработки событий заказов
## 📋 Общее описание
Система состоит из двух микросервисов для обработки событий заказов:

ingest-service (порт 8081) - Принимает HTTP запросы и отправляет события в RabbitMQ

processor-service (порт 8082) - Обрабатывает события из RabbitMQ, сохраняет в PostgreSQL и агрегирует в ClickHouse

## 🏗️ Архитектура
```
┌─────────────┐     HTTP     ┌─────────────┐     RabbitMQ     ┌──────────────┐
│   Клиент    │ ───────────> │ ingest-     │ ───────────────> │  processor-  │
│   (Postman, │              │  service    │                  │   service    │
│    curl)    │ <─────────── │ (8081)      │ <─────────────── │   (8082)     │
└─────────────┘    Ответ     └─────────────┘    Очередь       └──────┬───────┘
                                                                      │
                                                                      ▼
                                                              ┌─────────────────┐
                                                              │   PostgreSQL    │
                                                              │     (5432)      │
                                                              │  сырые события  │
                                                              └────────┬────────┘
                                                                       │
                                                                       ▼
                                                              ┌─────────────────┐
                                                              │   ClickHouse    │
                                                              │     (8123)      │
                                                              │   агрегаты      │
                                                              └─────────────────┘
```

# 📁 Структура проектов
## ingest-service
```
panayotov/
├── src/main/java/ru/rksp/panayotov/
│   ├── IngestServiceApplication.java
│   ├── config/
│   │   ├── RabbitMQConfig.java
│   │   └── SwaggerConfig.java
│   ├── controller/
│   │   └── EventController.java
│   └── dto/
│       └── EventRequest.java
├── src/main/resources/
│   └── application.yml
├── pom.xml
└── README.md
```

## processor-service
```
panayotov-processor/
├── src/main/java/ru/rksp/panayotov/processor/
│   ├── ProcessorApplication.java
│   ├── config/
│   │   ├── DataSourceConfig.java
│   │   ├── RabbitMQConfig.java
│   │   └── SwaggerConfig.java
│   ├── controller/
│   │   └── EventController.java
│   ├── dto/
│   │   └── EventDTO.java
│   ├── entity/
│   │   └── PostgreSQLEvent.java
│   ├── repository/
│   │   └── PostgreSQLRepository.java
│   └── service/
│       ├── EventService.java
│       └── RabbitMQListener.java
├── src/main/resources/
│   └── application.yml
├── sql/
│   ├── postgres-init.sql
│   └── clickhouse-init.sql
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## 🚀 Быстрый старт
### 1. Запуск инфраструктуры
```
# Перейдите в папку processor-service
cd panayotov-processor

# Создайте SQL файлы
mkdir -p sql
echo "CREATE TABLE IF NOT EXISTS сырые_события_заказов (
    идентификатор BIGSERIAL PRIMARY KEY,
    номер_заказа VARCHAR(50) NOT NULL,
    номер_телефона_покупателя VARCHAR(20) NOT NULL,
    описание_заказа TEXT,
    дата_события TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);" > sql/postgres-init.sql

echo "CREATE DATABASE IF NOT EXISTS aggregates_db;
CREATE TABLE IF NOT EXISTS aggregates_db.агрегаты_событий_заказов (
    дата_и_время_записи DateTime DEFAULT now(),
    количество_записей UInt64
) ENGINE = MergeTree()
ORDER BY дата_и_время_записи;" > sql/clickhouse-init.sql

# Запустите контейнеры
docker-compose up -d

# Подождите 15 секунд для запуска БД
sleep 15
```

### 2. Запуск processor-service
```
# Сборка и запуск processor-service
cd panayotov-processor
mvn clean package
java -jar target/panayotov-processor-0.0.1-SNAPSHOT.jar
```

### 3. Запуск ingest-service (в другом терминале)
```
# Сборка и запуск ingest-service
cd panayotov
mvn clean package
java -jar target/panayotov-0.0.1-SNAPSHOT.jar
```

## 🔧 Конфигурация
### Порты
ingest-service: 8081

processor-service: 8082

PostgreSQL: 5432

ClickHouse: 8123 (HTTP), 9000 (TCP)

RabbitMQ: 5672 (AMQP), 15672 (Management UI)

Доступ к админ-панелям
RabbitMQ Management: http://localhost:15672 (guest/guest)

Swagger UI ingest-service: http://localhost:8081/swagger-ui.html

Swagger UI processor-service: http://localhost:8082/swagger-ui.html
