-- База данных для агрегированных данных
CREATE DATABASE IF NOT EXISTS aggregates_db;

-- Таблица для агрегатов событий заказов
CREATE TABLE IF NOT EXISTS aggregates_db.агрегаты_событий_заказов (
                                                                      дата_и_время_записи DateTime DEFAULT now(),
    количество_записей UInt64
    ) ENGINE = MergeTree()
    ORDER BY дата_и_время_записи;