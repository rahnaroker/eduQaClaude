# Шпаргалка

Краткие выжимки по пройденным темам. Формат: определение → нюансы → ловушки.

---

## Java Core

### OOP

**Overloading vs Overriding**
- Overloading — тот же класс, разные сигнатуры, resolved в compile-time (static dispatch)
- Overriding — дочерний класс, та же сигнатура, resolved в runtime (dynamic dispatch через vtable)
- `@Override` не обязателен, но ловит ошибки компиляции
- Ловушка: overloading с `null` — компилятор выбирает наиболее специфичный тип

**Static dispatch vs Dynamic dispatch**
- Static dispatch: компилятор вписывает конкретный метод в байткод (`invokestatic`) — overloading, static методы
- Dynamic dispatch: JVM смотрит на реальный тип объекта в рантайме через vtable (`invokevirtual`) — overriding
- Static метод в дочернем классе — не overriding, а **method hiding**: вызов по типу переменной, не объекта

**Abstract class vs Interface**

| | Abstract class | Interface |
|---|---|---|
| Поля с состоянием | ✅ | ❌ (только `static final`) |
| Конструктор | ✅ | ❌ |
| Множественное наследование | ❌ | ✅ |
| `default` методы | ✅ | ✅ (Java 8+) |

- Abstract class — когда есть общее состояние и частичная реализация (пример: `BaseTest`)
- Interface — когда нужен чистый контракт без привязки к иерархии
- Ловушка: abstract class может не содержать ни одного abstract метода — это валидно

**equals / hashCode**
- Контракт: `equals == true` → `hashCode` обязан совпадать (обратное необязательно)
- Без `hashCode` → HashMap кладёт в разные бакеты → `get()` вернёт `null`
- HashMap: `hash = hashCode ^ (hashCode >>> 16)` → `index = hash & (capacity-1)` → equals в бакете
- Capacity 16, load factor 0.75, resize × 2; бакет → дерево при ≥ 8 элементах

**== vs equals**
- `==` для объектов — сравнение ссылок; для примитивов — значений
- `equals()` — сравнение содержимого (если переопределён)
- Integer cache: -128..127 → `Integer.valueOf()` возвращает один объект → `==` даёт `true`; вне диапазона — `false`
- `new Integer()` всегда новый объект, кэш обходится (deprecated с Java 9)
- Autoboxing + `==`: `Integer x = 5; x == 5` — Integer анбоксится, сравниваются примитивы → `true`

**static**
- `static` поля/методы/блоки — принадлежат классу, не экземпляру
- Порядок инициализации: static поля+блоки (при загрузке класса) → instance поля+блоки → конструктор
- Static nested class — нет доступа к instance-членам outer → нет утечки памяти
- Inner (нестатический) class — держит неявную ссылку на outer (`Outer.this`) → риск утечки памяти

**instanceof / ClassCastException**
- `instanceof` — проверка типа объекта, используется перед кастом
- `ClassCastException` — runtime, только объектные типы; `float→int` — не исключение, это narrowing
- Pattern matching (Java 16+): `if (obj instanceof String s)` — каст + объявление переменной в одну строку

**final**
- `final class` — нельзя наследоваться; `final method` — нельзя переопределить
- `final field` — нельзя переприсвоить ссылку, но состояние объекта менять можно
- Ловушка: `final List` — `add/remove` работают, `=` — нет
- Инициализировать до конца конструктора обязательно

---

### Collections

**Map**
- Map не наследует Collection
- `HashMap` — O(1), без порядка, 1 null-ключ
- `LinkedHashMap` — insertion order, O(1)
- `TreeMap` — сортировка по ключу, O(log n), null-ключи запрещены, нужен Comparable/Comparator
- `ConcurrentHashMap` — многопоточка (вместо устаревшего Hashtable)

**List**
- `ArrayList`: get(i) O(1), add в конец O(1) амортизированно, вставка в середину O(n); компактная память
- `LinkedList`: doubly-linked list; get(i) O(n); add в head/tail O(1); реализует Deque
- На практике ArrayList быстрее LinkedList даже при вставках — cache locality
- LinkedList как очередь/стек когда нужны операции с обоих концов

