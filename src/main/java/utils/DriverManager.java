package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DriverManager {

    private static WebDriver driver;

    public static WebDriver getDriver() {

        if (driver == null) {
            String os = System.getProperty("os.name").toLowerCase();
            String expectedVersion = null;
            String actualVersion = null;
            try {

                if (os.contains("win")){
                    expectedVersion = PropertyFileReader.getProperty("expectedWindowsDriverVersion");
                    // Force WebDriverManager to use expected version
                    WebDriverManager.chromedriver()
                            .driverVersion(expectedVersion)
                            .setup();

                    actualVersion = WebDriverManager.chromedriver().getDownloadedDriverVersion();
                }else if (os.contains("mac")){
                    // using downloaded driver because getDownloadedDriverVersion() returning null on mac
                    expectedVersion = PropertyFileReader.getProperty("expectedMacDriverVersion");
                    String driverPath = PropertyFileReader.getProperty("macChromeDriverPath");
                    if (driverPath == null || driverPath.isEmpty()) {
                        throw new RuntimeException("macChromeDriverPath not specified in properties file.");
                    }

                    System.setProperty("webdriver.chrome.driver", driverPath);
                    actualVersion = getChromeDriverVersion(driverPath);
                }else {
                    System.out.println(" We can handle accordingly if we need to : Not in scope as of now");
                }


            } catch (Exception e) {
                throw new RuntimeException("Failed to setup ChromeDriver with version: " + expectedVersion, e);
            }
//            String path = WebDriverManager.chromedriver().getDownloadedDriverPath();
//            String actualVersion = WebDriverManager.chromedriver().getDownloadedDriverVersion();
//
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

    private static String getChromeDriverVersion(String driverPath) {
        try {
            Process process = new ProcessBuilder(driverPath, "--version").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.readLine(); // Example: "ChromeDriver 120.0.6099.109"
            process.waitFor();
            if (output != null && output.toLowerCase().contains("chromedriver")) {
                return output.split(" ")[1];
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to determine ChromeDriver version from: " + driverPath, e);
        }
        return null;
    }
}
