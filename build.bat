@echo off
echo Compilando sistema de turnos...

mkdir dist 2>nul

javac -d dist src\com\clinica\*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilacion exitosa!
    echo.
    echo Para ejecutar el programa, use:
    echo   java -cp dist com.clinica.Main
) else (
    echo Error en la compilacion
    exit /b 1
)
