package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverManager {

    private static WebDriver driver;

    public static WebDriver getDriver() {

        if (driver == null) {
            String expectedVersion = null;
            String actualVersion;
            try {

                expectedVersion = PropertyFileReader.getProperty("expectedDriverVersion");
                WebDriverManager wdm = WebDriverManager.chromedriver();
                wdm.setup();

                actualVersion = wdm.getDownloadedDriverVersion();
                System.out.println("actual chrome driver version : " + actualVersion);

            } catch (Exception e) {
                throw new RuntimeException("Failed to setup ChromeDriver with version: " + expectedVersion, e);
            }

            if (actualVersion == null || !actualVersion.startsWith(expectedVersion)) {
                throw new RuntimeException("ChromeDriver version mismatch: expected " + expectedVersion + ", but got " + actualVersion);
            }

            ChromeOptions options = new ChromeOptions();
            if (PropertyFileReader.getProperty("isHeadless").equalsIgnoreCase("true")){
                options.addArguments("--headless=new");
            }

            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

}
