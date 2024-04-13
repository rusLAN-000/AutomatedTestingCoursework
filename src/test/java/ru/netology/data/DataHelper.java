
package ru.netology.data;

import com.github.javafaker.Faker;
import com.ibm.icu.text.Transliterator;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {

    private static final Faker faker = new Faker(new Locale("ru"));

    private DataHelper() {
    }

    public static String getApprovedCard() {
        return "1111222233334444";
    }

    public static String getDeclinedCard() {
        return "5555666677778888";
    }

    public static String generateMonth() {
        var month = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        return month[new Random().nextInt(month.length)];
    }

    public static String generateYear() {
        int minYear = 24;
        int maxYear = 29;
        int year = (int) (Math.random() * (maxYear - minYear + 1)) + minYear;
        LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy"));
//        var year = new String[]{LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy")),
//                LocalDate.now().plusYears(2).format(DateTimeFormatter.ofPattern("yy")),
//                LocalDate.now().plusYears(3).format(DateTimeFormatter.ofPattern("yy")),
//                LocalDate.now().plusYears(4).format(DateTimeFormatter.ofPattern("yy")),
//                LocalDate.now().plusYears(5).format(DateTimeFormatter.ofPattern("yy"))};
//        return year[new Random().nextInt(year.length)];
        return Integer.toString(year);
    }

    public static String getCurrentMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String gerCurrentYear() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String generateOwner() {
        String name = faker.name().firstName();
        String lastName = faker.name().lastName();
        String CYRILLIC_TO_LATIN = "Russian-Latin/BGN";
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        String result = toLatinTrans.transliterate(name + " " + lastName);
        result = result.replaceAll("ʹ", "");
        return result;
    }

    public static String generateRussianOwner() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String result = (firstName + " " + lastName);
        result = result.replaceAll("ʹ", "");
        return result;

    }

    public static String generateCode() {
        String code = "";
        for (int i = 0; i <= 2; i++) {
            int x = (int) (Math.random() * 10);
            String strX = Integer.toString(x);
            code = code + strX;
        }
        return code;
    }

    @Value
    public static class CardInfo {
        String number;
        String month;
        String year;
        String owner;
        String code;
    }

    public static CardInfo getInvalidCardWithRussianOwner() {
        return new CardInfo(getApprovedCard(), generateMonth(), generateYear(), generateRussianOwner(), generateCode());
    }

    public static CardInfo getApprovedCardAllForm() {
        return new CardInfo(getApprovedCard(), generateMonth(), generateYear(), generateOwner(), generateCode());
    }

    public static CardInfo getDeclinedCardAllForm() {
        return new CardInfo(getDeclinedCard(), generateMonth(), generateYear(), generateOwner(), generateCode());
    }

    public static String generateInvalidNumericData(int lengthNumber) {
        String number = "";
        for (int i = 0; i < lengthNumber; i++) {
            int x = (int) (Math.random() * 10);
            String strX = Integer.toString(x);
            number = number + strX;
        }
        return number;
    }

    public static String generateInvalidCyrillicData(int sumSymbol) {

        Random symbol = new Random();
        String cyrillic = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        String cyrillicAll = cyrillic + cyrillic.toUpperCase();

        String randomString = "";
        for (int i = 0; i < sumSymbol; i++) {
            char randomSymbol = cyrillicAll.charAt(symbol.nextInt(cyrillicAll.length()));
            randomString = randomString + randomSymbol;
        }
        return randomString;
    }

    public static String generateInvalidLatinData(int sumSymbol) {

        Random symbol = new Random();
        String latin = "abcdefghijklmnopqrstuvwxyz";
        String latinAll = latin + latin.toUpperCase();

        String randomString = "";
        for (int i = 0; i < sumSymbol; i++) {
            char randomSymbol = latinAll.charAt(symbol.nextInt(latinAll.length()));
            randomString = randomString + randomSymbol;
        }
        return randomString;
    }

    public static String generateInvalidSymbolData(int sumSymbol) {

        Random symbol = new Random();
        String specialSymbol = "~№;:.,?![]{}<>()@#$%^&_=*+_/|'";

        String randomString = "";
        for (int i = 0; i < sumSymbol; i++) {
            char randomSymbol = specialSymbol.charAt(symbol.nextInt(specialSymbol.length()));
            randomString = randomString + randomSymbol;
        }
        return randomString;
    }

    public static String generateInvalidOwnerNumeric(int lengthName, int lengthSurname) {
        return generateInvalidNumericData(lengthName) + " " + generateInvalidNumericData(lengthSurname);
    }

    public static String generateInvalidOwnerCyrillic(int lengthName, int lengthSurname) {
        return generateInvalidCyrillicData(lengthName) + " " + generateInvalidCyrillicData(lengthSurname);
    }

    public static String generateInvalidOwnerSymbol(int lengthName, int lengthSurname) {
        return generateInvalidSymbolData(lengthName) + " " + generateInvalidSymbolData(lengthSurname);
    }

    public static String generateInvalidMonth() {
        int minMonth = 14;
        int maxMonth = 99;
        int month = (int) (Math.random() * (maxMonth - minMonth + 1)) + minMonth;
        return Integer.toString(month);
    }

    public static String generateInvalidYear() {
        int minYear = Integer.parseInt(LocalDate.now().plusYears(7).format(DateTimeFormatter.ofPattern("yy")));
        int maxYear = 99;
        int year = (int) (Math.random() * (maxYear - minYear + 1)) + minYear;
        return Integer.toString(year);
    }

}