---

## Алгоритмы (Java)

Перенесено в отдельный файл: [ALGORITHMS.md](ALGORITHMS.md)

---

## Теория тестирования

**Тест-пирамида**
```
      [  E2E / UI       ]   — медленно, дорого, flaky, мало
    [  Integration        ]  — средний слой, БД, сервисы
  [  Unit                  ] — быстро, изолировано, много
```
- Антипаттерн "ice cream cone" — мало юнитов, много UI. Признак legacy-проекта.

**Test Doubles**

| Double  | Реальная логика | Проверяет вызовы | Возвращает данные |
|---------|----------------|-----------------|------------------|
| Dummy   | нет            | нет             | нет              |
| Stub    | нет            | нет             | да (жёстко)      |
| Mock    | нет            | **да**          | можно настроить  |
| Spy     | **да**         | **да**          | реальные         |
| Fake    | да (упрощённая)| нет             | реальные         |

- Stub — `when(repo.findById(1)).thenReturn(user)`; Mock — `verify(emailService).send(...)`
- Spy нужен редко — его появление часто сигнал нарушения SRP
- В Mockito mock умеет и stubbing и verification — отсюда путаница в терминах

**Техники тест-дизайна**
- **Классы эквивалентности** — достаточно одного представителя от каждого класса (валид/невалид)
- **Граничные значения** — для возраста 1–120: проверяем 0, 1, 2, 119, 120, 121
- **Таблица решений** — для сложной логики с несколькими условиями. Условия × действия, каждый столбец = тест-кейс
- **Pairwise** — большинство дефектов вызваны взаимодействием двух параметров. Инструменты: PICT, AllPairs
- **State transition** — для объектов с жизненным циклом (заказ: создан → оплачен → отправлен)
- **Error guessing** — интуитивное, на основе опыта

**Критерии приемки (AC)**
- Кто: PO — бизнес-смысл, QA — граничные/негативные кейсы, dev — технические ограничения («три амиго»)
- Когда: до начала разработки, на Refinement
- AC vs DoD: DoD — общий для всех историй; AC — специфичны для конкретной истории
- При невыполнении: story не закрывается, не идёт в релиз

---

## Backend-тестирование

**Идемпотентность HTTP**

| Метод | Идемпотентен | Безопасный |
|-------|-------------|-----------|
| GET, HEAD, OPTIONS | да | да |
| PUT, DELETE | да | нет |
| POST | нет | нет |
| PATCH | нет (по спеку) | нет |

- DELETE: коды разные (1-й → 200/204, повторный → 404), но эффект одинаков — норма
- PATCH не идемпотентен: `{"score": "+10"}` каждый раз меняет состояние

**POST vs PUT vs PATCH (частый вопрос Ozon)**

| | POST | PUT | PATCH |
|---|---|---|---|
| Семантика | создать | заменить целиком | обновить частично |
| Идемпотентность | ❌ нет | ✅ да | ⚠️ не гарантирована (зависит от патча) |
| URI | коллекция `POST /users` | конкретный `PUT /users/1` | конкретный `PATCH /users/1` |
| Тело | поля нового объекта | **весь** объект (опущенное поле → затрётся) | **только** меняемые поля |
| ID ресурса | назначает сервер | задаёт клиент (в URI) | задаёт клиент (в URI) |

- **Идемпотентность ≠ safe.** Safe (GET/HEAD) — вообще не меняет состояние. Идемпотентный — N повторов оставляют сервер в том же состоянии, что и 1 вызов (менять состояние может; PUT идемпотентен, но меняет).
- **PATCH идемпотентен или нет — зависит от операции:** `{name:"Анна"}` (set) — да; `{op:"increment"}` (прибавить) — нет. PUT идемпотентен **всегда** по спеку, PATCH — не обязан.
- **PUT vs PATCH — главное практическое:** PUT шлёт полное представление, опущенное поле обнуляется; PATCH трогает только переданные поля.
- **PUT может создавать** (upsert по известному URI). Отличие от POST: POST → создание в коллекции, ID даёт сервер; PUT → по адресу, заданному клиентом.
- **Миф «тело POST зашифровано» — нет.** Шифрует только HTTPS/TLS (и тело POST, и query GET одинаково). Плюс POST реальный: тело не в URL → не светится в логах/истории/Referer. Причина — «не в URL», не «зашифровано».

