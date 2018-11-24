package admin.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class ShopPage {

    By searchInput = By.xpath("//input[@name='s']");
    String xpath = "//a[contains(text(),'%s')]";

    By priceDetails = By.xpath("//span[@itemprop='price']");
    By nameDetails = By.xpath("//h1[@itemprop='name']");
    By countDetails = By.xpath("//div[@class='product-quantities']/span");

//    By detailsForm = By.xpath("//div[@class='modal-content']");
    By detailsForm = By.xpath("//h1[@itemprop='name']");
    By allProductLink = By.xpath("//a[contains(@class,'all-product')]");
    By products = By.id("products");
    By listProducts = By.xpath("//h1[@itemprop='name']/a");

    String productName;
    String productPrice;
    String productCount;

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductCount() {
        return productCount;
    }

    private final WebDriver driver;

    public ShopPage(WebDriver driver) {
        this.driver = driver;
    }

    public ShopPage openAllProducts() {
        new WebDriverWait(driver,15)
                .until(ExpectedConditions.elementToBeClickable(allProductLink));
        driver.findElement(allProductLink).click();
        new WebDriverWait(driver,15)
                .until(ExpectedConditions.visibilityOfElementLocated(products));
        return this;
    }

    public void checkNewProductPresent(String productName) {
        List<String> availableProducts = new ArrayList<>();
        List<WebElement> elements = driver.findElements(listProducts);
        for (int i = 0; i < elements.size(); i++) {
            availableProducts.add(elements.get(i).getText());
        }
        Assert.assertTrue(availableProducts.contains(productName));
    }

    public void openProductDetails(String productName) {
        String productXpath = String.format(xpath, productName);
        driver.findElement(By.xpath(productXpath)).click();
    }

    public void searchProduct(String productName) {
        driver.findElement(searchInput).sendKeys(productName);
        driver.findElement(searchInput).submit();
        String productXpath = String.format(xpath, productName);
        WebElement product = driver.findElement(By.xpath(productXpath));
        new WebDriverWait(driver,15)
                .until(ExpectedConditions.visibilityOf(product));
        Assert.assertTrue(product.isDisplayed(), "Товар не отображается на странице магазина");
    }

    public void getProductDetails() {
        new WebDriverWait(driver, 20)
                .until(ExpectedConditions.visibilityOfElementLocated(detailsForm));
        this.productName = driver.findElement(nameDetails).getAttribute("innerHTML");
        this.productPrice = driver.findElement(priceDetails).getAttribute("content");
        String stock = driver.findElement(countDetails).getAttribute("innerHTML");
        String []arr = stock.split("\\s+");
        this.productCount = arr[0];
    }

}
