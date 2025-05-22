# WaterTrack App
---
## 📝 Залежності (Dependencies)

Додаток використовує кілька бібліотек Android Jetpack та Kotlin. Ось опис ключових залежностей:

* **`androidx.datastore:datastore-preferences:1.1.1`**
    * **Пояснення:** Використовується для асинхронного та постійного зберігання простих даних у форматі "ключ-значення". У цьому додатку, ймовірно, використовується `WaterDataRepository` для збереження та отримання щоденного споживання води.

* **`androidx.compose.material:material-icons-extended-android:1.7.2`**
    * **Пояснення:** Надає розширений набір іконок Material Design для використання в інтерфейсі користувача.

* **`androidx.compose.ui:ui-text-google-fonts:1.5.4`**
    * **Пояснення:** Дозволяє використовувати шрифти, що завантажуються з Google Fonts, у Jetpack Compose.

* **`androidx.compose.material3:material3:1.2.1`**
    * **Пояснення:** Основна бібліотека для створення інтерфейсів користувача з компонентами Material Design 3 у Jetpack Compose.

* **`androidx.navigation:navigation-compose:$nav_version`** (наприклад, 2.8.9)
    * **Пояснення:** Спрощує навігацію між різними екранами (Composable функціями) у додатку Jetpack Compose.

* **`kotlinx.serialization.json`**
    * **Пояснення:** Використовується для серіалізації об'єктів Kotlin у формат JSON та десеріалізації з нього. Це корисно для обробки складних структур даних для аргументів навігації або зберігання даних.

* **`androidx.compose.foundation:foundation:1.6.7`**
    * **Пояснення:** Надає базові будівельні блоки та основну функціональність Compose, таку як `HorizontalPager`.

---
## 🚀 MainActivity.kt

Файл `MainActivity.kt` є точкою входу для програми.

```kotlin
package com.example.watertrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge 
import com.example.watertrack.ui.theme.WaterTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
```
* **`onCreate(savedInstanceState: Bundle?)`**: Це перший метод, який викликається при створенні активності.

```kotlin
        enableEdgeToEdge() 
```
* **`enableEdgeToEdge()`**: Цей виклик функції, ймовірно, дозволяє додатку малювати контент під системними панелями (рядок стану та панель навігації) для більш захоплюючого інтерфейсу.

```kotlin
        setContent { 
            WaterTrackTheme { 
                Navigation() 
            }
        }
    }
}
```
* **`setContent { ... }`**: Тут налаштовується інтерфейс користувача Jetpack Compose.
* **`WaterTrackTheme { ... }`**: Застосовує кастомну тему Material (наприклад, кольори, типографіку), визначену для програми, до всіх Composable функцій всередині.
* **`Navigation()`**: Ця Composable функція викликається для налаштування навігаційного графа програми, визначаючи різні екрани та способи переходу між ними.

---
## 🧭 Navigation.kt

Файл `Navigation.kt` відповідає за управління навігаційним потоком у програмі, переважно використовуючи `HorizontalPager` для перемикання між екранами "Home" та "Stats".

```kotlin
package com.example.watertrack

// ... імпорти ...
import androidx.compose.foundation.pager.HorizontalPager 
import androidx.compose.foundation.pager.rememberPagerState 
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
sealed class Destinations { 
    @Serializable
    object Home : Destinations() 
    @Serializable
    object Stats : Destinations() 
}
```
* **`Destinations` Sealed Class**: Визначає можливі навігаційні пункти призначення (`Home` та `Stats`) безпечним для типів способом.

```kotlin
data class NavigationScreenItem(val route: Destinations, val text: String, val icon: ImageVector)
```
* **`NavigationScreenItem` Data Class**: Зберігає інформацію для кожного екрана, включаючи його маршрут, текст для відображення та іконку.

```kotlin
@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val screens = listOf(
        NavigationScreenItem(Destinations.Home, "Home", Icons.Default.Home),
        NavigationScreenItem(Destinations.Stats, "Stats", Icons.Default.BarChart)
    )
```
* **`screens` List**: Визначає екрани, доступні в пейджері та нижній навігаційній панелі ("Home" та "Stats").

