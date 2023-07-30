# Отчёт по созданию проекта

## Содержимое проекта
В ходе работы было реализовано приложение по техническому заданию, выполняющее функционал
анализирования картинок в background'е.

Данный проект был реализован с применением таких технологий и библиотек:
- Kotlin Coroutines: использовались для реализации многопоточных операций, также являются более
оптимизированными по сравнению с обычными потоками, так как здесь используется ограниченный пул потоков
- Kotlin Flow: также использовался для многопоточности, хорошая замена RxJava, но при этом работает на корутинах
- Room: использовалась для реализации локальной БД для хранения результатов анализа картинок
- Jetpack Navigation: использовалась для реализации навигации в приложении, отличается достаточным удобством
- Paging 3: использовалась для реализации постраничной загрузки данных для RecyclerView
- TensorFlow: использовалась для анализа картинок с помощью обученной CV модели
- Skeleton: использовалась для реализации placeholder'ов с красивой анимацией

Теперь стоит взглянуть на состав проекта. Проект является многомодульным. На это есть несколько причин:
- Это удобно с точки зрения разработки, так как всё декомпозировано (лежит на своих местах) и 
каждая команда может работать над своими определёнными модулями
- Это при правильном обращении может ускорить скорость сборки
- Это возможность переиспользования различных модулей

Модули проекта:
- app: модуль, являющийся точкой входа в приложение - содержит Application, MainActivity и реализацию
класса для межмодульной навигации
- base: модуль, содержащий основные application-level зависимости, которые будут предоставлены Application
- core-actions: модуль, который содержит Intent Actions
- core-ai: модуль, который содержит класс-helper, необходимый для распознавания объектов на картинке
- core-analyzer: модуль, который содержит класс, инкапсулирующий в себе логику обработки картинки для
распознавания объектов на ней
- core-cache: модуль, содержащий класс для runtime-кэширования картинок
- core-database: модуль, который содержит сущности, DAO (Data Access Object) и Database
- core-design: модуль, содержащий общие UI-элементы и стили
- core-di: модуль, содержащий общие DI-классы
- core-extensions: модуль, содержащий общие функции-расширения
- core-images-provider: модуль, содержащий класс для получения картинок из External Storage с помощью
MediaStore API
- core-navigation: модуль, содержащий общие классы для межмодульной навигации
- core-view-model: модуль, содержащий ViewModelFactory класс, предназначенный для создания ViewModel'ей
посредством Dependency Injection
- feature-all-photos: модуль, содержащий UI для отображения всех картинок устройства
- feature-analyzer: модуль, содержащий Service для обработки картинок устройства в фоне
- feature-image: модуль, содержащий UI для отображения картинки и объектов, присущих ей
- feature-images-list: модуль, содержащий UI для отображения обработанных картинок с объектами
определённого типа

## Принцип работы основных компонентов

### Images Provider

Он работает посредством External Storage Content Provider'а. Данный класс имеет только один инстанс
на протяжении работы приложения. Инстанциируется посредством DI.
Этот класс позволяет получить всю необходимую информацию о фотографиях из внешней памяти устройства.

Возникшие сложности: единственная сложность была в том, чтобы понять как именно работают Content
Provider'ы, и какие именно данные могут потребоваться приложению.

### Permission Request

Для корректной работы приложения пользователю необходио предоставить разрешение на чтение файлов 
из внешней памяти устройства. Это происходит в MainActivity, где уже по результату запроса разрешения
происходит инициализация контента активити. Если же запрос не принимается, то активити, а, соответственно,
и приложение закрываются.

### Photo Cache

Для удобного использования RecyclerView требуется, чтобы картинки загружались как можно быстрее.
В таком случае удобно создать свой кэш фотографий устройства, который будет загружать недавно загруженные
картинки быстрее. В приложении такой кэш реализован посредством LruCache, который работает по стратегии
Least Recently Used. Он удаляет давно неиспользованные объекты в угоду новым.

### View Model Injection

