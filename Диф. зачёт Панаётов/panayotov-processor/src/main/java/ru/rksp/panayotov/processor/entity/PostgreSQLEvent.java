package ru.rksp.panayotov.processor.entity;

import lombok.Data;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "сырые_события_заказов")
@Data
public class PostgreSQLEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "идентификатор")
    private Long id;

    @Column(name = "номер_заказа", nullable = false, length = 50)
    private String orderNumber;

    @Column(name = "номер_телефона_покупателя", nullable = false, length = 20)
    private String customerPhone;

    @Column(name = "описание_заказа")
    private String orderDescription;

    @Column(name = "дата_события", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}