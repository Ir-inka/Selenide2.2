package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;
import static java.lang.String.format;
import static java.lang.String.valueOf;

public class CardDeliveryOrder {
    public String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }
    //public String date = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")); // Бронирование встречи через 4 дня от сегодня


    @BeforeEach
    void setUp() {

        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @Test
    void filledFieldsValid() {                                                                       // Поля заполнены

        String date = generateDate(4,"dd.MM.yyyy");

        $("[data-test-id =city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(valueOf(date));
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("+79876543210");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id='notification']").should(Condition.text("Успешно! Встреча успешно забронирована на " + date), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }
    @Test
    void cityNotListed() {

        String date = generateDate(4,"dd.MM.yyyy");                                   // Город не входит в один из административных центров субъектов РФ

        $("[data-test-id =city] input").setValue("Галич");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("+79876543210");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id=city] .input__sub").shouldBe(Condition.text("Доставка в выбранный город недоступна"));
    }

    @Test
    void surnameAndNameInLatin() {

        String date = generateDate(4,"dd.MM.yyyy");                                                    // Фамилия и Имя на латинице

        $("[data-test-id =city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue("Osipov Vasiliy");
        $("[data-test-id=phone] input").setValue("+79876543210");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id=name] .input__sub").shouldBe(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void cityInLatin() {

        String date = generateDate(4,"dd.MM.yyyy");                                                     // Город на латинице

        $("[data-test-id =city] input").setValue("Moscow");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("+79876543210");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id=city] .input__sub").shouldBe(Condition.text("Доставка в выбранный город недоступна"));
    }

    @Test
    void emptyDateField() {                                                                        // Пустое поле дата

        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("+79876543210");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id=date] .input__sub").shouldBe(Condition.text("Неверно введена дата"));
    }

    @Test
    void emptyFieldCity() {

        String date = generateDate(4,"dd.MM.yyyy");                                                      // пустое поле Город

        $("[data-test-id =city] input").setValue(" ");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("+79876543210");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id=city] .input__sub").shouldBe(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    void nameSurnameNotFilled() {

        String date = generateDate(4,"dd.MM.yyyy");                                                           // Пустое поле Фамилия и Имя

        $("[data-test-id =city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue(" ");
        $("[data-test-id=phone] input").setValue("+79876543210");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id=name] .input__sub").shouldBe(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    void withoutPhone() {

        String date = generateDate(4,"dd.MM.yyyy");                                                // Пустое поле номера телефона

        $("[data-test-id =city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue(" ");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id=phone] .input__sub").shouldBe(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    void phoneNumberSmaller() {

        String date = generateDate(4,"dd.MM.yyyy");                                                        // В номере меньше цифр

        $("[data-test-id =city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("+7987654321");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id=phone] .input__sub").shouldBe(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void phoneMoreNumbers() {

        String date = generateDate(4,"dd.MM.yyyy");                                                        // В номере больше цифр

        $("[data-test-id =city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("+798765432101");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id=phone] .input__sub").shouldBe(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void phoneInLatin() {

        String date = generateDate(4,"dd.MM.yyyy");                                                              // Номер указан латиницей

        $("[data-test-id =city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("phone");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id=phone] .input__sub").shouldBe(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void phoneInCyrillic() {

        String date = generateDate(4,"dd.MM.yyyy");                                                     // Номер указан кириллицей

        $("[data-test-id =city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("телефон");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id=phone] .input__sub").shouldBe(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }


    @Test
    void doNotTick() {

        String date = generateDate(4,"dd.MM.yyyy");                                                      // Галочку не поставим

        $("[data-test-id =city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("+79876543210");
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $(".checkbox__text").shouldBe(Condition.text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

}