Dagger 2 является очень мощной технологией, которая позволяет создать сложный DI в приложении. Работает
DI в Dagger 2 посредством компонентов и модулей. Хотя Dagger 2 и позволяет достаточно несложно производить
инжектирование объектов, ViewModel'и требуют, в свою очередь, ещё одну важную деталь - сохранение во
ViewModelStore для последующего использования в Activity и Fragment'ах: особенно это важно в ситуациях, когда
происходит изменение конфигурации, например, переворот экрана и последующее пересоздание Activity и Fragment'ов.
Для того, чтобы реализовать правильный DI для ViewModel'ей нужно создать специальный ViewModelFactory класс, 
который будет создавать нужную ViewModel в случае, когда та ещё не была сохранена во ViewModelStore.
Данная фабрика инжектится в нужный компонент и передаётся во ViewModelProvider, который как раз возвращает
нужную ViewModel.

### Images Recycler View

Картинки нужно отображать на экране пользователя списком. Для этого был создан PagingRecyclerViewAdapter, который
был применен для RecyclerView. Но почему Paging? Потому что на устройстве пользователя может быть очень большое количество
картинок, а, следовательно, их нужно загружать по необходимости некоторыми порциями. Для этого был создан
PagingSource, который как раз и отвечает за подгрузку нужных данных в нужное время.

Возникшие сложности: при подключении адаптера и RecyclerView, в целом, на экране были сильные фризы, 
которые сопровождались логами о том, что главный поток выполняет слишком много работы. Выяснилось, что
картинки на тестовом устройстве (на моём телефоне) были слишком большими, а потому их загрузка занимала
какое-то время. Для решения данной задачи было решено уменьшать картинки в размере при загрузке с сохранением
присущего им соотношения. После этого проблема была решена.

### Analyzing Service

Анализ объектов на картинках нужно было воспроизводить в фоне приложения. Для этого было решено использовать
компонент Service, который как раз и нужен для выполнения задач в фоне. Но также нужно было, чтобы
сервис перепроверял наличие новых картинок и анализировал их. Для этого было решено воспользоваться 
механизмом polling'а, который позволил бы выполнять операцию анализа картинок каждый промежуток времени.
Сам механизм был выполнен посредством корутин, при котором для выполнения паузы вызывался метод delay
с определённым таймаутом (в данном случае 30 секунд). Анализ картинок выполнялся по следующему алгоритму:
1. собрать все картинки с помощью Images Provider
2. проверить, проанализирована ли актуальная версия картинки с помощью запроса к БД
3. если да, то перейти к следующей картинке и к п.2 , а если нет, то проанализировать и перейти к п.4
4. добавить данные о картинке и объектах на ней в бд и перейти к следующей картинке и п.2

Возникшие сложности: при добавлении данных о новой обработанной картинке в БД должен отправляться специальный
broadcast, уведомляющий заинтересованные компоненты приложения об этом. Таким компонентом является фрагмент
со списком всех картинок устройства пользователя. При получении broadcast'а данный фрагмент должен обновлять
данные RecyclerView, дабы отобразить актуальное состояние картинки. Но проблема была в том, что не получалось
реализовать правильное обновление adapter'а. Данная проблема, к сожалению, так и не была решена из-за отсутствия
достаточного количества времени, но она может быть решена посредством правильной настройки метода getRefreshKey у
PagingSource. Затем, при получении broadcast'а можно просто вызывать метод refresh у adapter'а.

## Ещё пара слов

В целом, хотелось бы напомнить, что в данном проекте использовалась обученная CV модель, которая позволяет
определять объекты на картинке.

Список объектов, которые могут быть распознаны приложением:
- человек
- кошка
- собака 
- медведь
- машина
- грузовик
- автобус
- птица
- книга

Есть один недочёт у данной модели: он связан с тем, что один и тот же объект она может распознавать несколько
раз, из-за чего появляется некоторый шум - слишком большое количество объектов на одном объекте картинки.
Данная проблема, в моём понимании, может быть решена написанием специального обработчика результатов
анализа картинки, который бы находил такие ситуации, где есть один полноценный объект и много частей данного
объекта, распознанных так же, как этот объект, и удалял бы лишние объекты, избавляясь таким образом от шума.