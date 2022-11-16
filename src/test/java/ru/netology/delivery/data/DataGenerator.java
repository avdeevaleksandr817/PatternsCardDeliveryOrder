package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
        faker = new Faker(new Locale(locale));
        var city = (faker.address().city());
        return city;
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
            faker = new Faker(new Locale(locale));
            var user = new UserInfo(
                    generateCity(locale),
                    generateName(locale),
                    generatePhone(locale)
            );
            return user;
        }
    }
}