package admin.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class ProductPage {

    By addProductButton = By.xpath("//a[contains(@id,'add')]");
    By productNameInput = By.id("form_step1_name_1");
    By productQuantityInput = By.id("form_step1_qty_0_shortcut");
    By productPriceInput = By.id("form_step1_price_shortcut");
    By activateProduct = By.cssSelector(".switch-input");
    By settingsPopup = By.id("growls");
    By popupMessage = By.className("growl-message");
    By closeSettingsPopup = By.cssSelector(".growl-close");

    private final WebDriver driver;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    public void addProduct(String name, String count, String price) throws InterruptedException {
        driver.findElement(addProductButton).click();
        driver.findElement(productNameInput).sendKeys(name);
        driver.findElement(productQuantityInput).clear();
        driver.findElement(productQuantityInput).sendKeys(count);
        driver.findElement(productPriceInput).clear();
        driver.findElement(productPriceInput).sendKeys(price);
        driver.findElement(activateProduct).click();
        checkSaveSettings();
        driver.findElement(productNameInput).submit();
        checkSaveSettings();
    }

    public void checkSaveSettings() {
        new WebDriverWait(driver,10)
                .withMessage("Произошла ошибка при сохранении настроек!!!")
                .until(ExpectedConditions.visibilityOfElementLocated(settingsPopup));
        String message = driver.findElement(popupMessage).getAttribute("innerHTML");
        Assert.assertEquals(message, "Настройки обновлены.");
        if(driver.findElement(settingsPopup).isDisplayed()){
            driver.findElement(closeSettingsPopup).click();
        }
    }
}
