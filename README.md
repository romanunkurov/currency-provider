# currency-provider
It's testing project for Alfa job vacancy Junior Java Developer

Описание:
Сервис, к которому можно обратиться за курсом фиатной валюты по отношению к американскому доллару, в ответе отображается случайная GIF:
Если вводимый курс по отношению к USD за сегодня изменился, то сервис отдает случайную GIF ->
* вырос - https://giphy.com/search/rich
* упал  - https://giphy.com/search/broke

** Если сервис используется в выходное время, то берется последнее пятничное обновление**

Ссылки на API
Курс валют - https://docs.openexchangerates.org/
Гифки - https://developers.giphy.com/docs/api#quick-start-guide

Сервис реализован на Spring boot последней вресии
На сервис написаны тесты
В качестве сборки был использован Gradle

Инструкция по запуску: 

1. Base URL: ${YOUR_SERVICE_URL:http://localhost:8080}/v1/currency/provider/toUSD/{{ticker}}

2. Пример на локальном хосте с парой Рубль/Доллар: GET http://localhost:8080/v1/currency/provider/toUSD/RUB