**Структура HTTP-запроса и ответа (вопрос Ozon)**

Запрос — 3 части + пустая строка-разделитель:
```
POST /user?active=true HTTP/1.1   ← стартовая строка: метод + путь(с query) + версия
Host: test.ru                     ← заголовки (пары ключ:значение)
Content-Type: application/json
                                  ← пустая строка (разделитель)
{ "name": "Анна" }                ← тело (опц.; у GET обычно нет)
```
Ответ — то же, но первая строка статусная: `HTTP/1.1 200 OK` (версия + код + reason-phrase) → заголовки → пустая строка → тело.

- **Параметры — это НЕ заголовки.** path-параметр в пути (`/user/5`), query-параметр в URL после `?` (`/user?id=5&sort=name`), заголовки — отдельная секция. Не путать.
- `scheme://host` в стартовую строку не идёт: host → заголовок `Host`, схема → уровень TLS. В стартовой строке только путь+query.
- Метод + путь + версия — всё в одной стартовой строке.

**Статус-коды**

| Класс | Смысл | Знать |
|---|---|---|
| 1xx | информация | 100 Continue, 101 Switching Protocols (WebSocket) |
| 2xx | успех | 200 OK, 201 Created, 204 No Content (успех без тела — DELETE/PUT) |
| 3xx | редирект | 301 (навсегда), 302 (временно), 304 Not Modified (кэш актуален) |
| 4xx | ошибка клиента | 400 Bad Request, 401, 403, 404, 405 Method Not Allowed, 409 Conflict, 422 Unprocessable Entity, 429 Too Many Requests |
| 5xx | ошибка сервера | 500, 502 Bad Gateway, 503 Service Unavailable, 504 Gateway Timeout |

- **400 vs 422:** 400 — сломан синтаксис/формат; 422 — формат ок, не прошла бизнес-валидация.
- QA-практика: 204 → проверить, что тела нет; 429 → rate limiting; 304 → кэш.

**GET — свойства (вопрос Ozon)**
- **Safe** (только чтение, не меняет состояние) + **идемпотентен** + **кэшируется**.
- Идемпотентность ≠ «байт-в-байт одинаковый ответ»: это «запрос не меняет состояние сервера». GET к меняющемуся ресурсу может вернуть разные данные — всё равно safe/идемпотентен.
- Данные — в URL (path/query). Тело формально не запрещено, но без семантики → прокси/серверы игнорируют, полагаться нельзя.
- Чувствительные данные в GET не шлют: URL оседает в логах, истории, `Referer`, кэшах (причина — «URL запоминается везде», НЕ «GET не шифрует»; по HTTPS URL тоже шифруется в транзите).
- **Read-heavy ручки → GET:** safe→кэш (CDN/proxy перехватывают повторы, разгружают бэкенд) + безопасный ретрай при сбое сети (POST ретраить опасно — дубликаты). POST по умолчанию не кэшируется. Злонамеренный флуд кэш не лечит — нужен rate limiting (429).

**POST для чтения (поиск/фильтр)** — когда GET не тянет: длинный URL (`414 URI Too Long`, лимит ~2048), сложные вложенные фильтры удобнее JSON-телом, чувствительные критерии не в URL. Цена: теряются идемпотентность и кэш → применять точечно (`POST /search`).

**Authentication vs Authorization**
- Authentication — кто ты? → 401 если не опознан (название 401 Unauthorized обманчиво — значит «не аутентифицирован»)
- Authorization — что тебе можно? → 403 если опознан, но нет доступа

