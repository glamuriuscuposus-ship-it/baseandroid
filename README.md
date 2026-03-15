# sbase

Полноэкранный Android-браузер на `WebView` (без адресной строки), где кнопка **Назад** сворачивает приложение.

## Почему APK может не устанавливаться вручную

Чаще всего причина не в коде, а в одном из пунктов:

- APK собран как `debug`, а устройство блокирует установку через USB/ADB.
- APK **не подписан** (или подпись повреждена).
- На телефоне выключена установка из неизвестных источников для приложения-установщика (Файлы/Браузер/Telegram).
- Версия/подпись конфликтует с уже установленным приложением (`INSTALL_FAILED_UPDATE_INCOMPATIBLE`).
- Устройство/прошивка ограничивает установку политиками (`INSTALL_FAILED_USER_RESTRICTED`).

## Как собрать релизный APK

### 1) Создать release-keystore

```bash
keytool -genkeypair -v \
  -keystore release-key.jks \
  -alias sbase \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

Сохраните `release-key.jks` в безопасном месте (лучше не коммитить в Git).

### 2) Добавить параметры подписи в `~/.gradle/gradle.properties`

```properties
SBASE_STORE_FILE=/absolute/path/to/release-key.jks
SBASE_STORE_PASSWORD=your_store_password
SBASE_KEY_ALIAS=sbase
SBASE_KEY_PASSWORD=your_key_password
```

### 3) Подключить signingConfig в `app/build.gradle.kts`

В `android { ... }` добавить `signingConfigs` и использовать его в `buildTypes.release`:

```kotlin
signingConfigs {
    create("release") {
        storeFile = file(providers.gradleProperty("SBASE_STORE_FILE").get())
        storePassword = providers.gradleProperty("SBASE_STORE_PASSWORD").get()
        keyAlias = providers.gradleProperty("SBASE_KEY_ALIAS").get()
        keyPassword = providers.gradleProperty("SBASE_KEY_PASSWORD").get()
    }
}

buildTypes {
    release {
        isMinifyEnabled = false
        signingConfig = signingConfigs.getByName("release")
    }
}
```

### 4) Собрать APK

```bash
./gradlew assembleRelease
```

Готовый файл:

`app/build/outputs/apk/release/app-release.apk`

## Если релизный APK не ставится вручную

1. Удалите старую версию приложения перед установкой (если подпись отличается).
2. Включите «Установка неизвестных приложений» для приложения, из которого открываете APK.
3. Проверьте, что файл не поврежден (пересоберите и перекачайте).
4. Попробуйте установку через ADB для диагностики точной причины:

```bash
adb install -r app/build/outputs/apk/release/app-release.apk
```

## Почему возникает `INSTALL_FAILED_USER_RESTRICTED`

Ошибка:

`INSTALL_FAILED_USER_RESTRICTED: Installation via USB is disabled`

означает, что установка APK заблокирована **на устройстве**, а не в коде проекта.

### Как исправить на телефоне

1. Включите **Режим разработчика**.
2. Откройте `Настройки` → `Для разработчиков`.
3. Включите:
   - **USB debugging**
   - **Install via USB** (если есть)
   - **USB debugging (Security settings)** (на некоторых прошивках)
4. Разблокируйте экран телефона и подтвердите диалог доверия для ПК (`Allow USB debugging`).
5. Переподключите кабель и повторите запуск.

### Особые случаи (MIUI/HyperOS и корпоративные устройства)

- **MIUI/HyperOS**: часто нужно включить одновременно `USB debugging`, `USB debugging (Security settings)` и `Install via USB`.
- **Рабочий/MDM-телефон**: установка может блокироваться политиками администратора. В этом случае нужен администратор устройства.
