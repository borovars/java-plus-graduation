# Explore With Me (EWM)

Система управления событиями на основе микросервисной архитектуры, позволяющая пользователям создавать события, участвовать в них, оставлять комментарии и управлять подборками событий.

## Архитектура

### Миграция проекта на микросервисную архитектуру

Проект переведён с монолитной архитектуры (main-service) на микросервисную архитектуру с использованием Spring Cloud.

#### Основные изменения

1. **Создание новых микросервисов**
   - `category-service` - управление категориями событий
   - `comment-service` - управление комментариями к событиям
   - `compilation-service` - управление подборками событий
   - `event-service` - управление событиями
   - `location-service` - управление локациями
   - `request-service` - управление запросами на участие в событиях
   - `user-service` - управление пользователями
   - `stats-server`- сервис статистики просмотров событий

2. **Общий модуль interaction-api**
   - Содержит Feign-клиенты, DTO, исключения и общие конфигурации
   - Используется для взаимодействия между сервисами

3. **Изменения в моделях данных**
   - JPA-связи (@ManyToOne, @OneToOne) заменены на простые Long-идентификаторы
   - Добавлены Feign-вызовы для получения связанных данных

4. **Внутренние контроллеры (Internal*Controller)**
   - Реализуют контракты из interaction-api
   - Предоставляют API для межсервисного взаимодействия

5. **Обновление конфигурации сервисов**
   - Каждый сервис имеет свою базу данных (отдельные схемы и порты)
   - Добавлена поддержка:
     - Spring Cloud Config
     - Eureka
     - OpenFeign
   - Настроены application.yaml для каждого сервиса

6. **Обновление docker-compose.yaml**
   - Добавлены отдельные контейнеры баз данных для каждого сервиса

### Инфраструктурные сервисы

- **gateway-server** - API Gateway для маршрутизации запросов к микросервисам
- **discovery-server** - Eureka Server для регистрации и обнаружения сервисов
- **config-server** - Spring Cloud Config Server для централизованного управления конфигурацией


## Функциональность

Система предоставляет следующие возможности:

- **Управление пользователями** - создание, просмотр и удаление пользователей
- **Управление категориями** - создание, редактирование и удаление категорий событий
- **Управление событиями** - создание, редактирование, публикация и просмотр событий
- **Управление запросами** - подача заявок на участие в событиях, подтверждение/отклонение заявок
- **Управление комментариями** - добавление, редактирование и удаление комментариев к событиям
- **Управление подборками** - создание и управление подборками событий
- **Статистика** - сбор и просмотр статистики просмотров событий

## API Эндпоинты

### Category Service

#### Public Endpoints
- **GET** `/categories` - Получение списка категорий
- **GET** `/categories/{catId}` - Получение категории по ID

#### Admin Endpoints
- **POST** `/admin/categories` - Создание новой категории
- **PATCH** `/admin/categories/{id}` - Обновление категории
- **DELETE** `/admin/categories/{id}` - Удаление категории

### Comment Service

#### Private Endpoints
- **POST** `/users/{userId}/events/{eventId}/comments` - Создание комментария к событию
- **PATCH** `/users/{userId}/events/{eventId}/comments/{commentId}` - Редактирование комментария
- **DELETE** `/users/{userId}/events/{eventId}/comments/{commentId}` - Удаление комментария

#### Admin Endpoints
- **DELETE** `/admin/events/{eventId}/comments/{commentId}` - Удаление комментария администратором
- **GET** `/admin/events/{eventId}/comments/{commentId}` - Получение комментария по ID

#### Public Endpoints
- **GET** `/events/{eventId}/comments` - Получение списка комментариев к событию

### Compilation Service

#### Public Endpoints
- **GET** `/compilations` - Получение списка подборок событий
- **GET** `/compilations/{compId}` - Получение подборки по ID

#### Admin Endpoints
- **POST** `/admin/compilations` - Создание новой подборки
- **PATCH** `/admin/compilations/{compId}` - Обновление подборки
- **DELETE** `/admin/compilations/{compId}` - Удаление подборки

### Event Service

#### Public Endpoints
- **GET** `/events` - Получение списка событий с фильтрацией
- **GET** `/events/{eventId}` - Получение события по ID

#### Private Endpoints
- **GET** `/users/{userId}/events` - Получение событий текущего пользователя
- **POST** `/users/{userId}/events` - Создание нового события
- **GET** `/users/{userId}/events/{eventId}` - Получение события пользователя по ID
- **PATCH** `/users/{userId}/events/{eventId}` - Обновление события
- **GET** `/users/{userId}/events/{eventId}/requests` - Получение запросов на участие в событии
- **PATCH** `/users/{userId}/events/{eventId}/requests` - Изменение статуса запросов на участие

#### Admin Endpoints
- **GET** `/admin/events` - Получение списка событий для администратора
- **PATCH** `/admin/events/{eventId}` - Обновление события администратором

### Request Service

#### Private Endpoints
- **GET** `/users/{userId}/requests` - Получение запросов текущего пользователя
- **POST** `/users/{userId}/requests` - Создание запроса на участие в событии
- **PATCH** `/users/{userId}/requests/{requestId}/cancel` - Отмена запроса на участие

### User Service

#### Admin Endpoints
- **GET** `/admin/users` - Получение списка пользователей
- **POST** `/admin/users` - Создание нового пользователя
- **DELETE** `/admin/users/{userId}` - Удаление пользователя

### Stats Service

#### Public Endpoints
- **POST** `/hit` - Сохранение информации о просмотре события
- **GET** `/stats` - Получение статистики просмотров

## Технологический стек

- **Java** - язык программирования
- **Spring Boot** - фреймворк для создания микросервисов
- **Spring Cloud** - инструменты для микросервисной архитектуры
  - Spring Cloud Gateway - API Gateway
  - Spring Cloud Eureka - Service Discovery
  - Spring Cloud Config - централизованная конфигурация
  - Spring Cloud OpenFeign - межсервисное взаимодействие
- **PostgreSQL** - база данных
- **Docker** - контейнеризация
- **Maven** - система сборки

## Запуск проекта

Для запуска проекта необходимо выполнить следующие шаги:

1. **Запуск баз данных**
   ```bash
   docker compose up -d
   ```
   Эта команда запустит все необходимые контейнеры с базами данных PostgreSQL для каждого микросервиса.

2. **Запуск инфраструктурных сервисов**
   - Запустить `discovery-server` (Eureka Server)
   - Запустить `config-server` (Spring Cloud Config Server)
   - Запустить `gateway-server` (API Gateway)

3. **Запуск микросервисов**
   - Запустить `stats-server`
   - Запустить `user-service`
   - Запустить `category-service`
   - Запустить `event-service`
   - Запустить `request-service`
   - Запустить `comment-service`
   - Запустить `compilation-service`

После успешного запуска всех сервисов API Gateway будет доступен на порту 8080.

## Тестирование

Для проверки правильности работы функциональности приложения в папке `postman` находятся коллекции тестов Postman:

- `ewm-main-service.json` - коллекция тестов для основного функционала (пользователи, категории, события, запросы, комментарии, подборки)
- `ewm-stat-service.json` - коллекция тестов для сервиса статистики
- `feature.json` - дополнительные тесты функциональности `comment-service`

Для запуска тестов:
1. Импортируйте коллекции в Postman
2. Убедитесь, что все сервисы запущены и доступны
3. Запустите тесты через Postman Runner или выполните запросы вручную

Тесты покрывают основные сценарии использования API и позволяют проверить корректность работы всех эндпоинтов системы.