**Content-Type vs Accept**
- `Content-Type` — формат тела запроса который я отправляю
- `Accept` — формат ответа который я хочу получить. Сервер не может вернуть — 406

**Negative testing — чеклист**
- Пустые/невалидные поля → 400/422
- Несуществующий ресурс → 404
- Без токена → 401, чужой ресурс → 403
- Неверный метод → 405, дубликат → 409
- Граничные значения, SQL-инъекция в поле

**REST Assured — структура**
```java
given()  // baseUri, headers, contentType, body, auth
.when()  // .get/post/put/delete("/endpoint")
.then()  // statusCode(), body("field", equalTo(...)), extract()
```

**Contract testing (Pact)**
Consumer пишет тест → генерирует pact-файл → Pact Broker → Producer верифицирует в CI.
Проблема: у каждого сервиса тесты зелёные, но интеграция сломана из-за изменения контракта.

**POJO / Entity / DTO**
```
JSON → [Jackson] → RequestDTO → [Service] → Entity → [Hibernate] → БД
БД   → [Hibernate] → Entity → [Service] → ResponseDTO → [Jackson] → JSON
```
- Entity сама JSON не разбирает и в таблицу не пишет
- `LazyInitializationException`: конвертировать Entity в DTO внутри `@Transactional`
- JPA — спецификация; Hibernate — реализация JPA (как JDBC и PostgreSQL Driver)

**Тестирование пагинации**
- Обязательно: базовый позитив, последняя страница, за пределами (`page=999` → `[]`), невалидные параметры → 400
- Ловушка: без явного `sort` порядок недетерминирован → элемент может попасть на две страницы
- `size=10000` — сервер должен ограничивать максимум, иначе DoS

---

## Browser-тестирование

**Page Object Model**
- Под каждую страницу — отдельный класс. Локатор в одном месте.
- В PO: локаторы, методы взаимодействия, возврат следующей страницы
- НЕ в PO: assertions, тестовые данные, бизнес-логика

**Selenide vs Selenium**
- Selenium — W3C-протокол, развивается медленно
- Selenide — обёртка, убирает: явные ожидания, `StaleElementReferenceException`, управление драйвером
- `$("#submit").shouldBe(enabled).click()` вместо `WebDriverWait` + `ExpectedConditions`

**Локаторы — приоритет**
1. `data-testid` — явно для тестов, разработчик не тронет случайно
2. `id` — хорош если статический
3. `css` — быстрый, только вниз по DOM
4. `xpath` — когда нужно вверх по дереву или текстовый поиск

**Flaky тесты — причины и борьба**
- Нестабильные локаторы → `data-testid`
- Зависимость тестов → каждый тест создаёт свои данные
- Внешние зависимости → Awaitility с таймаутом
- `@RetryingTest` — временная мера, не решение

---

## БД (SQL)

**JOIN'ы**

| JOIN | Результат |
|------|-----------|
| INNER JOIN | только совпадающие строки с обеих сторон |
| LEFT JOIN | все из левой + совпадения из правой (NULL если нет) |
| FULL OUTER JOIN | все из обеих таблиц, NULL там где нет совпадения |
| CROSS JOIN | каждая строка левой × каждую строку правой, N×M строк |

- `INNER` — обязательная связь; `LEFT` — правая опциональна
- `FULL OUTER` — аудит расхождений; `CROSS JOIN` случайно в коде — обычно баг

**GROUP BY / WHERE / HAVING**
- `WHERE` — фильтрует строки **до** группировки, агрегатные функции нельзя
- `HAVING` — фильтрует группы **после** группировки, агрегатные функции можно
- `HAVING` вместо `WHERE` — работает, но медленнее

```sql
SELECT department, COUNT(*) as cnt
FROM employees
WHERE salary > 50000       -- до группировки
GROUP BY department
HAVING COUNT(*) > 5        -- после группировки
```

**NULL**
- `NULL = NULL` → не `true`, а `UNKNOWN`. Операторы `=`, `!=`, `<>` с NULL → `UNKNOWN`

