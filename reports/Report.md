Отчёт по итогам тестирования

Описание: Было произведено тестирование оплаты по карте (одной формы)
комплексного сервиса по покупке тура.

Количество тест-кейсов: Запланировано было 42 теста, указанных в файле
Plan.md. Во время тестирования было добавлено ещё 6 тестов позитивного происхождения и 3 теста негативного происхождения. Общая сумма тестов 51.

Процент успешных и неуспешных тест-кейсов: 37 тестов было пройдено успешно,
14 тестов были провалены.Не успешные тесты составили 27,45%.

Общие рекомендации: 

1.В первую очередь необходимо устранение ошибки при выводе сообщения об одобрении операции банком, но к тому же в БД заносится информация о том что операция не прошла(issues #1).

2.В поле “Владелец” устранить отсутствие каких-либо ограничений на ввод.
