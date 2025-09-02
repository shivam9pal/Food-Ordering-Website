@echo off
echo Running Food Ordering Website Tests...
echo.

echo Running Unit Tests...
call mvn test -Dtest="*Test" -DfailIfNoTests=false

echo.
echo Running Integration Tests...
call mvn test -Dtest="*IntegrationTest" -DfailIfNoTests=false

echo.
echo All tests completed!
pause

