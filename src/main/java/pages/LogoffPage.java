package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LogoffPage {

    //normal POM
    private WebDriver driver;

    private By sucessMessage = By.xpath("//div/h1[text()='Logged In Successfully']");

    public LogoffPage(WebDriver driver) {
        this.driver = driver;
    }

    public void verifyUserIsLoggedIn() {
        boolean isDisplayed = driver.findElement(sucessMessage).isDisplayed();
//        System.out.println("print flag : " + isDisplayed);
        if (!isDisplayed) {
            throw new AssertionError("Logoff header not displayed. Login may have failed.");
        }
    }
}
