# Приложение для облегчения ухода за кудрявыми волосами "CurlyLab"
<img src="https://github.com/laylawinxp/CurlyLabApp/blob/main/CurlyLab/app/src/main/res/drawable/logo_curly_lab.png" width="300" height="300">

# Содержание

- [Авторы проекта](#авторы-проекта)  
- [Определение проблемы](#определение-проблемы)  
- [Выработĸа требований](#выработка-требований)
- [Разработĸа архитеĸтуры и детальное проеĸтирование](#разработĸа-архитеĸтуры-и-детальное-проеĸтирование)  
- [Кодирование и отладĸа](#кодирование-и-отладĸа)  
- [Unit тестирование](#unit-тестирование)
- [Интеграционное тестирование](#интеграционное-тестирование)
- [Сборĸа](#сборĸа)

# Авторы проекта

* _Авдышева Полина Георгиевна_, 5130904/20101
* _Домбровская Виктория Игоревна_, 5130904/20101
* _Зиатдинова Алина Рафаилевна_, 5130904/20101

# Определение проблемы
Целевая аудитория — кудрявые люди. Уход за кудрявыми волосами требует особых усилий. Многие из людей, имеющих этот тип волос, переходя на **кудрявый метод**, сталкиваются с непониманием, с чего начать, какие методы и технологии использовать, чтобы укротить непослушные волосы. Приложение содержит всю необходимую информацию для легкого старта, а также предоставляет возможность делиться своим опытом и перенимать что-то новое у других. 

# Выработка требований
![image](https://github.com/user-attachments/assets/2586cd3d-8f03-4fef-bf99-c658f87665f6)
![image](https://github.com/user-attachments/assets/bd9462df-0a71-48b6-bf0f-146fd6afc519)
![image](https://github.com/user-attachments/assets/312e94a4-ee05-4cb9-a898-016f425fe4d2)

# Разработĸа архитеĸтуры и детальное проеĸтирование
## Характер нагрузки на сервис
* Соотношение R/W нагрузки: 80% чтения (авторизация, просмотр постов, просмотр продуктов, просмотр отзывов на продукты, просмотр профилей), 20% записи (регистрация, написание постов, подписка/отписка, написание отзывов, добавление продуктов в избранное, тестирование типа волос).
* Объемы трафика: ~1 000 000 запросов в месяц.
* Объемы дисковой системы: ~120 ГБ для хранения данных пользователей, продуктов, отзывов, постов.
## C4 Model
### Context 
![image](https://github.com/user-attachments/assets/7acded04-2823-4006-8345-bbce17d9796a)
### Container 
![image](https://github.com/user-attachments/assets/2e950f17-73da-4e79-ad69-e5677cc40147)
## Контракты API
## Authentication

### Register

- **URL**: `/auth/register`

- **Method**: `POST`

### Login

- **URL**: `/auth/login`

- **Method**: `POST`

### Refresh Tokens

- **URL**: `/auth/refresh`

- **Method**: `POST`

### Logout

- **URL**: `/auth/logout`

- **Method**: `POST`

## Users

### Create User

- **URL**: `/users`

- **Method**: `POST`

### Get All Users

- **URL**: `/users`

- **Method**: `GET`

### Get User by ID

- **URL**: `/users/{id}`

- **Method**: `GET`

### Update User

- **URL**: `/users/{id}`

- **Method**: `PUT`

### Upload User Image

- **URL**: `/users/{id}/uploadImage`

- **Method**: `POST`

- **Content-Type**: `multipart/form-data`

- **Body**: `image` (file part)

### Delete User

- **URL**: `/users/{id}`

- **Method**: `DELETE`

## Blog Records

### Get Posts by Subscribed Users

- **URL**: `/blog_records/subscriptions/{id}`

- **Method**: `GET`

### Get Recommended Posts for User

- **URL**: `/blog_records/recommended/{id}`

- **Method**: `GET`

### Get Recommended Posts

- **URL**: `/blog_records/recommended`

- **Method**: `GET`

### Search Posts by Keyword

- **URL**: `/blog_records/find/{word}`

- **Method**: `GET`

### Get Posts by User

- **URL**: `/blog_records/my/{id}`

- **Method**: `GET`

### Edit Post

- **URL**: `/blog_records/{id}`

- **Method**: `PUT`

### Delete Post

- **URL**: `/blog_records/{id}`

- **Method**: `DELETE`

### Add Post

- **URL**: `/blog_records`

- **Method**: `POST`

## Blog Subscribers

### Get Subscription Count for User

- **URL**: `/blog_subscribers/subscriptions/{id}`

- **Method**: `GET`

### Get Subscriber Count for User

- **URL**: `/blog_subscribers/subscribers/{id}`

- **Method**: `GET`

### Subscribe

- **URL**: `/blog_subscribers`

- **Method**: `POST`

### Unsubscribe

- **URL**: `/blog_subscribers/{id}`

- **Method**: `DELETE`

### Get Subscription ID

- **URL**: `/blog_subscribers/get`

- **Method**: `POST`

## Products

### Get All Products

- **URL**: `/products`

- **Method**: `GET`

### Get Product by ID

- **URL**: `/products/{productId}`

- **Method**: `GET`

### Get User Favorites

- **URL**: `/products/favorites/{userId}`

- **Method**: `GET`

### Check if Product is Favorite

- **URL**: `/products/{productId}/is-favorite/{userId}`

- **Method**: `GET`

### Add to Favorites

- **URL**: `/products/favorites`

- **Method**: `POST`

### Remove from Favorites

- **URL**: `/products/favorites`

- **Method**: `DELETE`

### Get Product Reviews

- **URL**: `/products/{id}/reviews`

- **Method**: `GET`

### Add Review

- **URL**: `/products/rate`

- **Method**: `POST`

### Update Review

- **URL**: `/products/{productId}/reviews/{userId}/update`

- **Method**: `POST`

### Delete Review

- **URL**: `/products/{productId}/reviews/{userId}`

- **Method**: `DELETE`

## Hair Types

### Get All Hair Types

- **URL**: `/hairtypes`

- **Method**: `GET`

### Get Hair Type by ID

- **URL**: `/hairtypes/{id}`

- **Method**: `GET`

### Update Hair Type for User

- **URL**: `/hairtypes/{userId}`

- **Method**: `PUT`

### Delete Hair Type for User

- **URL**: `/hairtypes/{userId}`

- **Method**: `DELETE`
## Схема базы данных и обоснование
<div id="db" align="center">
  <img src="https://github.com/user-attachments/assets/ba2fd1b2-8b08-4dff-b1df-338c068c652b" width="400" height="400">
  <img src="https://github.com/user-attachments/assets/aac54a65-2cef-49cc-966e-cd02076299cd" width="400" height="400">
  <img src="https://github.com/user-attachments/assets/1318caaf-d5b3-43e2-ae05-8af069b5235e" width="600" height="400">
</div>

Представленная схема базы данных разработана с целью эффективного хранения, управления и обработки информации о пользователях, продуктах, отзывах, предпочтениях и взаимодействии между пользователями. Архитектура базы данных построена с учётом принципов нормализации, безопасности и масштабируемости, что обеспечивает её надёжность и готовность к развитию.

В центре модели находится таблица users, содержащая основную информацию о каждом пользователе. Чувствительные данные, такие как email и пароль, вынесены в отдельную таблицу user_auth, что соответствует лучшим практикам обеспечения безопасности. Для поддержки механизма аутентификации через refresh-токены используется таблица user_refresh_tokens, в которой хранятся токены доступа, срок их действия и статус.

Для персонализации приложения предусмотрена таблица user_hairtypes, где фиксируются индивидуальные параметры пользователей, такие как пористость и толщина волос. Эти данные могут использоваться для генерации персонализированных рекомендаций по продуктам.

Продукты представлены в таблице products, где содержатся их названия, описания, изображения и теги. Пользователи могут оставлять свои отзывы через таблицу reviews, включающую рейтинги, комментарии и информацию о дате создания отзыва. Также реализована таблица favorites, отражающая взаимосвязь между пользователями и продуктами, которые они добавили в избранное.

Дополнительно реализован блоговый функционал. Таблица blog_records представляет собой записи блога, создаваемые пользователями. Каждая запись содержит текст контента и набор тегов. Эта таблица связана с таблицей users, что позволяет отслеживать авторство публикаций. Таблица blog_subscribers реализует подписочную модель между пользователями, отражая подписки одного пользователя на другого. Это позволяет формировать социальную составляющую платформы, развивать сообщество и персонализировать ленту контента.

Таким образом, структура базы данных обеспечивает:

* надёжное хранение и безопасную работу с пользовательскими данными;
* поддержку гибкой модели отношений между сущностями;
* возможность персонализации интерфейса и рекомендаций;
* реализацию функционала пользовательского контента (блогов);
* развитие социального взаимодействия через подписки.

## Схема масштабирования сервиса при росте нагрузĸи в 10 раз

# Кодирование и отладĸа
* Реализованы регистрация и авторизация с валидацией данных
* Используется шифрование чувствительных данных
* Интеграция с внешним API для хранения фотографий
* Git-стратегия с использованием feature-веток 
* Каждый участник команды сделал коммиты в репозиторий
  
# Unit тестирование
Общая информация
В проекте реализованы модульные тесты (unit tests), направленные на проверку отдельных компонентов системы без необходимости разворачивания всей инфраструктуры (например, БД или внешних API). Это позволяет оперативно выявлять ошибки в логике и повышать надёжность кода.

Примеры охваченных компонентов
1. TokenService
Компонент отвечает за создание и валидацию JWT-токенов.
Тесты проверяют:

*Корректность генерации токена.
*Валидность подписанного токена.
*Отказ при передаче некорректного токена.

2. GoogleDriveService
Класс реализует загрузку изображений в Google Drive.
Тесты покрывают:

*Поведение при успешной загрузке.
*Формирование корректной ссылки на файл.
*Назначение прав доступа через mocked-интерфейсы Google API.

3. BlogSubscribersRepository
Работа с подписками в блоге.
Тесты проверяют:

*Успешное добавление подписки.
*Удаление записи.
*Поиск подписок по authorId и subscriberId.

4. BlogRecordRepository
Тестируются методы работы с записями блога: добавление, редактирование, удаление.
Особое внимание уделено методам с фильтрацией по:

*Авторам.
*Подпискам.
*Тегам и содержимому текста.

5. ProductDTO и ReviewDTO (модели)
Проверена корректность инициализации объектов с использованием UUID, списков и строк.

Используемые технологии

JUnit 4 – основной фреймворк для модульного тестирования.
Mockito Kotlin – библиотека для создания mock-объектов и имитации поведения зависимостей.
H2 Database – встраиваемая БД для юнит-тестов компонентов, использующих ORM (Exposed).
Exposed ORM – взаимодействие с базой данных при тестировании репозиториев.

# Интеграционное тестирование
Реализован сценарий:
* Пользователь вводит неверный пароль -- переход на главный экран не происходит.
* Пользователь вводит верный пароль -- переход на главный экран происходит.
* Проверка, что на экране профиля отображается имя пользователя.

# Сборĸа
* Сборка Android APK
```
docker-compose run --rm android-builder
```
* Запуск Unit-тестов
```
docker-compose run --rm test-runner
```



