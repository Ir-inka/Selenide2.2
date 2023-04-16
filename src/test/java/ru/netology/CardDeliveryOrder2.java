package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.String.format;

public class CardDeliveryOrder2 {
    public LocalDate dateToday = LocalDate.now();                                   // Дата сегодня


    @BeforeEach
    void setUp() {

        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @Test
    void dropdownAndCalendarTest() {                                                                                   // Заполнение полей
        $("[data-test-id =city] input").setValue("Мо");
        $$(".menu-item .menu-item__control").find(exactText("Москва")).click();
        $("[data-test-id=date] input").doubleClick();
        LocalDate deliveryDate = LocalDate.now().plusWeeks(1);                                                // На неделю вперед
        String date = deliveryDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));                                   // Формат даты доставки
        if (deliveryDate.getMonthValue() + dateToday.getMonthValue() == 1) {
            $("[data-step='1']").click();
            $$("td.calendar__day").find(exactText(date)).click();
        }
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("+79876543210");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id='notification']").should(text("Встреча успешно забронирована на " + date), Duration.ofSeconds(15)).shouldBe(visible);
    }

    @Test
    void showDateNextMonth() {                                                                                   // Заполнение полей
        $("[data-test-id =city] input").setValue("Мо");
        $$(".menu-item .menu-item__control").find(exactText("Москва")).click();
        $("[data-test-id=date] input").doubleClick();
        LocalDate deliveryDate = LocalDate.now().plusWeeks(4);                                                // На 4 недели вперед
        String date = deliveryDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));                                   // Формат даты доставки
        if (deliveryDate.getMonthValue() + dateToday.getMonthValue() == 1) {
            $("[data-step='1']").click();
            $$("td.calendar__day").find(exactText(date)).click();
        }
        $("[data-test-id=date] input").sendKeys(format(date));
        $("[data-test-id=name] input").setValue("Осипов Василий");
        $("[data-test-id=phone] input").setValue("+79876543210");
        $("[data-test-id=agreement]").click();
        $x("//button//span[contains(text(),'Забронировать')]").click();
        $("[data-test-id='notification']").should(text("Встреча успешно забронирована на " + date), Duration.ofSeconds(15)).shouldBe(visible);
    }


}

