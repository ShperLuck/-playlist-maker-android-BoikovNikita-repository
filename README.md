
# Playlist Maker

[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white&style=for-the-badge)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=black&style=for-the-badge)](https://developer.android.com/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white&style=for-the-badge)](https://developer.android.com/develop/ui/compose)
[![Room](https://img.shields.io/badge/Room-43A047?logo=android&logoColor=white&style=for-the-badge)](https://developer.android.com/training/data-storage/room)
[![Coil](https://img.shields.io/badge/Coil-00BFA5?logo=coil&logoColor=white&style=for-the-badge)](https://coil-kt.github.io/coil/)
[![minSdk 26](https://img.shields.io/badge/minSdk-26-orange?style=for-the-badge)](https://developer.android.com/guide/topics/manifest/uses-sdk-element#min)
[![target / compileSdk 35](https://img.shields.io/badge/target%20%2F%20compileSdk-35-blue?style=for-the-badge)](https://developer.android.com/about/versions/15)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

Современное Android-приложение для поиска музыки через **iTunes Search API** и удобного управления собственной медиатекой.

Функционал включает создание плейлистов, избранное и уникальную фичу — **склейку плейлистов** без дубликатов.


## Основные возможности

- Поиск треков с debounce и историей запросов (DataStore)  
- Создание, редактирование, удаление плейлистов  
- Добавление и удаление треков в плейлисты  
- Отдельная коллекция «Избранное»  
- **Склейка плейлистов** — интеллектуальный перенос треков с автоматической дедубликацией по `trackId`

## Технологический стек

- **UI** — Jetpack Compose (Material 3), Compose Navigation, State Hoisting  
- **Архитектура** — Clean Architecture + MVVM  
- **DI** — Koin (Service Locator)  
- **Асинхронность** — Coroutines + Flow (StateFlow / SharedFlow)  
- **Сеть** — Retrofit 2 + OkHttp + Gson  
- **База данных** — Room (Entity, DAO, TypeConverters, миграции)  
- **Настройки** — DataStore (Preferences)  
- **Изображения** — Coil  
- **Тесты** — JUnit 5, Kotest, MockK, Turbine

## Структура проекта

```
:app
├─ :core              # утилиты, темы, расширения
├─ :core-ui           # общие composables, компоненты
├─ :navigation        # NavHost, маршруты, аргументы
├─ :feature:search
├─ :feature:media     # медиатека + избранное
├─ :feature:playlist
├─ :feature:player    # минимальная реализация плеера
├─ :data              # Retrofit, Room, репозитории
└─ :domain            # UseCases, модели, интерфейсы репозиториев
```

## Запуск проекта

**Требования**

- Android Studio **Panda 2 | 2025.3.2** (Stable) или новее  
- JDK 17 / 21 (рекомендуется 21)  
- `minSdk 26` · `targetSdk 35` · `compileSdk 35`

**Шаги**

1. Клонирование и переход на финальную ветку

   ```bash
   git clone https://github.com/ShperLuck/playlist-maker-android-BoikovNikita.git
   cd playlist-maker-android-BoikovNikita
   git checkout sprint-12
   ```

2. Откройте проект в Android Studio → дождитесь Gradle sync

3. Выполните чистую пересборку

    - **Build → Clean Project**
    - **Build → Rebuild Project**

4. Запустите приложение (Shift + F10) на эмуляторе / устройстве (API 35+)

**Частые проблемы и решения**

| Проблема                     | Решение                                                            |
|------------------------------|--------------------------------------------------------------------|
| Gradle sync не проходит      | File → Invalidate Caches → Invalidate and Restart                  |
| Неправильный JDK             | Project Structure → SDK Location → Gradle JDK → 21                 |
| Ошибки Room / KSP            | JDK 17+ и включён KSP в gradle.properties                          |
| Нет интернета в эмуляторе    | Настройки эмулятора → Proxy → No proxy                             |

## Ключевые вехи разработки

- **v3.0**  — базовая навигация и заглушки экранов
- **v6.0**  — поиск по API, debounce, настройки
- **v9.0**  — полный переход на Jetpack Compose + BottomSheet
- **v11.0** — Room + DataStore, реальные данные вместо моков
- **v12.0** — избранное, удаление, **склейка плейлистов**

## Реализованные опциональные задания (Sprint 12)

- [x] Удаление плейлиста целиком (с диалогом подтверждения)
- [x] Удаление трека из конкретного плейлиста
- [x] Склейка плейлистов с автоматическим исключением дубликатов

## Автор

**Бойков Никита** 
Институт: СКФУ, 
Группа: ИБАС-с-о-22-1  
[GitHub](https://github.com/ShperLuck) · [nikitaboickov4@gmail.com](mailto:nikitaboickov4@gmail.com)

*Учебный проект 2025–2026 учебного года*
```