import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.HoverOptions
import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.logevents.SelenideLogger
import io.qameta.allure.selenide.AllureSelenide
import org.openqa.selenium.chrome.ChromeOptions
import java.time.Duration


fun main() {
    print("USER_ID : ")
    val id = readlnOrNull() ?: error("아이디 입력 오류")

    print("PWD : ")
    val pwd = System.console()?.readPassword()?.toString() ?: readlnOrNull() ?: error("패스워드 입력 오류")


    SelenideLogger.addListener("allure", AllureSelenide())
    Configuration.headless = true
    Configuration.browserCapabilities = ChromeOptions().addArguments("--mute-audio")
    open("https://portone.hunet.co.kr/Home")

    element("input[id='userId']").setValue(id)
    element("input[id='userPw']").sendKeys(pwd)
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

            webdriver().driver().executeJavaScript<String>("document.querySelector('video').playbackRate = 2")

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