```sql
WHERE email = NULL      -- всегда 0 строк → нужно IS NULL
NOT IN (1, 2, NULL)     -- вернёт 0 строк
AVG([1, 2, NULL, NULL]) -- вернёт 1.5 (NULL игнорируется агрегатами)
COUNT(*)                -- считает строки включая NULL
COUNT(field)            -- только не-NULL
```
- `COALESCE(field, default)` — заменяет NULL на дефолт
- `NOT EXISTS` вместо `NOT IN` когда подзапрос может вернуть NULL

**Индексы**
- Структура: B-tree, хранит отсортированные значения + указатели на строки. Поиск O(log n).
- Главный минус: каждый INSERT/UPDATE/DELETE обновляет все индексы таблицы
- Кардинальность: индекс на `is_deleted` (2 значения) бесполезен — оптимизатор сделает full scan

**PRIMARY KEY / FOREIGN KEY**
- PK: автоматически создаёт индекс. UUID v4 — фрагментирует B-tree; UUID v7 — монотонный, без фрагментации
- FK: referential integrity. `ON DELETE CASCADE` — удаляет дочерние; `ON DELETE SET NULL` — обнуляет FK
- Ловушка: FK **не** создаёт индекс автоматически в PostgreSQL → JOIN по FK без индекса → seq scan

**Транзакции и ACID**

| Буква | Свойство | Суть |
|-------|----------|------|
| A | Atomicity | Всё или ничего |
| C | Consistency | После транзакции все constraints выполняются |
| I | Isolation | Параллельные транзакции не видят промежуточные состояния |
| D | Durability | После коммита данные не потеряются (WAL на диске) |

**Уровни изоляции:**

| Уровень | Dirty Read | Non-repeatable Read | Phantom Read |
|---------|-----------|---------------------|--------------|
| READ UNCOMMITTED | да | да | да |
| READ COMMITTED | нет | да | да |
| REPEATABLE READ | нет | нет | да |
| SERIALIZABLE | нет | нет | нет |

PostgreSQL по умолчанию — READ COMMITTED.

**EXPLAIN / EXPLAIN ANALYZE**
- `EXPLAIN` — план оптимизатора, запрос не выполняется
- `EXPLAIN ANALYZE` — выполняет реально + фактическое время

```sql
BEGIN;
EXPLAIN ANALYZE DELETE FROM orders WHERE status = 'old';
ROLLBACK;
```

- `Seq Scan` — full scan, индекс не используется → плохо на больших таблицах
- `Index Scan` — индекс используется → хорошо
- `Bitmap Index Scan` — промежуточный вариант, лучше Seq Scan

**Subquery**
```sql
-- коррелированный (медленно — выполняется для каждой строки)
SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = u.id) FROM users u;

-- в WHERE
SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE status = 'paid');

-- в FROM (derived table)
SELECT * FROM (SELECT user_id, COUNT(*) as cnt FROM orders GROUP BY user_id) t WHERE t.cnt > 3;
```
- `JOIN` обычно быстрее — оптимизатор лучше строит план
- `IN (subquery)` на больших данных → медленнее чем `EXISTS` или `JOIN`

**Сериализация**
- Объект → байты/строка (сериализация). Байты/строка → объект (десериализация).
- JSON (Jackson): `mapper.writeValueAsString(obj)` / `mapper.readValue(json, Cls.class)`
- Java Serializable: без `serialVersionUID` при изменении класса → `InvalidClassException`
- Kafka: продюсер сериализует → консьюмер десериализует. Несовпадение → corrupted данные
- SERIALIZABLE в SQL — совпадение названия, не про сериализацию данных

---

**UNION / UNION ALL**
- Оба объединяют результаты двух запросов с одинаковым набором и типами колонок
- `UNION` — убирает дубликаты (неявный DISTINCT). Дороже — нужна сортировка/хэширование.
- `UNION ALL` — оставляет все строки включая дубликаты. Быстрее — просто склеивает.
- Типичная ошибка: писать `UNION` везде по привычке, хотя дублей нет — лишняя работа для БД.

