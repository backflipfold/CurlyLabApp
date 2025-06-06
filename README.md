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

## Схема масштабирования сервиса при росте нагрузĸи в 10 раз

# Кодирование и отладĸа
* Реализованы регистрация и авторизация с валидацией данных
* Используется шифрование чувствительных данных
* Интеграция с внешним API для хранения фотографий
* Git-стратегия с использованием feature-веток 
* Каждый участник команды сделал коммиты в репозиторий
  
# Unit тестирование

# Интеграционное тестирование

# Сборĸа
* Сборка Android APK
```
docker-compose run --rm android-builder
```
* Запуск Unit-тестов
```
docker-compose run --rm test-runner
```