```kotlin
    val pagerState = rememberPagerState(pageCount = { screens.size }) 
    val coroutineScope = rememberCoroutineScope() 

    val currentScreen = screens[pagerState.currentPage] 
```
* **`rememberPagerState`**: Керує станом `HorizontalPager`, наприклад, поточною сторінкою.
* **`coroutineScope`**: Використовується для запуску асинхронних операцій, наприклад, анімації прокрутки пейджера.
* **`currentScreen`**: Визначає поточний активний екран на основі стану `pagerState`, використовується для оновлення заголовка.

```kotlin
    Scaffold(
        topBar = {
            TopAppBar( 
                title = { Text(currentScreen.text) }, 
                navigationIcon = {
                    Image(
                        painter = painterResource(R.drawable.droplet),
                        contentDescription = "App Icon",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
```
* **`Scaffold`**: Надає стандартну структуру макета (TopAppBar, BottomBar).
* **`TopAppBar`**: Відображає заголовок поточного екрана та іконку програми. Заголовок динамічно оновлюється залежно від активної сторінки в пейджері.

```kotlin
        bottomBar = {
            NavigationBar { 
                screens.forEachIndexed { index, screenItem ->
                    NavigationBarItem( 
                        selected = pagerState.currentPage == index, 
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index) 
                            }
                        },
                        icon = { Icon(screenItem.icon, contentDescription = screenItem.text) },
                        label = { Text(screenItem.text) }
                    )
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
```
* **`NavigationBar` (Нижня навігація)**:
    * Проходить по списку `screens` для створення `NavigationBarItem` для кожного елемента.
    * Обробляє кліки на елементах навігації для прокручування `HorizontalPager` до відповідної сторінки.
    * Виділяє поточний вибраний елемент.

```kotlin
        HorizontalPager( 
            state = pagerState,
            modifier = Modifier.padding(paddingValues) 
        ) { pageIndex ->
            when (screens[pageIndex].route) { 
                is Destinations.Home -> HomeScreen()
                is Destinations.Stats -> StatsScreen() 
            }
        }
    }
}
```
* **`HorizontalPager`**:
    * Це основний компонент для навігації між екранами за допомогою свайпів.
    * Він відображає вміст поточної сторінки (`HomeScreen` або `StatsScreen`) на основі `pagerState`.

---
## 🏠 HomeScreen.kt

Файл `HomeScreen.kt` визначає головний екран програми, де користувачі можуть відстежувати своє щоденне споживання води.

```kotlin
package com.example.watertrack

// ... імпорти ...
import androidx.compose.runtime.saveable.rememberSaveable 
import kotlinx.coroutines.launch
import java.time.LocalDate 

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val goal = 2000 
    var currentProgress by rememberSaveable { mutableStateOf(0f) } 
```
* **`goal`**: Константа, що представляє денну норму споживання води (2000 мл).
* **`currentProgress`**: `mutableStateOf`, що зберігається за допомогою `rememberSaveable` для збереження поточного споживання води, яке зберігається при змінах конфігурації. Ініціалізується значенням 0f.

```kotlin
    val context = LocalContext.current
    val waterRepository = remember { WaterDataRepository(context) } 
    val today = LocalDate.now() 
```
* **`waterRepository`**: Екземпляр `WaterDataRepository` для взаємодії зі сховищем даних.
* **`today`**: Поточна дата, використовується для збереження та завантаження даних за конкретний день.

```kotlin
    var isInitialLoadDone by rememberSaveable { mutableStateOf(false) } 
```
* **`isInitialLoadDone`**: Логічний прапорець `rememberSaveable`, який гарантує, що `currentProgress` оновлюється з `DataStore` лише один раз під час початкової композиції.

```kotlin
    val P_loadedProgressFromDataStore by waterRepository.getWaterIntake(today).collectAsState(initial = -1f) 
```
* **`P_loadedProgressFromDataStore`**: Збирає дані про споживання води за поточний день (`today`) з `waterRepository` як `StateFlow`. Ініціалізується значенням -1f, щоб вказати, що дані ще не завантажені.

