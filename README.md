# sbase

Полноэкранный Android-браузер на `WebView` (без адресной строки), где кнопка **Назад** сворачивает приложение.

## Почему возникает `INSTALL_FAILED_USER_RESTRICTED`

Ошибка

`INSTALL_FAILED_USER_RESTRICTED: Installation via USB is disabled`

означает, что установка APK заблокирована **на устройстве**, а не в коде проекта.

## Как исправить на телефоне

1. Включите **Режим разработчика**.
2. Откройте `Настройки` → `Для разработчиков`.
3. Включите:
   - **USB debugging**
   - **Install via USB** (если есть)
   - **USB debugging (Security settings)** (на некоторых прошивках)
4. Разблокируйте экран телефона и подтвердите диалог доверия для ПК (`Allow USB debugging`).
5. Переподключите кабель и повторите запуск.

## Особые случаи (MIUI/HyperOS и корпоративные устройства)

- **MIUI/HyperOS**: часто нужно включить одновременно `USB debugging`, `USB debugging (Security settings)` и `Install via USB`.
- **Рабочий/MDM-телефон**: установка может блокироваться политиками администратора. В этом случае нужен администратор устройства.

## Проверка через ADB

```bash
adb devices
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Если снова `INSTALL_FAILED_USER_RESTRICTED`, проблема точно в ограничениях устройства.
