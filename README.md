<div align="center">

# 🎓 ScheduleBotHTK

### Telegram-бот для удобного просмотра учебного расписания

<img src="docs/screenshots/banner.png" width="800" alt="ScheduleBotHTK Banner">

![Java](https://img.shields.io/badge/Java-26-orange)
![Maven](https://img.shields.io/badge/Maven-Build-blue)
![Telegram](https://img.shields.io/badge/Telegram-Bot_API-2CA5E0)
![Status](https://img.shields.io/badge/Status-In_Development-green)

</div>

---

# 📌 О проекте

Production-ready Telegram-бот для просмотра и управления учебным расписанием.

---

# О проекте

**ScheduleBotHTK** — Telegram-бот, разработанный на Java для удобного получения учебного расписания через Telegram.

Проект построен на модульной архитектуре с разделением конфигурации, обработчиков, сервисов, репозиториев и утилит. Текущая реализация ориентирована на обработку расписания и взаимодействие с Telegram API, при этом архитектура уже подготовлена к дальнейшему масштабированию и production-развёртыванию.

---

# Возможности

* Получение расписания через Telegram
* Система обработки команд
* Парсинг Excel-файлов
* Форматирование и обработка расписания
* Работа со временем занятий и перемен
* Модульная архитектура
* Поддержка логирования
* Конфигурация через environment variables
* Maven build system

---

# Технологический стек

| Технология       | Назначение                                |
|------------------| ----------------------------------------- |
| Java 26+         | Основной язык разработки                  |
| Maven            | Система сборки и управления зависимостями |
| TelegramBots API | Интеграция с Telegram                     |
| Apache POI       | Работа с Excel                            |
| SLF4J / Logging  | Система логирования                       |
| dotenv / env     | Конфигурация через переменные окружения   |

---

# Структура проекта

```text
src/main/java/ru/stavarachi
│
├── app/
│   ├── Application.java        # Bootstrap приложения
│   └── BotApplication.java     # Инициализация Telegram-бота
│
├── config/
│   ├── AppConfig.java          # Основная конфигурация приложения
│   ├── BotConfig.java          # Конфигурация Telegram-бота
│   ├── PathConfig.java         # Конфигурация путей
│   └── ScheduleConfig.java     # Конфигурация расписания
│
├── handler/
│   └── CommandHandler.java     # Обработка Telegram-команд
│
├── model/
│   ├── Pair.java               # Модель пары
│   └── Break.java              # Модель перемены
│
├── repository/
│   └── ExcelRepository.java    # Слой доступа к Excel-данным
│
├── service/
│   ├── ExcelService.java       # Логика обработки Excel
│   ├── ScheduleService.java    # Бизнес-логика расписания
│   │
│   └── telegram/
│       ├── CallService.java
│       ├── InfoService.java
│       └── SixSevenService.java
│
├── util/
│   ├── HtmlUtil.java           # HTML-форматирование
│   ├── MessageUtil.java        # Работа с сообщениями
│   └── TimeUtil.java           # Работа со временем
│
└── Main.java                   # Точка входа
```

---

# Требования

* Java 26
* Maven 3.8+
* Telegram Bot by rubenlagus

---

# Установка

## 1. Клонирование репозитория

```bash
git clone https://github.com/StavaRachl/ScheduleBotHTK.git
cd ScheduleBotHTK
```

---

## 2. Настройка environment variables

Создайте файл `.env` в корне проекта.

Пример:

```env
BOT_TOKEN=your_telegram_bot_token
BOT_USERNAME=your_bot_username
```

---

## 3. Установка зависимостей

```bash
mvn clean install
```

---

# Запуск приложения

## Development mode

```bash
mvn exec:java
```

## Production build

```bash
mvn clean package
java -jar target/ScheduleBotHTK.jar
```

---

# Логирование

Логи приложения сохраняются в директории:

```text
logs/
```

Логирование включает:

* runtime-ошибки;
* исключения приложения;
* ошибки Telegram API.

---

# Конфигурация

## BotConfig

`BotConfig.java` содержит:

* токен бота;
* username бота;
* инициализацию Telegram API.

## ScheduleConfig

`ScheduleConfig.java` отвечает за:

* настройки расписания;
* время пар;
* параметры обработки расписания.

## PathConfig

`PathConfig.java` управляет:

* путями к файлам;
* расположением Excel-файлов;
* локальными ресурсами.

---

# Работа с Excel

Проект использует Excel как источник расписания.

Распределение ответственности:

| Компонент       | Назначение                        |
| --------------- | --------------------------------- |
| ExcelRepository | Чтение Excel-файлов               |
| ExcelService    | Обработка и преобразование данных |
| ScheduleService | Бизнес-логика расписания          |

---

# Telegram-взаимодействие

Telegram-логика вынесена в отдельные сервисы.

| Сервис          | Назначение                           |
| --------------- | ------------------------------------ |
| CallService     | Работа с вызовами                    |
| InfoService     | Информационные ответы                |
| SixSevenService | Специализированная логика расписания |

Такой подход позволяет не смешивать Telegram API и бизнес-логику.

---

# Безопасность

Чувствительные данные не должны храниться в репозитории.

Рекомендуемый `.gitignore`:

```gitignore
.env
logs/
target/
.idea/
*.iml
```

---

# Система сборки

Проект использует Maven для:

* управления зависимостями;
* сборки проекта;
* упаковки jar-файлов;
* подключения плагинов.

Основной конфигурационный файл:

```text
pom.xml
```

---

# Планы развития

Планируемые production-функции:

* Docker deployment
* Расширенное логирование
* Система пользовательских настроек
* Кэширование
* Админ-панель
* Мониторинг и метрики

---

# Лицензия

Проект распространяется в учебных и ознакомительных целях.

---

# Автор

Разработчик: StavaRachl.