```sql
-- A: [1,2,3], B: [2,3,4]
UNION     → 1, 2, 3, 4
UNION ALL → 1, 2, 3, 2, 3, 4
```

**DDL / DML / DCL**

| | Расшифровка | Команды |
|---|---|---|
| DDL | Data **Definition** Language | `CREATE`, `ALTER`, `DROP`, `TRUNCATE` |
| DML | Data **Manipulation** Language | `SELECT`, `INSERT`, `UPDATE`, `DELETE` |
| DCL | Data **Control** Language | `GRANT`, `REVOKE` |

- DDL — структура БД (схема); DML — данные внутри таблиц; DCL — права доступа
- Ловушка: `TRUNCATE` — DDL, не DML. В MySQL не откатывается транзакцией; в PostgreSQL — откатывается.

**CTE (Common Table Expression)**
- Именованный подзапрос, объявляется через `WITH` перед основным запросом
- Функционально = subquery, но читабельнее — сложный запрос разбивается на именованные шаги
- Можно объявить несколько CTE через запятую

```sql
WITH active_users AS (
    SELECT user_id, COUNT(*) as cnt
    FROM orders
    GROUP BY user_id
    HAVING COUNT(*) > 3
)
SELECT u.name FROM users u
JOIN active_users a ON u.id = a.user_id;
```

- `WITH RECURSIVE` — для иерархических данных (дерево категорий, оргструктура)
- Оптимизатор в большинстве случаев строит тот же план что и для subquery

**Window Functions**
- Агрегатные вычисления над набором строк без GROUP BY — строки не схлопываются
- Синтаксис: `функция() OVER (PARTITION BY поле ORDER BY поле)`
- `PARTITION BY` — аналог GROUP BY без схлопывания. Без него окно = вся таблица.

```sql
ROW_NUMBER() OVER (PARTITION BY department ORDER BY salary DESC)  -- номер строки в группе
RANK()        -- ранг с пропусками (1,2,2,4)
DENSE_RANK()  -- ранг без пропусков (1,2,2,3)
LAG(salary)  OVER (ORDER BY hire_date)   -- значение предыдущей строки
LEAD(salary) OVER (ORDER BY hire_date)   -- следующей строки
```

**Классический вопрос:** топ-1 сотрудник по зарплате в каждом отделе:
```sql
WITH ranked AS (
    SELECT *, ROW_NUMBER() OVER (PARTITION BY department ORDER BY salary DESC) as rn
    FROM employees
)
SELECT * FROM ranked WHERE rn = 1;
```

---

## Git

**merge vs rebase**

```
Старт:
main:    A - B - C
feature: A - B - D - E
```

`git merge main → feature` — создаёт merge-commit M, история нелинейная, D и E остаются с теми же SHA:
```
feature: A - B - D - E - M
```

`git rebase main → feature` — пересоздаёт D и E поверх C, новые SHA, история линейная:
```
feature: A - B - C - D' - E'
```

- `merge` — публичные ветки (main, develop)
- `rebase` — локальные feature-ветки перед PR
- Ловушка: rebase переписывает SHA → нельзя делать на уже запушенных коммитах

---

**reset vs revert**

`git reset` — двигает HEAD назад, переписывает историю:
```bash
git reset --soft HEAD~1   # коммит отменён, изменения в staging
git reset --mixed HEAD~1  # коммит отменён, изменения в working tree (дефолт)
git reset --hard HEAD~1   # коммит отменён, изменения уничтожены
```

`git revert` — создаёт новый коммит, отменяющий изменения указанного. История не переписывается:
```bash
git revert abc123
```

| | reset | revert |
|---|---|---|
| История | переписывается | сохраняется |
| Новый коммит | нет | да |
| Безопасно для shared веток | нет | да |

---

**stash**

Временно откладывает незакоммиченные изменения, чтобы переключиться на другую ветку.

```bash
git stash                     # отложить
git stash push -m "описание"  # отложить с именем
git stash list                # stash@{0}, stash@{1}...
git stash pop                 # вернуть последний + удалить
git stash apply               # вернуть, но оставить в stash
git stash drop stash@{1}      # удалить конкретный
git stash -u                  # включить untracked файлы (по умолчанию не попадают)
```

