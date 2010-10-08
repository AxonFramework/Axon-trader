package org.axonframework.samples.trader.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author Jettro Coenradie
 */
public class BasicWebDriverTest {
    private static final String SERVER = "http://localhost:8080/";

    public static void main(String[] args) {

        WebDriver driver = new HtmlUnitDriver();

        LoginPage login = new LoginPage(driver);
        TradeItemsPage tradeItems = new TradeItemsPage(driver);
        login.login("buyer1", "buyer1");

        Random randomFactory = new Random();
        String[] orderActions = {"buy","sell"};

        long start = new Date().getTime();
        for (int i=0; i < 1000; i++) {
            placeOrder(driver, tradeItems, randomFactory, orderActions);
        }

        long end = new Date().getTime();
        System.out.println("Total milli seconds : " + (end-start));
    }

    private static void placeOrder(WebDriver driver, TradeItemsPage tradeItems, Random randomFactory, String[] orderActions) {
        long start = new Date().getTime();
        int randomTradeItem = randomFactory.nextInt(1000);
        String randomOrderAction = orderActions[randomFactory.nextInt(2)];
        int randomPrice = randomFactory.nextInt(500) + 1;
        int randomAmount = randomFactory.nextInt(50) + 1;


        tradeItems.selectTradeItem(randomTradeItem);

        // trade item details
        driver.findElement(By.linkText(randomOrderAction)).click();

        // buy screen
        driver.findElement(By.id("itemPrice")).sendKeys(String.valueOf(randomPrice));
        driver.findElement(By.id("tradeCount")).sendKeys(String.valueOf(randomAmount));
        driver.findElement(By.name("submit")).submit();
        long duration = new Date().getTime() - start;
        System.out.println("order : (" + duration + ") "
                + randomOrderAction + " " + randomTradeItem + " " + randomAmount + " " + randomPrice);
    }


    private static class TradeItemsPage {
        private WebDriver driver;

        private TradeItemsPage(WebDriver driver) {
            this.driver = driver;
        }

        public void selectTradeItem(int itemToSelect) {
            driver.get(SERVER + "tradeitem/");
            List<WebElement> elements = driver.findElements(By.xpath("//table[@class='hor-minimalist-b']/tbody/tr"));
            WebElement webElement = elements.get(itemToSelect);
            webElement.findElement(By.tagName("a")).click();
        }
    }

    private static class LoginPage {
        private WebDriver driver;

        public LoginPage(WebDriver driver) {
            this.driver = driver;
        }

        public void login(String username, String password) {
            driver.get(SERVER + "login.jsp");
            driver.findElement(By.name("j_username")).sendKeys("buyer1");
            driver.findElement(By.name("j_password")).sendKeys("buyer1");
            driver.findElement(By.name("submit")).submit();
        }
    }
}
