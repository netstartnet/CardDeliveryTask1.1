package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

    class DeliveryTest {

        @BeforeEach
        void setup() {
            Configuration.holdBrowserOpen = true;
        }

        @Test
        @DisplayName("Should successful plan and replan meeting")
        void shouldSuccessfulPlanAndReplanMeeting() {
            var validUser = DataGenerator.Registration.generateUser("ru");
            var daysToAddForFirstMeeting = 5;
            var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
            var daysToAddForSecondMeeting = 11;
            var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
            open("http://localhost:9999/");
            $("[data-test-id='city'] input").setValue(validUser.getCity());
            $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE));
            $x("//input[@placeholder='Дата встречи']").setValue(firstMeetingDate);
            $x("//input[@name='name']").setValue(validUser.getName());
            $x("//input[@name='phone']").setValue(validUser.getPhone());
            $("[data-test-id='agreement']").click();
            $x("//span[@class='button__text']").click();
            $x("//*[contains(text(),'Успешно!')]").shouldBe(Condition.visible, Duration.ofSeconds(15));
            $("[class='notification__content']").shouldHave(Condition.exactText("Встреча успешно запланирована на "
                    + firstMeetingDate));
            $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
            $x("//input[@placeholder='Дата встречи']").setValue(secondMeetingDate);
            $x("//span[@class='button__text']").click();
            $x("//*[contains(text(),'У вас уже запланирована встреча на другую дату. " +
                    "Перепланировать?')]").shouldBe(Condition.visible);
            $x("//button[@type='button']/span[.='Перепланировать']").click();
            $("[data-test-id='success-notification']").shouldHave(Condition.exactText("Успешно! Встреча успешно запланирована на "
                    + secondMeetingDate));

        }
    }