---

**cherry-pick**

Берёт конкретный коммит из любой ветки и применяет на текущую. Новый SHA, оригинал остаётся на месте.

```bash
git cherry-pick abc123
git cherry-pick abc123 def456  # несколько
```

Когда: hotfix попал не в ту ветку, закоммитил на неправильной ветке, нужна одна фича из большой ветки.
Ловушка: злоупотребление → дублирование в истории → признак плохого ветвления.

---

**fetch vs pull**

`git fetch` — скачивает изменения с remote, рабочую ветку не трогает.
`git pull` = fetch + merge (или rebase).

```bash
git fetch origin           # origin/main обновлён, локальный main — нет
git diff main origin/main  # посмотреть что изменилось
git pull origin main       # подтянуть и слить
git pull --rebase          # слить без merge-commit, история линейная
```

---

**merge conflict**

Триггеры: одна строка изменена по-разному, файл изменён и удалён, оба добавили файл с одинаковым именем.

```
<<<<<<< HEAD
return "hello world";
=======
return "hi there";
>>>>>>> feature-branch
```

```bash
git merge --abort          # отменить весь merge
git add filename           # отметить конфликт как решённый
git merge --continue
```

На практике: IntelliJ/VS Code → "Annotate with Git Blame" / визуальный diff с Accept Ours / Accept Theirs.

---

**.gitignore**

```
*.log           # все файлы с расширением
build/          # директория целиком
secret.env      # конкретный файл
!important.log  # исключение из правила
```

Ловушка: не работает на уже отслеживаемых файлах:
```bash
git rm --cached filename   # убрать из отслеживания, файл останется локально
```

Уровни: корень репо → поддиректория → глобальный (`core.excludesFile`).
Что игнорируют: `.env`, `target/`, `build/`, `.idea/`, `*.class`, `*.jar`.

---

**tag**

```bash
git tag v1.0                    # lightweight
git tag -a v1.0 -m "Release"   # annotated (рекомендуется для релизов)
git push origin --tags          # теги не пушатся автоматически с git push!
git checkout v1.0               # detached HEAD
```

В CI/CD тег часто триггерит деплой в прод.

---

**blame**

Показывает для каждой строки файла: кто, когда и каким коммитом написал.

```bash
git blame UserService.java
```

Ловушка: показывает автора текущей версии строки, не оригинального. Рефакторинг/форматирование — blame покажет того кто форматировал. Полная картина: `git log -p filename`.

---

**Branching Strategy**

**Feature Branch Workflow** — самая распространённая: каждая фича = отдельная ветка → PR → merge в main.

**Gitflow:**
```
main       — только релизы (теги v1.0, v2.0)
develop    — интеграционная ветка
feature/*  — от develop
release/*  — подготовка релиза
hotfix/*   — срочный фикс от main
```

**Trunk-Based Development** — все коммитят в main напрямую (или ветки max 1-2 дня). Фичи скрыты за feature flags. Требует зрелого CI/CD.

| | Feature Branch | Gitflow | Trunk-Based |
|---|---|---|---|
| Сложность | низкая | высокая | низкая |
| Подходит для | большинства | несколько версий в проде | зрелые CI/CD команды |

---

## Docker

**Container vs VM**
- VM: гипервизор → гостевое ядро → процессы. Стартует минуты, образ гигабайты.
- Container: процесс на ядре хоста, изолированный через:
  - **namespaces** — изоляция PID, сети, файловой системы, hostname
  - **cgroups** — ограничение CPU, RAM, I/O
- Стартует секунды, образ мегабайты. Изоляция слабее: взлом ядра хоста → все контейнеры.
- Ловушка: Docker на Windows/Mac — внутри Linux VM (Docker Desktop). На Linux — напрямую.

