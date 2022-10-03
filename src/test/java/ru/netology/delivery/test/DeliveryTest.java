package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.Keys.BACK_SPACE;

class DeliveryTest {
    private Faker faker = new Faker(new Locale("ru"));

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Happy Path")
    void shouldSuccessfulPlanAndRescheduleMeeting() {
        //По завершении оставить браузер открытым
        Configuration.holdBrowserOpen = true;
        //Валидный пользователь = Запустить класс генератор данных. Вызвать метод регистрация. Создать пользователя (русский)
        var validUser = DataGenerator.Registration.generateUser("ru");
        //дней, до первой встречи
        var daysToAddForFirstMeeting = 4;
        //Дата первой встречи = Запустить класс генератор данных. Метод создать дату(дней, чтобы добавить для первой встречи)
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        //дней, до второй встречи
        var daysToAddForSecondMeeting = 7;
        //Дата второй встречи = Запустить класс генератор данных. Метод создать дату(дней, чтобы добавить для второй встречи)
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        //По селектору выбираем Город. Методом.setValue устанавливаем значение(валидный пользователь. Метод получить город)
        $("[data-test-id=city] input").setValue(validUser.getCity());
        //Из-за бага, значение дата, ввести руками невозможно. Используем программный метод
        //выбираем поле дата по селектору. Вызываем метод отправить ключи(через сочетание клавиш выделяем все в поле)
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        //Выбираем то же самое поле по селектору. Вызываем метод отправить ключи(выделенное командой .CONTROL + "A" удаляем BACK_SPACE)
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        //Теперь поле очищено можно вводить свои данные
        //выбираем то же самое поле по селектору. Вызываем метод установить значение(вносим дату ПЕРВОЙ встречи)
        $("[data-test-id=date] input").setValue(String.valueOf(firstMeetingDate));
        //По селектору выбираем ИМЯ. Методом.setValue устанавливаем значение(валидный пользователь. Метод получить имя)
        $("[data-test-id=name] input").setValue(validUser.getName());
        //По селектору выбираем ТЕЛЕФОН. Методом.setValue устанавливаем значение(валидный пользователь. Метод получить телефон)
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        //Ставим галочку на согласие в "Я соглашаюсь с условиями обработки и использования моих персональных данных"
        $("[data-test-id=agreement]").click();
        //Жмем кнопку "запланировать"
        $(".button_theme_alfa-on-white").click();
        //По селектору находим всплывающее окно"Успешно"
        $("[data-test-id=success-notification] .notification__content")
                //. Содержит "Встреча успешно запланирована на" + дата встречи. Длительность ожидания ответа страницы(15 секунд)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15))
                // Сообщение должно быть видимым на странице.
                .shouldBe(Condition.visible);
        //Из-за бага, значение дата, ввести руками невозможно. Используем программный метод
        //выбираем поле дата по селектору. Вызываем метод отправить ключи(через сочетание клавиш выделяем все в поле)
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        //Выбираем то же самое поле по селектору. Вызываем метод отправить ключи(выделенное командой .CONTROL + "A" удаляем BACK_SPACE)
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        //Теперь поле очищено можно вводить свои данные
        //выбираем то же самое поле по селектору. Вызываем метод установить значение(вносим дату ВТОРОЙ встречи)
        $("[data-test-id=date] input").setValue(String.valueOf(secondMeetingDate));
        //Выбираем кнопку "Запланировать" по селектору. Используем метод клик - нажимаем
        $(".button_size_m").click();
        //По селектору находим уведомление о перепланировке
        $("[data-test-id=replan-notification] .notification__content")
                //Должен иметь (Условие .text("У вас уже запланирована встреча на другом сервере. Перепланировать?"), Продолжительность. Секунд 15
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"), Duration.ofSeconds(15))
                //Должно Быть (Условие. Фраза "У вас уже запланирована встреча на другую дату. Перепланировать?" должна быть видимой на странице);
                .shouldBe(Condition.visible);
        //Выбираем кнопку "Перепланировать" по селектору. Используем метод клик - нажимаем
        $(".button_size_s").click();
        //По селектору находим уведомление об успехе
        $("[data-test-id=success-notification] .notification__content")
                //. Содержит "Встреча успешно запланирована на" + дата ВТОРОЙ встречи. Длительность ожидания ответа страницы(15 секунд)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15))
                //Должно Быть (Условие. Фраза "Встреча успешно запланирована на" + secondMeetingDate - должна быть видимой на странице);
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Поле(Фамилия и Имя) нет валидации буквы Ё")
    void ErrorNameValidationRU() {
        //По завершении оставить браузер открытым
        Configuration.holdBrowserOpen = true;
        //Валидный пользователь = Запустить класс генератор данных. Вызвать метод регистрация. Создать пользователя (русский)
        var validUser = DataGenerator.Registration.generateUser("ru");
        //дней, до первой встречи
        var daysToAddForFirstMeeting = 4;
        //Дата первой встречи = Запустить класс генератор данных. Метод создать дату(дней, чтобы добавить для первой встречи)
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        //дней, до второй встречи
        var daysToAddForSecondMeeting = 7;
        //Дата второй встречи = Запустить класс генератор данных. Метод создать дату(дней, чтобы добавить для второй встречи)
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        //По селектору выбираем Город. Методом.setValue устанавливаем значение(валидный пользователь. Метод получить город)
        $("[data-test-id=city] input").setValue(validUser.getCity());
        //Из-за бага, значение дата, ввести руками невозможно. Используем программный метод
        //выбираем поле дата по селектору. Вызываем метод отправить ключи(через сочетание клавиш выделяем все в поле)
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        //Выбираем то же самое поле по селектору. Вызываем метод отправить ключи(выделенное командой .CONTROL + "A" удаляем BACK_SPACE)
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        //Теперь поле очищено можно вводить свои данные
        //выбираем то же самое поле по селектору. Вызываем метод установить значение(вносим дату ПЕРВОЙ встречи)
        $("[data-test-id=date] input").setValue(String.valueOf(firstMeetingDate));
        //По селектору выбираем ИМЯ. Методом.setValue устанавливаем значение(валидный пользователь. Метод получить имя)
        $("[data-test-id=name] input").setValue("Ёлкина Ольга");
        //По селектору выбираем ТЕЛЕФОН. Методом.setValue устанавливаем значение(валидный пользователь. Метод получить телефон)
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        //Ставим галочку на согласие в "Я соглашаюсь с условиями обработки и использования моих персональных данных"
        $("[data-test-id = agreement]").click();
        //Жмем кнопку "запланировать"
        $(".button_theme_alfa-on-white").click();
        $("[data-test-id=name]")
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Поле(Фамилия и Имя) не валидирует количество вводимых значений, можно ввести только одну букву")
    void ErrorNameValidationRUSurname() {
        //По завершении оставить браузер открытым
        Configuration.holdBrowserOpen = true;
        //Валидный пользователь = Запустить класс генератор данных. Вызвать метод регистрация. Создать пользователя (русский)
        var validUser = DataGenerator.Registration.generateUser("ru");
        //дней, до первой встречи
        var daysToAddForFirstMeeting = 4;
        //Дата первой встречи = Запустить класс генератор данных. Метод создать дату(дней, чтобы добавить для первой встречи)
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        //дней, до второй встречи
        var daysToAddForSecondMeeting = 7;
        //Дата второй встречи = Запустить класс генератор данных. Метод создать дату(дней, чтобы добавить для второй встречи)
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        //По селектору выбираем Город. Методом.setValue устанавливаем значение(валидный пользователь. Метод получить город)
        $("[data-test-id=city] input").setValue(validUser.getCity());
        //Из-за бага, значение дата, ввести руками невозможно. Используем программный метод
        //выбираем поле дата по селектору. Вызываем метод отправить ключи(через сочетание клавиш выделяем все в поле)
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        //Выбираем то же самое поле по селектору. Вызываем метод отправить ключи(выделенное командой .CONTROL + "A" удаляем BACK_SPACE)
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        //Теперь поле очищено можно вводить свои данные
        //выбираем то же самое поле по селектору. Вызываем метод установить значение(вносим дату ПЕРВОЙ встречи)
        $("[data-test-id=date] input").setValue(String.valueOf(firstMeetingDate));
        //По селектору выбираем ИМЯ. Методом.setValue устанавливаем значение(валидный пользователь. Метод получить имя)
        $("[data-test-id=name] input").setValue("И");
        //По селектору выбираем ТЕЛЕФОН. Методом.setValue устанавливаем значение(валидный пользователь. Метод получить телефон)
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        //Ставим галочку на согласие в "Я соглашаюсь с условиями обработки и использования моих персональных данных"
        $("[data-test-id = agreement]").click();
        //Жмем кнопку "запланировать"
        $(".button_theme_alfa-on-white").click();
        $("[data-test-id = success-notification] .notification__content")
                //. Содержит "Встреча успешно запланирована на" + дата встречи. Длительность ожидания ответа страницы(15 секунд)
                .shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15))
                // Сообщение должно быть видимым на странице.
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("В поле телефон можно ввести только одну цифру, регистрация успешна")
    void ErrorPhoneValidationOneNumber() {
        //По завершении оставить браузер открытым
        Configuration.holdBrowserOpen = true;
        //Валидный пользователь = Запустить класс генератор данных. Вызвать метод регистрация. Создать пользователя (русский)
        var validUser = DataGenerator.Registration.generateUser("ru");
        //дней, до первой встречи
        var daysToAddForFirstMeeting = 4;
        //Дата первой встречи = Запустить класс генератор данных. Метод создать дату(дней, чтобы добавить для первой встречи)
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        //дней, до второй встречи
        var daysToAddForSecondMeeting = 7;
        //Дата второй встречи = Запустить класс генератор данных. Метод создать дату(дней, чтобы добавить для второй встречи)
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        //По селектору выбираем Город. Методом.setValue устанавливаем значение(валидный пользователь. Метод получить город)
        $("[data-test-id=city] input").setValue(validUser.getCity());
        //Из-за бага, значение дата, ввести руками невозможно. Используем программный метод
        //выбираем поле дата по селектору. Вызываем метод отправить ключи(через сочетание клавиш выделяем все в поле)
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        //Выбираем то же самое поле по селектору. Вызываем метод отправить ключи(выделенное командой .CONTROL + "A" удаляем BACK_SPACE)
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        //Теперь поле очищено можно вводить свои данные
        //выбираем то же самое поле по селектору. Вызываем метод установить значение(вносим дату ПЕРВОЙ встречи)
        $("[data-test-id=date] input").setValue(String.valueOf(firstMeetingDate));
        //По селектору выбираем ИМЯ. Методом.setValue устанавливаем значение(валидный пользователь. Метод получить имя)
        $("[data-test-id=name] input").setValue(validUser.getName());
        //По селектору выбираем ТЕЛЕФОН. Методом.setValue устанавливаем значение(валидный пользователь. Метод получить телефон)
        $("[data-test-id=phone] input").setValue("1");
        //Ставим галочку на согласие в "Я соглашаюсь с условиями обработки и использования моих персональных данных"
        $("[data-test-id = agreement]").click();
        //Жмем кнопку "запланировать"
        $(".button_theme_alfa-on-white").click();
        //Всплывающее окно"Успешно"
        $("[data-test-id = success-notification] .notification__content")
                //. Содержит "Встреча успешно запланирована на" + дата встречи. Длительность ожидания ответа страницы(15 секунд)
                .shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15))
                // Сообщение должно быть видимым на странице.
                .shouldBe(Condition.visible);
    }

    @AfterEach
    void closeWebBrowser() {
        closeWebDriver();
    }
}