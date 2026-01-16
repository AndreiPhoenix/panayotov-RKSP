-- Таблица для сырых событий заказов
CREATE TABLE IF NOT EXISTS сырые_события_заказов (
                                                     идентификатор BIGSERIAL PRIMARY KEY,
                                                     номер_заказа VARCHAR(50) NOT NULL,
    номер_телефона_покупателя VARCHAR(20) NOT NULL,
    описание_заказа TEXT,
    дата_события TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );