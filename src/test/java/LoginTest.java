import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.LogoffPage;
import utils.DriverManager;
import utils.PropertyFileReader;

public class LoginTest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    @BeforeSuite
    @Parameters("env")
    public void setupSuite(String env) {
        PropertyFileReader.loadProperties(env);
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    @BeforeMethod
    public void setup() {
        driver = DriverManager.getDriver();
        driver.get(PropertyFileReader.getProperty("url"));
    }

    @Test
    public void testLogin() {
        test = extent.createTest("Login Test");
        try {
            LoginPage loginPage = new LoginPage(driver);
            LogoffPage logoffPage = loginPage.login(PropertyFileReader.getProperty("username"), PropertyFileReader.getProperty("password"));
            logoffPage.verifyUserIsLoggedIn();
            System.out.println("Login test passed.");
            test.pass("Login test passed.");
        } catch (Exception e) {
            test.fail("Login test failed: " + e.getMessage());
            Assert.fail("Login test failed: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }

    @AfterSuite
    public void tearDownSuite() {
        extent.flush();
    }
}
