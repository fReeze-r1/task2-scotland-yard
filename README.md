# task2-scotland-yard

Реализация игры Scotland Yard на Java.

## Структура проекта

- `backend/` - движок игры с логикой
- `console-demo/` - консольное приложение для демонстрации

## Требования

- Java 11 или выше
- Maven 3.6 или выше

## Сборка проекта

### Backend

Сначала необходимо собрать и установить backend модуль в локальный Maven репозиторий:

```bash
cd backend
mvn clean install
```

### Консольное приложение

После установки backend, можно собрать консольное приложение:

```bash
cd console-demo
mvn clean package
```

## Запуск

### Запуск тестов

```bash
cd backend
mvn test
```

### Запуск консольного приложения

**Важно:** Перед запуском console-demo необходимо установить backend модуль (см. раздел "Сборка проекта").

```bash
cd console-demo
mvn exec:java -Dexec.mainClass="ru.cs.vsu.oop.task2.lopatin_n.ConsoleDemo"
```

Или после сборки:

```bash
cd console-demo
java -jar target/scotland-yard-console-demo-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Использование

1. При запуске консольного приложения введите количество детективов (1-5)
2. Введите имена детективов
3. Игра начнется автоматически
4. Следуйте инструкциям на экране для совершения ходов