```kotlin
    LaunchedEffect(P_loadedProgressFromDataStore, isInitialLoadDone) { 
        if (P_loadedProgressFromDataStore != -1f) { 
            if (!isInitialLoadDone) { 
                currentProgress = P_loadedProgressFromDataStore 
                isInitialLoadDone = true 
            }
        }
    }
```
* **`LaunchedEffect`**:
    * Спостерігає за `P_loadedProgressFromDataStore` та `isInitialLoadDone`.
    * Коли `P_loadedProgressFromDataStore` отримує значення, відмінне від -1f (тобто дані завантажені), і `isInitialLoadDone` має значення false, він оновлює `currentProgress` завантаженим значенням і встановлює `isInitialLoadDone` в true. Це запобігає перезапису змін поточного сеансу користувача збереженим значенням, якщо він переходить на інший екран і повертається до того, як потік видасть нове значення.

```kotlin
    val coroutineScope = rememberCoroutineScope()

    val updateAndSaveProgress = { newAmount: Float -> 
        val validAmount = newAmount.coerceAtLeast(0f) 
        currentProgress = validAmount 
        if (isInitialLoadDone) {
            coroutineScope.launch {
                waterRepository.saveWaterIntake(today, validAmount)
            }
        }
    }
```
* **`updateAndSaveProgress` Lambda**:
    * Функція, яка оновлює стан `currentProgress`.
    * Вона гарантує, що нова кількість не є від'ємною (`coerceAtLeast(0f)`).
    * Якщо `isInitialLoadDone` має значення true (тобто початкове завантаження даних завершено), вона запускає корутину для збереження `validAmount` в `waterRepository` для поточного дня `today`. Це запобігає збереженню початкового значення за замовчуванням (0f або -1f) до того, як будуть завантажені фактичні дані або користувач взаємодіятиме.

```kotlin
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column( /* ... для заголовку та тексту прогресу ... */ ) {
            Text("Today's progress", /* ... */)
            Text("${currentProgress.toInt()}ml / ${goal}ml", /* ... */)

            Box( /* ... для повідомлення про досягнення цілі ... */ ) {
                if (currentProgress >= goal) {
                    Text( /* ... Great job! ... */ )
                }
            }
        }
```
* **Макет UI**: `Column` впорядковує елементи вертикально. Відображає текст "Today's progress" та поточне споживання відносно цілі (наприклад, "500ml / 2000ml"). Умовне повідомлення з'являється, якщо ціль досягнута або перевищена.

```kotlin
        FillingCircleWithBorder(
            modifier = Modifier.fillMaxWidth(0.75f).height(240.dp).padding(16.dp),
            progress = (currentProgress / goal).coerceIn(0f, 1f), 
            currentProgress = currentProgress,
            dailyGoal = goal,
            // ... інші параметри кольорів та тексту ...
        )
```
* **`FillingCircleWithBorder` Composable**:
    * Кастомний Composable, який візуально представляє прогрес споживання води у вигляді заповнюваного кола.
    * `progress`: Обчислюється як `(currentProgress / goal).coerceIn(0f, 1f)`, щоб гарантувати, що значення знаходиться в межах від 0 до 1.
    * Вигляд кола (кольори) змінюється, коли ціль досягнута.
    * Використовує `animateFloatAsState` для плавної анімації рівня заповнення.
    * Відображає відсоток або текст "AMAZING!" всередині кола.
    * `Icon` (крапля) відображається всередині кола.

```kotlin
        Spacer(modifier = Modifier.height(24.dp))

        Column( /* ... для кнопок додавання води ... */ ) {
            Text("Add water intake", /* ... */)
            Row( /* ... кнопки +100ml, +250ml, +500ml ... */ ) {
                WaterButton(text = "+100ml", onClick = { updateAndSaveProgress(currentProgress + 100f) })
                WaterButton(text = "+250ml", onClick = { updateAndSaveProgress(currentProgress + 250f) })
                WaterButton(text = "+500ml", onClick = { updateAndSaveProgress(currentProgress + 500f) })
            }
            Row( /* ... кнопки "Full Cup", -100ml ... */ ) {
                WaterButton(text = "Full Cup\n(330ml)", onClick = { updateAndSaveProgress(currentProgress + 330f) })
                WaterButton(text = "-100ml", onClick = { updateAndSaveProgress(currentProgress - 100f) }, /* ... інші кольори ... */)
            }
        }
    }
}
```
* **Кнопки додавання води**:
    * Розділ із заголовком "Add water intake".
    * Кілька Composable `WaterButton` використовуються для швидкого додавання попередньо визначених об'ємів води (наприклад, +100мл, +250мл, +500мл, "Full Cup (330мл)").
    * Також є кнопка для віднімання води (-100мл), стилізована по-іншому (кольори помилки).
    * `onClick` кожної кнопки викликає `updateAndSaveProgress` з відповідним об'ємом.

