[![Java CI with Gradle](https://github.com/rusLAN-000/AutomatedTestingCoursework/actions/workflows/gradle.yml/badge.svg)](https://github.com/rusLAN-000/AutomatedTestingCoursework/actions/workflows/gradle.yml)

Запуск автотестов после клонирования репозитория:

1.На компьютере должны быть установлены программы IntelliJ IDEA и Docker Desktop

2.Проект открывается в IntelliJ IDEA

3.Запускается Docker Desktop

4.В командной строке IntelliJ IDEA вводится: docker-compose up

5.В новой командной строке IntelliJ IDEA вводится: java -jar artifacts/aqa-shop.jar

6.В новой командной строке IntelliJ IDEA вводится: ./gradlew clean test

7.В той же командной строке вводится: ./gradlew allureserve
