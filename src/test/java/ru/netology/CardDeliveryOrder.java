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

public class CardDeliveryOrder {
    public String date = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")); // Бронирование встречи через 4 дня от сегодня


    @BeforeEach
    void setUp() {

        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @Test
    void filledFieldsValid() {                                                                // Поля заполнены

        $("[data-test-id =city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("+79876543210");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id='notification']").should(Condition.text("Встреча успешно забронирована на " + date), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Test
    void cityNotListed() {                                                        // Город не входит в один из административных центров субъектов РФ

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
    void surnameAndNameInLatin() {                                                               // Фамилия и Имя на латинице

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
    void cityInLatin() {                                                                             // Город на латинице

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
    void emptyDateField() {                                             // Пустое поле дата

        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("+79876543210");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id=date] .input__sub").shouldBe(Condition.text("Неверно введена дата"));
    }

    @Test
    void emptyFieldCity() {                                                              // пустое поле Город

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
    void nameSurnameNotFilled() {                                                    // Пустое поле Фамилия и Имя

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
    void withoutPhone() {                                                          // Пустое поле номера телефона

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
    void phoneNumberSmaller() {                                                          // В номере меньше цифр

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
    void phoneMoreNumbers() {                                                          // В номере больше цифр

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
    void phoneInLatin() {                                                          // Номер указан латиницей

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
    void phoneInCyrillic() {                                                          // Номер указан кириллицей

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
    void doNotTick() {                                                                // Галочку не поставим

        $("[data-test-id =city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("+79876543210");
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $(".checkbox__text").shouldBe(Condition.text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

}