```kotlin
@Composable
fun WaterButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    // ... параметри кольорів та розмірів ...
) {
    Button(onClick = onClick, /* ... форма, кольори, модифікатор ... */) {
        Text(text = text, /* ... стиль тексту ... */)
    }
}
```
* **`WaterButton` Composable**: Стилізована `Button` для багаторазового використання в цьому додатку. Приймає текст, дію `onClick`, кольори та розміри як параметри.

```kotlin
@Composable
fun FillingCircleWithBorder( 
    progress: Float,
    currentProgress: Float,
    dailyGoal: Int,
    modifier: Modifier = Modifier,
    // ... параметри кольорів, іконки ...
) {
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progressAnimation") 
    val goalReached = currentProgress >= dailyGoal 

    // ... визначення кольорів залежно від goalReached ...

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // ... розрахунки розмірів, радіуса ...
            val fillHeightValue = 2 * radius * animatedProgress 

            // ... малювання фонового кола, обрізка контуру, малювання прямокутника заповнення, малювання рамки ...
        }
        Column( /* ... для іконки та тексту всередині кола ... */ ) {
            Icon(painter = painterResource(icon), /* ... */)
            Text(text = if (goalReached) "AMAZING!" else "${(animatedProgress * 100).toInt()}%", /* ... */)
            if (goalReached) {
                Text(text = "${(currentProgress / dailyGoal * 100).toInt()}%", /* ... */)
            }
        }
    }
}
```
* **Логіка малювання `FillingCircleWithBorder`**:
    * Використовує `Canvas` для малювання кастомної графіки.
    * Малює фонове коло, потім заповнений прямокутник (обрізаний за контуром кола) для представлення прогресу, і, нарешті, зовнішню рамку.
    * Висота заповнення (`fillHeightValue`) обчислюється на основі `animatedProgress`.

---
## 📊 StatsScreen.kt

Файл `StatsScreen.kt` відповідає за відображення історії споживання води користувачем.

```kotlin
package com.example.watertrack

// ... імпорти ...
import com.example.watertrack.data.WaterDataRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle 

@Composable
fun StatsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val waterRepository = remember { WaterDataRepository(context) }
```
* **`waterRepository`**: Екземпляр `WaterDataRepository` для отримання історичних даних.

```kotlin
    val waterHistory by waterRepository.getAllWaterIntakeHistory().collectAsState(initial = emptyMap()) 
```
* **`waterHistory`**: Збирає всю історію споживання води з `waterRepository` як `StateFlow`. Це `Map<String, Float>`, де ключ - це рядок дати, а значення - кількість спожитої води. Ініціалізується порожньою картою.

```kotlin
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Your Water Intake History",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
```
* **Макет UI**: `Column` для загального макета. Заголовок "Your Water Intake History".

```kotlin
        if (waterHistory.isEmpty()) { 
            Text(
                "No data yet. Start tracking your water intake!",
                style = MaterialTheme.typography.bodyLarge
            )
        } else { 
            val P_displayList = waterHistory.toList() 
```
* **Умовний вміст**:
    * Якщо `waterHistory` порожня, відображається "No data yet. Start tracking your water intake!".
    * В іншому випадку, історія відображається в `LazyColumn`.
* **`P_displayList`**: Карта `waterHistory` перетворюється на список пар (`List<Pair<String, Float>>`) для `LazyColumn`. У коментарі зазначено, що сортування ключів (дат) очікується в репозиторії.

```kotlin
            LazyColumn( 
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(P_displayList) { (dateString, intake) -> 
                    val P_displayDate = try {
                        LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
                            .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                    } catch (e: Exception) {
                        dateString 
                    }
                    WaterHistoryItem(date = P_displayDate, intake = intake.toInt()) 
                }
            }
        }
    }
}
```
* **`LazyColumn` для історії**:
    * Ефективно відображає список історичних записів.
    * `items(P_displayList)`: Проходить по історичних даних.
    * Для кожного запису намагається розпарсити рядок дати (`dateString`) в `LocalDate`, а потім відформатувати його в більш читабельну локалізовану дату (наприклад, "22 трав. 2025 р."). Якщо розбір не вдається, відображається вихідний рядок дати як запасний варіант. Це позначено як потенційний "недолік", якщо розбір не вдасться.
    * Кожен елемент історії відображається за допомогою Composable `WaterHistoryItem`.

