require: slotfilling/slotFilling.sc
  module = sys.zb-common
  
require: wordData.csv
    name = word
    var = word

require: common.js
    module = sys.zb-common
  
theme: /

    state: Правила
        q!: $regex</start>
        intent!: /Давай поиграем
        a: Я загадаю любое слово, а ты будешь отгадывать. Если ошибешься больше 6 рза - игра закончится. Также, ты можешь в любой момент попробовать отгадать всё слово целиком! Если не получится, это тоже засчитается за одну ошибку. Ну что, начнём?
        go!: /Правила/Согласен?

        state: Согласен?
        
            state: Да
                intent: /Согласие
                go!: /Игра

            state: Нет
                intent: /Несогласие
                a: Ну и ладно! Если передумаешь - скажи "давай поиграем".

    state: Игра
        # сгенерируем случайное число и перейдем в стейт /Проверка
        script:
            $session.word = word[id].random();
            # $reactions.answer("Загадано {{$session.number}}");
            $reactions.transition("/Проверка");
        
    state: Проверка
        intent: /Число
        script:
            # сохраняем введенное пользователем число
            var num = $parseTree._Number;

            # проверяем угадал ли пользователь загаданное число и выводим соответствующую реакцию
            if (num == $session.number) {
                $reactions.answer("Ты выиграл! Хочешь еще раз?");
                $reactions.transition("/Правила/Согласен?");
            }
            else
                if (num < $session.number)
                    $reactions.answer(selectRandomArg(["Мое число больше!", "Бери выше", "Попробуй число больше"]));
                else $reactions.answer(selectRandomArg(["Мое число меньше!", "Подсказка: число меньше", "Дам тебе еще одну попытку! Мое число меньше."]));

    state: NoMatch || noContext = true
        event!: noMatch
        random:
            a: Я не понял.
            a: Что вы имеете в виду?
            a: Ничего не пойму