**Dockerfile — основные инструкции**
- `FROM` — базовый образ
- `WORKDIR` — рабочая директория внутри контейнера
- `COPY` — копирует файлы с хоста в образ
- `RUN` — выполняется при **сборке** образа (установка пакетов)
- `EXPOSE` — документирует порт (не открывает реально)
- `ENV` — переменная окружения
- `CMD` — команда при **запуске** контейнера, можно переопределить в `docker run`
- `ENTRYPOINT` — фиксированный исполняемый файл, нельзя переопределить

```dockerfile
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/myapp.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

- `RUN` vs `CMD`: RUN — сборка, CMD — запуск
- `COPY` vs `ADD`: ADD умеет `.tar` и URL, но используй COPY если не нужно это
- Каждый `RUN` = новый слой → объединять через `&&` чтобы не раздувать образ
- `ENTRYPOINT` + `CMD`: ENTRYPOINT фиксирует команду, CMD задаёт аргументы по умолчанию

**docker-compose**
- Оркестрация нескольких контейнеров через один `docker-compose.yml`
- `image` — готовый образ; `build` — собрать из Dockerfile
- `ports` — `хост:контейнер`; `environment` — переменные окружения; `volumes` — персистентность данных
- `depends_on` — порядок старта, но не готовность сервиса (ловушка!)
- Все сервисы в одной сети → обращаются друг к другу по имени сервиса (`db`, `app`)

```bash
docker-compose up -d     # поднять в фоне
docker-compose down      # остановить + удалить контейнеры
docker-compose logs app  # логи сервиса
docker-compose ps        # статус
```

Для QA: `up` перед сьютом → прогон → `down`. Воспроизводимое изолированное окружение в CI.

**Основные команды Docker**
```bash
docker run -d -p 8080:80 --name myapp nginx  # запуск: фон, порт хост:контейнер, имя
docker run --rm nginx                         # удалить контейнер после остановки
docker ps / docker ps -a                      # живые / все контейнеры
docker exec -it myapp bash                    # зайти внутрь контейнера
docker logs -f myapp                          # логи в реальном времени
docker stop myapp / docker rm myapp           # остановить / удалить
docker rm -f myapp                            # остановить + удалить сразу
docker build -t myapp:1.0 .                   # собрать образ из Dockerfile
docker images / docker rmi nginx              # список образов / удалить образ
```
- `docker stop` — SIGTERM (graceful); `docker kill` — SIGKILL (немедленно)
- Для QA: `logs -f` — дебаг сервиса; `exec -it` — проверить файлы/переменные внутри; `ps -a` — понять почему упал

**Volumes**
- Контейнер эфемерный: удалил → данные пропали. Volume — директория вне контейнера.
- Именованный (`pgdata:/var/lib/postgresql/data`) — Docker управляет хранилищем
- Bind mount (`/host/path:/container/path`) — конкретная папка с хоста

| | Именованный | Bind mount |
|---|---|---|
| Хранение | Docker (`/var/lib/docker/volumes/`) | путь на хосте |
| Когда | данные БД | конфиги, логи |

- Для QA: `docker-compose down -v` — удалить контейнеры + volumes → чистая БД перед прогоном

**Docker в CI/CD**
- Пайплайн: `build jar → docker build → docker-compose up → tests → docker-compose down -v`
- Готовность контейнера — не retry в тестах, а `healthcheck` + `depends_on: condition: service_healthy`
- Testcontainers — поднимает контейнеры из Java-кода, гарантирует готовность к первому тесту
- Параллелизация сьютов: `docker-compose -p suite1 up` / `-p suite2 up` — разные namespace, нет конфликтов портов

**Docker Registry / Docker Hub**
- Хранит **образы** (не Dockerfile, не docker-compose)
- Путь: `docker build` → `docker push` → Registry → `docker pull` → сервер
- Docker Hub — публичный. В компаниях: AWS ECR, GitLab Registry, JFrog Artifactory
- Теги: `1.0.0` — для прода; `latest` — опасно (непредсказуемо); `main-abc123` — лучшая практика в CI
- Ловушка: `latest` не обновляется автоматически — это просто тег, нужно явно пушить
