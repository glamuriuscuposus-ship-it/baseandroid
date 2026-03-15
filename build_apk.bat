@echo off
setlocal EnableExtensions

REM Usage:
REM   build_apk.bat           -> debug APK
REM   build_apk.bat debug     -> debug APK
REM   build_apk.bat release   -> release APK

set MODE=%~1
if "%MODE%"=="" set MODE=debug

if not exist gradlew.bat (
  echo [ERROR] gradlew.bat not found in project root.
  echo Run this script from the repository root.
  exit /b 1
)

if /I "%MODE%"=="debug" (
  echo [INFO] Building DEBUG APK...
  call gradlew.bat clean :app:assembleDebug
  if errorlevel 1 (
    echo [ERROR] Debug build failed.
    exit /b 1
  )

  echo.
  echo [OK] Debug APK built:
  echo app\build\outputs\apk\debug\app-debug.apk
  exit /b 0
)

if /I "%MODE%"=="release" (
  echo [INFO] Building RELEASE APK...
  echo [INFO] Ensure signing is configured if required by your Gradle setup.
  call gradlew.bat clean :app:assembleRelease
  if errorlevel 1 (
    echo [ERROR] Release build failed.
    exit /b 1
  )

  echo.
  echo [OK] Release APK built:
  echo app\build\outputs\apk\release\app-release.apk
  exit /b 0
)

echo [ERROR] Unknown mode: %MODE%
echo Usage: build_apk.bat [debug^|release]
exit /b 1
