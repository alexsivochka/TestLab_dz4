package admin;

import admin.listeners.EventHandler;
import admin.pages.ProductPage;
import admin.pages.LoginPage;
import admin.pages.MainPage;
import admin.pages.ShopPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

public class TestRunner {

    EventFiringWebDriver driver;
    LoginPage loginPage;
    MainPage mainPage;
    ProductPage productPage;
    ShopPage shopPage;

    String randomIndex = RandomStringUtils.randomNumeric(3);
    String productName = "Sivochka".concat(randomIndex);
    String productCount = Random.getRandomCount();
    String productPrice = Random.getRandomPrice();

    @BeforeClass
    @Parameters("browser")
    public EventFiringWebDriver getConfiguredDriver(String browser) {
        if (browser.equals("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new EventFiringWebDriver(new ChromeDriver());
        }
        if (browser.equals("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new EventFiringWebDriver(new FirefoxDriver());
        }
        if (browser.equals("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EventFiringWebDriver(new EdgeDriver());
        }
        driver.manage().deleteAllCookies();
        driver.register(new EventHandler());
        driver.manage().window().maximize();
        driver.navigate().to("http://prestashop-automation.qatestlab.com.ua/admin147ajyvk0/");
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60,TimeUnit.SECONDS);
        return driver;
    }

    @DataProvider(name = "Authentication")
    public static Object[][] credentials() {
        return new Object[][] { { "webinar.test@gmail.com", "Xcg7299bnSmMuRLp9ITw" }};
    }

    @Test(priority = 1,
        description = "Авторизация на сайте",
        dataProvider = "Authentication")
    public void login(String login, String Password){
        loginPage = new LoginPage(driver);
        loginPage.login(login, Password);
        mainPage = new MainPage(driver);
        Assert.assertTrue(mainPage.checkLogoDisplay());
    }

    @Test(priority = 2,
            dependsOnMethods = "login",
            description = "Создание нового продукта")
    public void createNewProduct() throws InterruptedException {
        mainPage.goToProductsPage();
        productPage = new ProductPage(driver);
        productPage.addProduct(productName, productCount, productPrice);
    }

    @Test(priority = 3,
    dependsOnMethods = "createNewProduct",
    description = "Отображение созданного продукта в магазине")
    public void checkProductView() throws InterruptedException {
        driver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
        shopPage = new ShopPage(driver);
        shopPage.openAllProducts().checkNewProductPresent(productName);

        shopPage.openProductDetails(productName);
        shopPage.getProductDetails();
        Assert.assertEquals(shopPage.getProductName(), this.productName);
        Assert.assertEquals(shopPage.getProductPrice(), this.productPrice);
        Assert.assertEquals(shopPage.getProductCount(), this.productCount);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
