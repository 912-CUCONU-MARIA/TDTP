package org.example;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;
public class SignUpTest {
    WebElement firstNameField;
    WebElement lastNameField;
    WebElement emailField;
    WebElement passwordField;
    WebElement confirmPasswordField;
    WebElement createAccountButton;
    String validFirstName = "A".repeat(51);
    String validLastName = "A".repeat(50);
    String validEmail="test+" + UUID.randomUUID() + "@example.com";
    String validPassword="validPassword123!";
    String validConfirmPassword="validPassword123!";

    public SignUpTest(){
        allValidFieldsTest();
        testInvalidEmail();
    }

    private void input(String firstName, String lastName, String email, String password, String confirmPassword) {
        firstNameField.sendKeys(firstName);
        lastNameField.sendKeys(lastName);
        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        confirmPasswordField.sendKeys(confirmPassword);
    }

    private void init(WebDriver driver, WebDriverWait wait) {
        passCookieConsent(driver, wait);
        Main.sleep(5*1000);
        initFindFieldsByElement(driver);
    }

    private void initFindFieldsByElement(WebDriver driver) {
        firstNameField = driver.findElement(By.id("firstname"));
        lastNameField = driver.findElement(By.id("lastname"));
        emailField = driver.findElement(By.id("email_address"));
        passwordField = driver.findElement(By.id("password"));
        confirmPasswordField = driver.findElement(By.id("password-confirmation"));
        createAccountButton = driver.findElement(By.cssSelector("button.action.submit.primary"));
    }

    private void passCookieConsent(WebDriver driver, WebDriverWait wait) {
        driver.get("https://magento.softwaretestingboard.com/customer/account/create/");
        WebElement cookiesConsentButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("html/body/div[3]/div[2]/div[1]/div[2]/div[2]/button[1]/p")));
        cookiesConsentButton.click();
    }

    private boolean isLoginSuccessful(WebDriverWait wait) {
        boolean isLoginSuccess;
        try {
            WebElement greetWelcome = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class, 'logged-in') and contains(text(), 'Welcome,')]")));
            isLoginSuccess = true;
            System.out.println("Login successful: " + greetWelcome.getText());
        } catch (org.openqa.selenium.TimeoutException e) {
            // If the wait times out, login is unsuccessful
            isLoginSuccess = false;
            System.out.println("Login failed");
        }
        return isLoginSuccess;
    }

    public void allValidFieldsTest(){
        WebDriver driver = new EdgeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            init(driver, wait);

            input(validFirstName,validLastName,validEmail, validPassword, validConfirmPassword);

            createAccountButton.click();

            isLoginSuccessful(wait);

        } finally {
            driver.quit();
        }
    }

    public void testInvalidEmail() {
        WebDriver driver = new EdgeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            init(driver, wait);
            String email="test+" + UUID.randomUUID()+"@example.co";
            System.out.println(email);
            input(validFirstName,validLastName,email, validPassword, validConfirmPassword);

            createAccountButton.click();
            Main.sleep(5000);
            isLoginSuccessful(wait);

        } finally {
            driver.quit();
        }
    }



}
