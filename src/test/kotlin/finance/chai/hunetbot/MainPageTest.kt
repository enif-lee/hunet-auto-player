package finance.chai.hunetbot

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.HoverOptions
import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.logevents.SelenideLogger
import io.qameta.allure.selenide.AllureSelenide
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.chrome.ChromeOptions
import java.time.Duration


class MainPageTest {

    companion object {
        @BeforeAll
        fun setUpAll() {
//            Configuration.browserSize = "1280x800"
            SelenideLogger.addListener("allure", AllureSelenide())
        }
    }

    @BeforeEach
    fun setUp() {
        Configuration.headless = false
        Configuration.browserCapabilities = ChromeOptions().addArguments("--mute-audio")

        open("https://portone.hunet.co.kr/Home")
    }

    @Test
    fun search() {

        element("input[id='userId']").setValue("portone-<name>")
        element("input[id='userPw']").sendKeys("<password>")
        element("button[class='btn-login']").click()
        val element = element("span[class='w60']")
        element.shouldBe(visible)
        element.click()
        switchTo().window(1)
        element("#content > div.main-info-box > a").click()

        switchTo().window(2)
        Thread.sleep(1000)

        var lastTitle = ""


        while (true) {
            Thread.sleep(1000)
            try {
                switchTo().alert(Duration.ofMillis(100)).accept()
                println("Done and go next video")
            } catch (e: Throwable) {
            }

            try {
                switchTo().window(2)
                try {
                    switchTo().frame("main")
                } catch (e: Throwable) {
                }

                val title = element("#sangsang-player-header > table > tbody > tr > td.subject > h1 > span").innerText()
                val elapsed = element("#video_controlbar_elapsed").innerText()
                val duration = element("#video_controlbar_duration").innerText()

                if (lastTitle != title) {
                    element("#video_display").hover(HoverOptions.withOffset(100, 100))
                    element("#video_controlbar > span.jwgroup.jwright > span.jwmute > span:nth-child(2) > button").click()
                    lastTitle = title
                }

                println("$title - $elapsed : $duration")
            } catch (e: Throwable) {
            }

            switchTo().window(2)
        }
    }
}
