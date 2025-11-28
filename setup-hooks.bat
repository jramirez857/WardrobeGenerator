@echo off
REM Setup Git hooks for the project (Windows)

echo Setting up Git hooks...
echo.

REM Git hooks should work on Windows without chmod
echo Git hooks configured successfully!
echo.
echo The pre-commit hook will now run Spotless checks before each commit.
echo.
echo Useful commands:
echo   gradlew spotlessCheck  - Check for formatting issues
echo   gradlew spotlessApply  - Auto-format all code
echo.
pause