```kotlin
@Composable
fun WaterHistoryItem(date: String, intake: Int, modifier: Modifier = Modifier) { 
    Card( 
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row( 
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = date, style = MaterialTheme.typography.bodyLarge) 
            Text(
                text = "${intake}ml",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
```
* **`WaterHistoryItem` Composable**:
    * `Card` Composable для відображення одного запису про споживання води за день.
    * Показує відформатовану дату та кількість спожитої води (наприклад, "22 трав. 2025 р." та "1500ml").
    * Використовує `Row` для розташування дати та кількості поруч.

---
## 🗄️ WaterDataRepository.kt

Файл `WaterDataRepository.kt` відповідає за управління даними про споживання води, використовуючи DataStore для постійного зберігання.

```kotlin
package com.example.watertrack.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "water_prefs_datastore") 
```
* **Context Extension**: Створює розширення для `Context`, яке забезпечує доступ до єдиного екземпляра `DataStore<Preferences>` з назвою "water_prefs_datastore".

```kotlin
class WaterDataRepository(private val context: Context) {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE // Формат: yyyy-MM-dd
```
* **`WaterDataRepository` Class**: Основний клас для роботи з даними про споживання води.
* **`dateFormatter`**: Використовує ISO стандартний формат дати (yyyy-MM-dd) для узгодженого форматування ключів.

```kotlin
    private fun getPreferenceKey(date: LocalDate): Preferences.Key<Float> {
        return floatPreferencesKey(date.format(dateFormatter))
    }
```
* **`getPreferenceKey` Method**: Перетворює `LocalDate` у `Preferences.Key<Float>`, використовуючи відформатовану дату як ключ. Це забезпечує узгодженість у тому, як дати зберігаються та отримуються з DataStore.

```kotlin
    suspend fun saveWaterIntake(date: LocalDate, amount: Float) {
        context.dataStore.edit { preferences ->
            preferences[getPreferenceKey(date)] = amount
        }
    }
```
* **`saveWaterIntake` Method**: 
    * Зберігає кількість спожитої води (`amount`) для конкретної дати (`date`).
    * Використовує `context.dataStore.edit` для атомарного оновлення значення.
    * `suspend` функція, тому що операції DataStore є асинхронними.

```kotlin
    fun getWaterIntake(date: LocalDate): Flow<Float> {
        return context.dataStore.data.map { preferences ->
            preferences[getPreferenceKey(date)] ?: 0f
        }
    }
```
* **`getWaterIntake` Method**:
    * Повертає `Flow<Float>` для конкретної дати, що дозволяє реактивне спостереження за змінами даних.
    * Якщо дані для дати не знайдені, повертає 0f як значення за замовчуванням.
    * `Flow` автоматично емітує нові значення при зміні даних у DataStore.

```kotlin
    fun getAllWaterIntakeHistory(): Flow<Map<String, Float>> {
        return context.dataStore.data.map { preferences ->
            preferences.asMap()
                .filterKeys { key ->
                    try {
                        LocalDate.parse(key.name, dateFormatter)
                        true
                    } catch (e: Exception) {
                        false
                    }
                }
```
* **`getAllWaterIntakeHistory` Method**: Повертає всю історію споживання води як `Flow<Map<String, Float>>`.
* **Фільтрація ключів**: Перевіряє, чи кожен ключ у DataStore може бути розпарсений як дата. Це допомагає відфільтрувати будь-які неправильні ключі, які могли б потрапити до DataStore.

```kotlin
                .mapNotNull { (key, value) ->
                    if (value is Float) {
                        key.name to value
                    } else {
                        null
                    }
                }
                .toMap()
```
* **Перетворення типів**: Перетворює ключі на рядки (`key.name`) та перевіряє, що значення є типу `Float`. `mapNotNull` відфільтровує будь-які недійсні записи.

```kotlin
                .toSortedMap(compareByDescending { it })
        }
    }
}
```
* **Сортування**: Створює відсортовану мапу з датами в порядку спадання (найновіші дати спочатку). Формат "yyyy-MM-dd" дозволяє правильне лексикографічне сортування рядків, але це може бути неочевидно для початківців.
