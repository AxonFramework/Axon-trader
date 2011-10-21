/*
 * Copyright (c) 2010. Gridshore
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private static final String SERVER = "http://axon-trader.cloudfoundry.com/";

    public static void main(String[] args) {

        WebDriver driver = new HtmlUnitDriver();

        LoginPage login = new LoginPage(driver);
        CompaniesPage companies = new CompaniesPage(driver);
        login.login("buyer1", "buyer1");

        Random randomFactory = new Random();
        String[] orderActions = {"Buy »", "Sell »"};

        long start = new Date().getTime();
        for (int i = 0; i < 1000; i++) {
            placeOrder(driver, companies, randomFactory, orderActions);
        }

        long end = new Date().getTime();
        System.out.println("Total milli seconds : " + (end - start));
    }

    private static void placeOrder(WebDriver driver, CompaniesPage companies, Random randomFactory, String[] orderActions) {
        long start = new Date().getTime();
        int randomCompany = randomFactory.nextInt(3);
        String randomOrderAction = orderActions[randomFactory.nextInt(2)];
        int randomPrice = randomFactory.nextInt(500) + 1;
        int randomAmount = randomFactory.nextInt(50) + 1;


        companies.selectCompany(randomCompany);

        // company details
        driver.findElement(By.linkText(randomOrderAction)).click();

        // buy screen
        driver.findElement(By.id("itemPrice")).sendKeys(String.valueOf(randomPrice));
        driver.findElement(By.id("tradeCount")).sendKeys(String.valueOf(randomAmount));
        driver.findElement(By.name("submit")).submit();
        long duration = new Date().getTime() - start;
        System.out.println("order : (" + duration + ") "
                + randomOrderAction + " " + randomCompany + " " + randomAmount + " " + randomPrice);
    }


    private static class CompaniesPage {
        private WebDriver driver;

        private CompaniesPage(WebDriver driver) {
            this.driver = driver;
        }

        public void selectCompany(int itemToSelect) {
            driver.get(SERVER + "company/");
            List<WebElement> elements = driver.findElements(By.xpath("//table[@id='available-stock']/tbody/tr"));
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
