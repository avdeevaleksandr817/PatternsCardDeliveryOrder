package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }

    private DataGenerator() {
    }

    private static Faker faker = new Faker();

    public static String generateDate(int shift) {
        String date = LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return date;
    }

    public static String generateCity(String locale) {

          var city = new String[]{"Абакан","Альметьевск","Ангарск","Арзамас","Армавир","Артем","Архангельск","Астрахань","Ачинск","Балаково"
                  ,"Балашиха","Барнаул","Белгород","Бердск","Березники","Благовещенск","Братск","Брянск","Батайск","Бийск"
                  ,"Великий Новгород","Владивосток","Владикавказ","Владимир","Волгоград","Волгодонск","Волжский","Вологда"
                  ,"Воронеж","Грозный","Дербент","Дзержинск","Димитровград","Долгопрудный","Домодедово","Евпатория","Екатеринбург"
                  ,"Елец","Ессентуки","Железногорск","Жуковский","Зеленодольск","Златоуст","Иваново","Ижевск","Иркутск"
                  ,"Йошкар-Ола","Камышин","Казань","Нижний Новгород","Калининград","Калуга","Каменск — Уральский","Каспийск"
                  ,"Кемерово","Керчь","Киров","Кисловодск","Ковров","Коломна","Комсомольск-на-Амуре","Копейск","Королёв"
                  ,"Кострома","Красногорск","Краснодар","Красноярск","Курган","Курск","Кызыл","Люберцы","Липецк","Магнитогорск"
                  ,"Майкоп","Махачкала","Миасс","Мурманск","Муром","Мытищи","Набережные Челны","Назрань","Нальчик","Находка"
                  ,"Невинномысск","Нефтекамск","Нефтеюганск","Нижневартовск","Нижнекамск","Нижний Тагил","Новокузнецк","Новомосковск"
                  ,"Новороссийск","Новосибирск","Новочебоксарск","Новочеркасск","Новошахтинск","Новый Уренгой","Норильск","Ногинск"
                  ,"Ноябрьск","Обнинск","Одинцово","Октябрьский","Омск","Орёл","Оренбург","Орехово-Зуево","Орск","Пенза"
                  ,"Первоуральск","Пермь","Петрозаводск","Петропавловск-Камчатский","Подольск","Прокопьевск","Псков","Пушкино"
                  ,"Пятигорск","Раменское","Реутов","Ростов-на-Дону","Рыбинск","Рязань","Салават","Самара","Саранск","Саратов"
                  ,"Севастополь","Северодвинск","Северск","Сергиев Посад","Серпухов","Симферополь","Сочи","Ставрополь","Старый Оскол"
                  ,"Стерлитамак","Сургут","Сызрань","Сыктывкар","Санкт-Петербург","Смоленск","Таганрог","Тамбов","Тверь","Тольятти"
                  ,"Томск","Тула","Тюмень","Улан-Удэ","Ульяновск","Уссурийск","Уфа","Хабаровск","Ханты-Мансийск","Хасавюрт","Химки"
                  ,"Чебоксары","Челябинск","Череповец","Черкесск","Чита","Шахты","Щёлково","Электросталь","Элиста","Энгельс"
                  ,"Южно-Сахалинск","Якутск","Ярославль"};
        return city[new Random().nextInt(city.length)];
    }

    public static String generateName(String locale) {
        faker = new Faker(new Locale(locale));
        String name = faker.name().lastName() + " " + faker.name().firstName();
        return name;
    }

    public static String generatePhone(String locale) {
        faker = new Faker(new Locale(locale));
        String phone = faker.phoneNumber().phoneNumber();
        return phone;
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
//            faker = new Faker(new Locale(locale));
            var user = new UserInfo(
                    generateCity(locale),
                    generateName(locale),
                    generatePhone(locale)
            );
            return user;
        }
    }
}







