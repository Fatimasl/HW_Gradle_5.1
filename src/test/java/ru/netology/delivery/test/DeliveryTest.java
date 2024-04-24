package ru.netology.delivery.test;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {
    private WebDriver driver;

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting(){
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        //планируем начальную встречу
        $("[data-test-id='city'] input").setValue(validUser.getCity());

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);

        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(".grid-row button").click();

//        проверка только для начальной встречи
//        $("[data-test-id='success-notification']").shouldBe(visible);
//        $("[data-test-id='success-notification'] .notification__content").
//                shouldHave(exactText("Встреча успешно запланирована на "+firstMeetingDate));

        //перепланируем встречу для того же пользователя
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $(".grid-row button").click();

//        проверка предложения перепланировать встречу
//        $("[data-test-id='replan-notification']").shouldBe(visible);
//        $("[data-test-id='replan-notification'] .notification__content").
//                shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));

        //проверка успешного перепланирования. 1 тест = 1 проверка
        $$("[data-test-id='replan-notification'] button").find(exactText("Перепланировать")).click();
        $("[data-test-id='success-notification']").shouldBe(visible);
        $("[data-test-id='success-notification'] .notification__content").
                shouldHave(exactText("Встреча успешно запланирована на "+secondMeetingDate));

        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        // имени и номера телефона без создания пользователя в методе generateUser(String locale) в датагенераторе

    }
}
