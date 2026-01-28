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
   - `collector` - сбор действий пользователей (gRPC → Kafka)
   - `aggregator` - агрегация действий и расчёт похожести событий (Kafka → Kafka)
   - `analyzer` - хранение статистики и выдача рекомендаций (Kafka + БД → gRPC)

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
   - Добавлены отдельные контейнеры баз данных для каждого сервиса и для Kafka

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
- **Рекомендации и статистика взаимодействий** - сбор действий пользователей (просмотры/регистрации/лайки), расчёт рейтингов и выдача рекомендаций

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
- **GET** `/events/{eventId}` - Получение события по ID (требуется заголовок `X-EWM-USER-ID`)
- **GET** `/events/recommendations?max={max}` - Получение рекомендаций событий для пользователя (требуется заголовок `X-EWM-USER-ID`)
- **PUT** `/events/{eventId}/like` - Поставить лайк событию (требуется заголовок `X-EWM-USER-ID`, доступно только зарегистрированным на событие)

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

#### Internal Endpoints (межсервисное взаимодействие)
- **GET** `/internal/event?categoryId={categoryId}` - Проверка наличия событий в категории
- **GET** `/internal/event/all?eventsIds={id1}&eventsIds={id2}...` - Получение набора id событий
- **GET** `/internal/event/exists/{eventId}` - Проверка существования события
- **GET** `/internal/event/{eventId}` - Получение события по id (полная DTO)
- **GET** `/internal/event/all/full?eventsIds={id1}&eventsIds={id2}...` - Получение набора событий (полные DTO)

### Request Service

#### Private Endpoints
- **GET** `/users/{userId}/requests` - Получение запросов текущего пользователя
- **POST** `/users/{userId}/requests` - Создание запроса на участие в событии
- **PATCH** `/users/{userId}/requests/{requestId}/cancel` - Отмена запроса на участие

#### Internal Endpoints (межсервисное взаимодействие)
- **GET** `/internal/request/{userId}/events/{eventId}` - Получение заявок на участие в событии (для владельца события)
- **PUT** `/internal/request/{userId}/events/{eventId}` - Изменение статуса заявок на участие (подтвердить/отклонить)
- **GET** `/internal/request/confirmed/{eventId}` - Количество подтверждённых заявок по событию
- **GET** `/internal/request/confirmed/{eventId}/{userId}` - Проверка регистрации пользователя на событие

### User Service

#### Admin Endpoints
- **GET** `/admin/users` - Получение списка пользователей
- **POST** `/admin/users` - Создание нового пользователя
- **DELETE** `/admin/users/{userId}` - Удаление пользователя

### Новая статистика и рекомендации (collector / aggregator / analyzer)

Новая статистика реализована отдельным контуром из трёх модулей в `stats/` и работает через **gRPC** и **Kafka**:

- **collector**: принимает действия пользователя по gRPC и публикует сообщения в Kafka топик `stats.user-actions.v1`
- **aggregator**: читает `stats.user-actions.v1`, рассчитывает похожесть событий (score) и публикует результаты в `stats.events-similarity.v1`
- **analyzer**: читает оба топика (`stats.user-actions.v1`, `stats.events-similarity.v1`), сохраняет данные в свою БД и отдаёт рекомендации по gRPC

#### gRPC API

- **collector**: `UserActionController/collectUserAction` (действия: VIEW/REGISTER/LIKE)
- **analyzer**: `RecommendationsController`
  - `getRecommendationsForUser`
  - `getSimilarEvents`
  - `getInteractionsCount`

## Технологический стек

- **Java** - язык программирования
- **Spring Boot** - фреймворк для создания микросервисов
- **Spring Cloud** - инструменты для микросервисной архитектуры
  - Spring Cloud Gateway - API Gateway
  - Spring Cloud Eureka - Service Discovery
  - Spring Cloud Config - централизованная конфигурация
  - Spring Cloud OpenFeign - межсервисное взаимодействие
- **gRPC** - бинарный RPC-протокол для взаимодействия сервисов статистики (collector/analyzer)
- **Apache Kafka** - брокер сообщений для поточной обработки действий пользователей и схожести событий
- **PostgreSQL** - база данных
- **Docker** - контейнеризация
- **Maven** - система сборки

## Запуск проекта

Для запуска проекта необходимо выполнить следующие шаги:

1. **Запуск инфраструктуры (Kafka + топики + базы данных)**
   ```bash
   docker compose up -d
   ```
   Эта команда поднимет Kafka, создаст топики `stats.user-actions.v1` и `stats.events-similarity.v1`, а также запустит контейнеры PostgreSQL для микросервисов.

2. **Запуск инфраструктурных сервисов**
   - Запустить `discovery-server` (Eureka Server)
   - Запустить `config-server` (Spring Cloud Config Server)
   - Запустить `gateway-server` (API Gateway)

3. **Запуск микросервисов**
   - Запустить `stats/collector`
   - Запустить `stats/aggregator`
   - Запустить `stats/analyzer`
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

### Проверка новой статистики (collector / aggregator / analyzer)

Проверка новой статистики выполняется через `tester-0.0.1.jar` (далее в командах — `tester.jar`).

- **Проверка коллектора**:

```bash
java -jar tester.jar --tester.execution.mode=COLLECTION --tester.execution.output.file-path=./report.txt
```

- **Проверка коллектора и агрегатора**:

```bash
java -jar tester.jar --tester.execution.mode=AGGREGATION --tester.execution.output.file-path=./report.txt
```

- **Проверка всех трёх модулей (collector + aggregator + analyzer)**:

```bash
java -jar tester.jar --tester.execution.mode=ANALYZE --tester.execution.output.file-path=./report.txt
```
