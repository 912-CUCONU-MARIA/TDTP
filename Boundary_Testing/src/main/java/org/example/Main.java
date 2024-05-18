package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Main {

    public static void main(String[] args){
        new SignUpTest();
    }

//    public static void main(String[] args) {
//        WebDriver driver = new EdgeDriver();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//        try {
//            driver.get("https://www.selenium.dev/selenium/web/web-form.html");
//
//            sleep(2000);
//
//            WebElement textBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("my-text")));
//
//            sleep(2000);
//
//            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button")));
//
//            sleep(2000);
//
//            textBox.sendKeys("Selenium");
//
//            sleep(2000);
//
//            submitButton.click();
//
//            sleep(2000);
//
//            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
//            System.out.println("Message: " + message.getText());
//        } finally {
//            driver.quit();
//        }
//    }

    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace(); // This catch block handles any interruption during the sleep period
        }
    }

}

