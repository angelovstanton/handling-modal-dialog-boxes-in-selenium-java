import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Wait;

import java.time.Duration;
public class StaleElementExceptionsTests {
    private final int WAIT_FOR_ELEMENT_TIMEOUT = 30;
    private ChromeDriver driver;
    private WebDriverWait webDriverWait;

    @BeforeAll
    public static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_FOR_ELEMENT_TIMEOUT));
    }

    @Test
    public void createStaleElementReferenceException() {
        driver.navigate().to("https://www.lambdatest.com/selenium-playground/");

        WebElement pageLink = driver.findElement(By.linkText("Table Data Search"));
        pageLink.click();
        WebElement filterByField = driver.findElement(By.id("task-table-filter"));

        filterByField.sendKeys("in progress");

        driver.navigate().back();

        pageLink.click();
        filterByField.sendKeys("completed");
    }

    @Test
    public void test1_ReInitializeWebElementToHandle() {
        driver.navigate().to("https://www.lambdatest.com/selenium-playground/");

        WebElement pageLink = driver.findElement(By.linkText("Table Data Search"));
        pageLink.click();
        WebElement filterByField = driver.findElement(By.id("task-table-filter"));

        filterByField.sendKeys("in progress");
        driver.navigate().back();
        pageLink = driver.findElement(By.linkText("Table Data Search"));
        pageLink.click();
        filterByField = driver.findElement(By.id("task-table-filter"));
        filterByField.sendKeys("completed");
    }

    @Test
    public void test2_WhileLoopToHandle_SERE() {
        driver.navigate().to("https://www.lambdatest.com/selenium-playground/");

        WebElement pageLink = driver.findElement(By.linkText("Table Data Search"));
        pageLink.click();
        By filterByField = By.id("task-table-filter");

        Wait wait = new Wait(driver);
        var input = wait.retryWhileLoop(filterByField);
        input.sendKeys("in progress");

        driver.navigate().back();
        pageLink = driver.findElement(By.linkText("Table Data Search"));
        pageLink.click();

        input = wait.retryWhileLoop(filterByField);
        input.sendKeys("completed");
    }

    @Test
    public void test3_ForLoopToHandle_SERE() {
        driver.navigate().to("https://www.lambdatest.com/selenium-playground/");

        WebElement pageLink = driver.findElement(By.linkText("Table Data Search"));
        pageLink.click();
        By filterByField = By.id("task-table-filter");

        Wait wait = new Wait(driver);
        var input = wait.retryWhileLoop(filterByField);
        input.sendKeys("in progress");

        driver.navigate().back();
        pageLink = driver.findElement(By.linkText("Table Data Search"));
        pageLink.click();

        input = wait.retryWhileLoop(filterByField);
        input.sendKeys("completed");
    }

    @Test
    public void test4_chainExpectedConditionsToHandle() {
        driver.navigate().to("https://www.lambdatest.com/selenium-playground/");

        WebElement pageLink = driver.findElement(By.linkText("Table Data Search"));
        pageLink.click();
        By filterByField = By.id("task-table-filter");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        var filter = wait.until(ExpectedConditions.refreshed(
                ExpectedConditions.presenceOfElementLocated(filterByField)));
        filter.sendKeys("in progress");

        driver.navigate().back();
        pageLink = driver.findElement(By.linkText("Table Data Search"));
        pageLink.click();

        filter = wait.until(ExpectedConditions.refreshed(
                ExpectedConditions.presenceOfElementLocated(filterByField)));
        filter.sendKeys("completed");
    }

    public void waitForAjax() {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor)driver;
        webDriverWait.until(d -> (Boolean) javascriptExecutor.executeScript("return window.jQuery != undefined && jQuery.active == 0"));
    }

    public void waitUntilPageLoadsCompletely() {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor)driver;
        webDriverWait.until(d -> javascriptExecutor.executeScript("return document.readyState").toString().equals("complete"));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}