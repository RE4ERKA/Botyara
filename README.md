# Botyara

Ботяра является чат-ботом для платформы Discord.

Discord-сервер: https://discord.com/invite/NrhBehZ7pN

### Основные системы.
1. Система слушателей.
2. Система активностей.
3. Система режима сна.
4. Система настроения.
5. Система дружбы.

## Система слушателей.

Система слушателей позволяет боту прослушивать, что содержится в сообщении пользователей и по-особенному ему отвечать.

Имеются такие слушатели как:
1. Стандарный (DefaultListener) - это слушатель который выполняется без условий в конце прослушивания всех слушателей.
2. Фильтр (FilterListener) - это слушатель который выполняется если в сообщении пользователя содержатся запрещенные слова.
3. Повторение (RepetitionListener) - это слушатель который выполняется когда пользователь повторяет прошлое сообщение.
4. Сон (SleepingListener) - это слушатель который выполняется когда бот спит.

Также и остальные слушатели или слушатели конфигурации (ConfigListener) которые загружаются с папки "listeners".
 
## Система активностей.

Система активностей позволяет обновлять статус бота (PlayingActivity, ListeningActivity, WatchActivity), режим сна (SleepActivity) и настроение (MoodActivity).

Имеются такие активности как:
1. PlayingActivity - это активность с мини-играми.
2. ListeningActivity - это активность с музыкой.
3. WatchActivity - это активность с видео из YouTube каналов.
4. EatingActivity - это активность с едой.
5. NothingActivity - это пустая активность.

Список мини-игр, музыки и отслеживыемых айди YouTube каналов настраивается в файле "properties.yml".

## Система режима сна.

Днем бот находится в сети и обновляет раз в указанное время свои активности, а ночью его статус меняется на "не в сети".

Если во время сна написать боту, то ваша репутация будет снижена и будет активирован слушатель SleepingListener.

Время сна, во сколько он будет просыпаться и ложиться указывается в properties.yml

## Система дружбы.

Система дружбы позволяет пользователям дружить с ботом. Тип дружбы зависит от числа репутации.

Репутация может расти от общения с ботом.

Репутация спускается если:
1. Разбудить бота пока он спит.
2. Не общаться с ботом более двух дней. С каждым днем увеличивается штраф.
3. Переспрашивать одно и то же предложение.

Типы дружбы:
- Незнакомец. Дается тем кого имя бот еще не знает.
- Знакомый. Требуется репутации: 0.
- Друг. Требуется репутации: 150.
- Лучший друг. Требуется репутации: 500.

При определенных типах дружбы открываются новые слушатели или ответы в определенных слушателях.


