package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverManager {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            String expectedVersion = PropertyFileReader.getProperty("expectedDriverVersion");

            try {
                // Force WebDriverManager to use expected version
                WebDriverManager.chromedriver()
                        .driverVersion(expectedVersion)
                        .setup();
            } catch (Exception e) {
                throw new RuntimeException("Failed to setup ChromeDriver with version: " + expectedVersion, e);
            }
//            String path = WebDriverManager.chromedriver().getDownloadedDriverPath();
            String actualVersion = WebDriverManager.chromedriver().getDownloadedDriverVersion();

            if (actualVersion == null || !actualVersion.startsWith(expectedVersion)) {
                throw new RuntimeException("ChromeDriver version mismatch: expected " + expectedVersion + ", but got " + actualVersion);
            }

            driver = new ChromeDriver();
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
