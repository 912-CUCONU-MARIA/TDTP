import org.example.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

public class SignUpBoundaryTest {
    WebElement firstNameField;
    WebElement lastNameField;
    WebElement emailField;
    WebElement passwordField;
    WebElement confirmPasswordField;
    WebElement createAccountButton;
    String validFirstName = "A".repeat(51);
    String validLastName = "A".repeat(50);
    String validEmail = "test+" + UUID.randomUUID() + "@example.com";
    String validPassword = "validPassword123!";
    String validConfirmPassword = "validPassword123!";
    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    public void initData() {
        Main.sleep(5 * 1000);
        driver = new EdgeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        passCookieConsent(driver);
        initFindFieldsByElement(driver);
    }

    private void input(String firstName, String lastName, String email, String password, String confirmPassword) {
        firstNameField.sendKeys(firstName);
        lastNameField.sendKeys(lastName);
        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        confirmPasswordField.sendKeys(confirmPassword);
    }

    private void initFindFieldsByElement(WebDriver driver) {
        firstNameField = driver.findElement(By.id("firstname"));
        lastNameField = driver.findElement(By.id("lastname"));
        emailField = driver.findElement(By.id("email_address"));
        passwordField = driver.findElement(By.id("password"));
        confirmPasswordField = driver.findElement(By.id("password-confirmation"));
        createAccountButton = driver.findElement(By.cssSelector("button.action.submit.primary"));
    }

    private void passCookieConsent(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get("https://magento.softwaretestingboard.com/customer/account/create/");
//        WebElement cookiesConsentButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("html/body/div[3]/div[2]/div[1]/div[2]/div[2]/button[1]/p")));

        for (int i = 0; i < 5; i++) {
            try {
                WebElement cookiesConsentButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("html/body/div[3]/div[2]/div[1]/div[2]/div[2]/button[1]/p")));
                cookiesConsentButton.click();
                break;
            } catch (org.openqa.selenium.TimeoutException e) {
                if (i == 4) {
                    System.out.println("passCookieConsent timed out after 5 attempts");
                }
                throw new TimeoutException("passCookieConsent timed out after 5 attempts");
            }
        }

        //cookiesConsentButton.click();
    }

    private boolean isSignUpSuccessful(WebDriverWait wait) {
        boolean isSignUpSuccessful;
        try {
            WebElement greetWelcome = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class, 'logged-in') and contains(text(), 'Welcome,')]")));
            isSignUpSuccessful = true;
            System.out.println("Sign up successful: " + greetWelcome.getText());
        } catch (org.openqa.selenium.TimeoutException e) {
            // If the wait times out, login is unsuccessful
            isSignUpSuccessful = false;
            System.out.println("Sign up failed");
        }
        return isSignUpSuccessful;
    }

    private boolean isSignUpFieldsValid(WebDriverWait wait) {
        boolean isSignUpFieldsValid;
        try {
            WebElement existingAccountMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'There is already an account with this email address.')]")));
            isSignUpFieldsValid = true;
            System.out.println("Sign up fields valid: " + existingAccountMessage.getText());
        } catch (org.openqa.selenium.TimeoutException e) {
            // If the wait times out, login is unsuccessful
            isSignUpFieldsValid = false;
            System.out.println("Sign up fields invalid");
        }
        return isSignUpFieldsValid;
    }

    @Test
    public void testAllFieldsValid() {
        System.out.println("testAllFieldsValid");
        try {

            input(validFirstName, validLastName, validEmail, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    /*
    FirstName boundaries
    Variable firstName in string of length [1, 255] is tested:

    Input "" Expected Output: Invalid (Empty string)
    Input "A" Expected Output: Valid (Minimum valid length)
    Input "AA" Expected Output: Valid (Minimum valid length + 1)
    Input "A".repeat(254) Expected Output: Valid (Just inside the upper boundary)
    Input "A".repeat(255) Expected Output: Valid (At the upper boundary)
    Input "A".repeat(256) Expected Output: Invalid (Just outside the upper boundary)

    Extra test with really long values:
    Input "A".repeat(1000) Expected Output: Invalid (Far outside the upper boundary)
     */
    @Test
    public void testInvalidFirstNameEmptyString() {
        System.out.println("testInvalidFirstNameEmptyString");
        try {
            String firstName = "";
            System.out.println(firstName);
            input(firstName, validLastName, validEmail, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidFirstNameMinimumValidLength() {
        System.out.println("testInvalidFirstNameMinimumValidLength");
        try {
            String firstName = "A";
            System.out.println(firstName);
            input(firstName, validLastName, validEmail, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidFirstNameMinimumPlusOneValidLength() {
        System.out.println("testInvalidFirstNameMinimumPlusOneValidLength");
        try {
            String firstName = "AA";
            System.out.println(firstName);
            input(firstName, validLastName, validEmail, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidFirstNameJustInsideUpper() {
        System.out.println("testInvalidFirstNameJustInsideUpper");
        try {
            String firstName = "A".repeat(254);
            System.out.println(firstName);
            input(firstName, validLastName, validEmail, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidFirstNameMaximumValidLength() {
        System.out.println("testInvalidFirstNameMaximumValidLength");
        try {
            String firstName = "A".repeat(255);
            System.out.println(firstName);
            input(firstName, validLastName, validEmail, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidFirstNameJustOutsideUpper() {
        System.out.println("testInvalidFirstNameJustInsideUpper");
        try {
            String firstName = "A".repeat(256);
            System.out.println(firstName);
            input(firstName, validLastName, validEmail, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    /*
    LastName boundaries
    Variable lastName in string of length [1, 255] is tested:

    Input "" Expected Output: Invalid (Empty string)
    Input "A" Expected Output: Valid (Minimum valid length)
    Input "AA" Expected Output: Valid (Minimum valid length + 1)
    Input "A".repeat(254) Expected Output: Valid (Just inside the upper boundary)
    Input "A".repeat(255) Expected Output: Valid (At the upper boundary)
    Input "A".repeat(256) Expected Output: Invalid (Just outside the upper boundary)

    Extra test with really long values:
    Input "A".repeat(1000) Expected Output: Invalid (Far outside the upper boundary)
     */
    @Test
    public void testInvalidLastNameEmptyString() {
        System.out.println("testInvalidLastNameEmptyString");
        try {
            String lastName = "";
            System.out.println(lastName);
            input(validFirstName, lastName, validEmail, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidLastNameMinimumValidLength() {
        System.out.println("testInvalidLastNameMinimumValidLength");
        try {
            String lastName = "A";
            System.out.println(lastName);
            input(validFirstName, lastName, validEmail, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidLastNameMinimumPlusOneValidLength() {
        System.out.println("testInvalidLastNameMinimumPlusOneValidLength");
        try {
            String lastName = "AA";
            System.out.println(lastName);
            input(validFirstName, lastName, validEmail, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidLastNameJustInsideUpper() {
        System.out.println("testInvalidLastNameJustInsideUpper");
        try {
            String lastName = "A".repeat(254);
            System.out.println(lastName);
            input(validFirstName, lastName, validEmail, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidLastNameMaximumValidLength() {
        System.out.println("testInvalidLastNameMaximumValidLength");
        try {
            String lastName = "A".repeat(255);
            System.out.println(lastName);
            input(validFirstName, lastName, validEmail, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidLastNameJustOutsideUpper() {
        System.out.println("testInvalidLastNameJustInsideUpper");
        try {
            String lastName = "A".repeat(256);
            System.out.println(lastName);
            input(validFirstName, lastName, validEmail, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    /*
    Email boundaries
    Variable email in string of length [1, 255] is tested:

    Length Boundary
    Input "" Expected Output: Invalid (Empty string)
    Input "a@b.co" Expected Output: Valid (Shortest valid email)
    Input "aa@b.co" Expected Output: Valid (Shortest valid email)
    Input "a".repeat(1000) + "@example.com" Expected Output: Invalid (Far outside the upper boundary)

    Format Boundary
    Input "test@" Expected Output: Invalid (Local part only)
    Input "test@example" Expected Output: Invalid (No TLD)
    Input "test@.com" Expected Output: Invalid (Dot but no TLD)
    Input "test@example.co" Expected Output: Valid (Proper format)
    Input "test@example.c" Expected Output: Invalid (Incomplete TLD)
     */
    @Test
    public void testInvalidEmailEmptyString() {
        System.out.println("testInvalidEmailEmptyString");
        try {
            String email = "";
            System.out.println(email);
            input(validFirstName, validLastName, email, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }
    @Test
    public void testValidEmailBelowMinimumLength() {
        System.out.println("testValidEmailBelowMinimumLength");
        try {
            String email = "a@b.c";
            System.out.println(email);
            input(validFirstName, validLastName, email, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (!isSignUpFieldsValid(wait) || !isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }
    @Test
    public void testValidEmailMinimumLength() {
        System.out.println("testValidEmailMinimumLength");
        try {
            String email = "a@b.co";
            System.out.println(email);
            input(validFirstName, validLastName, email, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (isSignUpFieldsValid(wait) ||isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testValidEmailMinimumPlusOneLength() {
        System.out.println("testValidEmailMinimumPlusOneLength");
        try {
            String email = "aa@b.co";
            System.out.println(email);
            input(validFirstName, validLastName, email, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (isSignUpFieldsValid(wait) ||isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidEmailBelowUpper() {
        System.out.println("testInvalidEmailBelowUpper");
        try {
            String email = "d".repeat(63) + "@example.com";
            System.out.println(email);
            input(validFirstName, validLastName, email, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }
    @Test
    public void testInvalidEmailAtUpper() {
        System.out.println("testInvalidEmailAtUpper");
        try {
            String email = "d".repeat(64) + "@example.com";
            System.out.println(email);
            input(validFirstName, validLastName, email, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }
    @Test
    public void testInvalidEmailOutsideUpper() {
        System.out.println("testInvalidEmailOutsideUpper");
        try {
            String email = "d".repeat(65) + "@example.com";
            System.out.println(email);
            input(validFirstName, validLastName, email, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidEmailLocalPartOnly() {
        System.out.println("testInvalidEmailLocalPartOnly");
        try {
            String email = "test@";
            System.out.println(email);
            input(validFirstName, validLastName, email, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidEmailNoTLD() {
        System.out.println("testInvalidEmailNoTLD");
        try {
            String email = "test@example";
            System.out.println(email);
            input(validFirstName, validLastName, email, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidEmailDotButNoTLD() {
        System.out.println("testInvalidEmailDotButNoTLD");
        try {
            String email = "test@.com";
            System.out.println(email);
            input(validFirstName, validLastName, email, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testValidEmailProperFormat() {
        System.out.println("testValidEmailProperFormat");
        try {
            String email = "test"+UUID.randomUUID()+"@example.co";
            System.out.println(email);
            input(validFirstName, validLastName, email, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidEmailIncompleteTLD() {
        System.out.println("testInvalidEmailIncompleteTLD");
        try {
            String email = "test@example.c";
            System.out.println(email);
            input(validFirstName, validLastName, email, validPassword, validConfirmPassword);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    /*
    Password boundaries

    Length Boundary
    Input "" Expected Output: Invalid (Empty string)
    Input "Aa1Aa1!" Expected Output: Invalid (Below the minimum length)
    Input "Aa1!".repeat(2) Expected Output: Valid (At the minimum length)
    Input "A".repeat(20) Expected Output: Valid (Typical maximum length)
    Input "A".repeat(1000) Expected Output: Invalid (Far outside the upper boundary)

    Character Classes Boundary
    Input "aaa" Expected Output: Invalid (Only one character class)
    Input "aaA" Expected Output: Invalid (Two character classes)
    Input "aaA1" Expected Output: Invalid (Three character classes, but below minimum length)
    Input "Aa1!".repeat(2) Expected Output: Valid (Three character classes and minimum length)
    Input "aA1!aA1!a" Expected Output: Valid (Just over minimum valid length)
    Input "aA1! ".repeat(2) Expected Output: Valid (If leading/trailing spaces are ignored)
    Input "password" or "PASSWORD" or "12345678" Expected Output: Invalid (Missing required character classes)
     */

    @Test
    public void testInvalidPasswordEmptyString() {
        System.out.println("testInvalidPasswordEmptyString");
        try {
            String password = "";
            System.out.println(password);
            input(validFirstName, validLastName, validEmail, password, password);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidPasswordBelowMinimumLength() {
        System.out.println("testInvalidPasswordBelowMinimumLength");
        try {
            String password = "Aa1!Aa1";
            System.out.println(password);
            input(validFirstName, validLastName, validEmail, password, password);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testValidPasswordMinimumLength() {
        System.out.println("testValidPasswordMinimumLength");
        try {
            String password = "Aa1!".repeat(2);
            System.out.println(password);
            input(validFirstName, validLastName, validEmail, password, password);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testValidPasswordMinimumPlusOneLength() {
        System.out.println("testValidPasswordMinimumPlusOneLength");
        try {
            String password = "Aa1!".repeat(2) + "A";
            System.out.println(password);
            input(validFirstName, validLastName, validEmail, password, password);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidPasswordFarOutsideUpperBoundary() {
        System.out.println("testInvalidPasswordFarOutsideUpperBoundary");
        try {
            String password = "Aa1!".repeat(64)+"a";
            System.out.println(password);
            input(validFirstName, validLastName, validEmail, password, password);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidPasswordOnlyOneCharacterClass() {
        System.out.println("testInvalidPasswordOnlyOneCharacterClass");
        try {
            String password = "aaa";
            System.out.println(password);
            input(validFirstName, validLastName, validEmail, password, password);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidPasswordTwoCharacterClasses() {
        System.out.println("testInvalidPasswordTwoCharacterClasses");
        try {
            String password = "aaA";
            System.out.println(password);
            input(validFirstName, validLastName, validEmail, password, password);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidPasswordThreeCharacterClassesBelowMinimumLength() {
        System.out.println("testInvalidPasswordThreeCharacterClassesBelowMinimumLength");
        try {
            String password = "aaA1";
            System.out.println(password);
            input(validFirstName, validLastName, validEmail, password, password);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testValidPasswordThreeCharacterClassesMinimumLength() {
        System.out.println("testValidPasswordThreeCharacterClassesMinimumLength");
        try {
            String password = "Aa1!".repeat(2);
            System.out.println(password);
            input(validFirstName, validLastName, validEmail, password, password);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testValidPasswordThreeCharacterClassesJustOverMinimumValidLength() {
        System.out.println("testValidPasswordThreeCharacterClassesJustOverMinimumValidLength");
        try {
            String password = "aA1!aA1!a";
            System.out.println(password);
            input(validFirstName, validLastName, validEmail, password, password);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testValidPasswordIgnoreLeadingTrailingSpaces() {
        System.out.println("testValidPasswordIgnoreLeadingTrailingSpaces");
        try {
            String password = "aA1! aA1!";
            System.out.println(password);
            input(validFirstName, validLastName, validEmail, password, password);

            createAccountButton.click();
            assert (isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testInvalidPasswordMissingRequiredCharacterClasses() {
        System.out.println("testInvalidPassword_MissingRequiredCharacterClasses");
        try {
            String password = "password";
            System.out.println(password);
            input(validFirstName, validLastName, validEmail, password, password);

            createAccountButton.click();
            assert (!isSignUpSuccessful(wait));

        } finally {
            driver.quit();
        }
    }
/*
    ConfirmPassword boundaries

    Length Boundary
    Input "" Expected Output: Invalid (Empty string)
    Input "Aa1" Expected Output: Invalid (Below the minimum length)
    Input "Aa1!".repeat(2) Expected Output: Valid (At the minimum length)
    Input "A".repeat(20) Expected Output: Valid (Typical maximum length)
    Input "A".repeat(1000) Expected Output: Invalid (Far outside the upper boundary)

    Character Classes Boundary
    Input "aaa" Expected Output: Invalid (Only one character class)
    Input "aaA" Expected Output: Invalid (Two character classes)
    Input "aaA1" Expected Output: Invalid (Three character classes, but below minimum length)
    Input "Aa1!".repeat(2) Expected Output: Valid (Three character classes and minimum length)
    Input "aA1!aA1!a" Expected Output: Valid (Just over minimum valid length)
    Input "aA1! ".repeat(2) Expected Output: Valid (If leading/trailing spaces are ignored)
    Input "confirmPassword" or "PASSWORD" or "12345678" Expected Output: Invalid (Missing required character classes)
     */

//    @Test
//    public void testInvalidConfirmPasswordEmptyString() {
//        System.out.println("testInvalidConfirmPasswordEmptyString");
//        try {
//            String confirmPassword = "";
//            System.out.println(confirmPassword);
//            input(validFirstName, validLastName, validEmail, validPassword, confirmPassword);
//
//            createAccountButton.click();
//            assert (!isLoginSuccessful(wait));
//
//        } finally {
//            driver.quit();
//        }
//    }
//
//    @Test
//    public void testInvalidConfirmPasswordBelowMinimumLength() {
//        System.out.println("testInvalidConfirmPasswordBelowMinimumLength");
//        try {
//            String confirmPassword = "Aa1!Aa1";
//            System.out.println(confirmPassword);
//            input(validFirstName, validLastName, validEmail, validPassword, confirmPassword);
//
//            createAccountButton.click();
//            assert (!isLoginSuccessful(wait));
//
//        } finally {
//            driver.quit();
//        }
//    }
//
//    @Test
//    public void testValidConfirmPasswordMinimumLength() {
//        System.out.println("testValidConfirmPasswordMinimumLength");
//        try {
//            String confirmPassword = "Aa1!".repeat(2);
//            System.out.println(confirmPassword);
//            input(validFirstName, validLastName, validEmail, validPassword, confirmPassword);
//
//            createAccountButton.click();
//            assert (isLoginSuccessful(wait));
//
//        } finally {
//            driver.quit();
//        }
//    }
//
//    @Test
//    public void testValidConfirmPasswordMinimumPlusOneLength() {
//        System.out.println("testValidConfirmPasswordMinimumPlusOneLength");
//        try {
//            String confirmPassword = "Aa1!".repeat(2) + "A";
//            System.out.println(confirmPassword);
//            input(validFirstName, validLastName, validEmail, validPassword, confirmPassword);
//
//            createAccountButton.click();
//            assert (isLoginSuccessful(wait));
//
//        } finally {
//            driver.quit();
//        }
//    }
//
//    @Test
//    public void testInvalidConfirmPasswordFarOutsideUpperBoundary() {
//        System.out.println("testInvalidConfirmPasswordFarOutsideUpperBoundary");
//        try {
//            String confirmPassword = "A".repeat(1000);
//            System.out.println(confirmPassword);
//            input(validFirstName, validLastName, validEmail, validPassword, confirmPassword);
//
//            createAccountButton.click();
//            assert (!isLoginSuccessful(wait));
//
//        } finally {
//            driver.quit();
//        }
//    }
//
//    @Test
//    public void testInvalidConfirmPasswordOnlyOneCharacterClass() {
//        System.out.println("testInvalidConfirmPasswordOnlyOneCharacterClass");
//        try {
//            String confirmPassword = "aaa";
//            System.out.println(confirmPassword);
//            input(validFirstName, validLastName, validEmail, validPassword, confirmPassword);
//
//            createAccountButton.click();
//            assert (!isLoginSuccessful(wait));
//
//        } finally {
//            driver.quit();
//        }
//    }
//
//    @Test
//    public void testInvalidConfirmPasswordTwoCharacterClasses() {
//        System.out.println("testInvalidConfirmPasswordTwoCharacterClasses");
//        try {
//            String confirmPassword = "aaA";
//            System.out.println(confirmPassword);
//            input(validFirstName, validLastName, validEmail, validPassword, confirmPassword);
//
//            createAccountButton.click();
//            assert (!isLoginSuccessful(wait));
//
//        } finally {
//            driver.quit();
//        }
//    }
//
//    @Test
//    public void testInvalidConfirmPasswordThreeCharacterClassesBelowMinimumLength() {
//        System.out.println("testInvalidConfirmPasswordThreeCharacterClassesBelowMinimumLength");
//        try {
//            String confirmPassword = "aaA1";
//            System.out.println(confirmPassword);
//            input(validFirstName, validLastName, validEmail, validPassword, confirmPassword);
//
//            createAccountButton.click();
//            assert (!isLoginSuccessful(wait));
//
//        } finally {
//            driver.quit();
//        }
//    }
//
//    @Test
//    public void testValidConfirmPasswordThreeCharacterClassesMinimumLength() {
//        System.out.println("testValidConfirmPasswordThreeCharacterClassesMinimumLength");
//        try {
//            String confirmPassword = "Aa1!".repeat(2);
//            System.out.println(confirmPassword);
//            input(validFirstName, validLastName, validEmail, validPassword, confirmPassword);
//
//            createAccountButton.click();
//            assert (isLoginSuccessful(wait));
//
//        } finally {
//            driver.quit();
//        }
//    }
//
//    @Test
//    public void testValidConfirmPasswordThreeCharacterClassesJustOverMinimumValidLength() {
//        System.out.println("testValidConfirmPasswordThreeCharacterClassesJustOverMinimumValidLength");
//        try {
//            String confirmPassword = "aA1!aA1!a";
//            System.out.println(confirmPassword);
//            input(validFirstName, validLastName, validEmail, validPassword, confirmPassword);
//
//            createAccountButton.click();
//            assert (isLoginSuccessful(wait));
//
//        } finally {
//            driver.quit();
//        }
//    }
//
//    @Test
//    public void testValidConfirmPasswordIgnoreLeadingTrailingSpaces() {
//        System.out.println("testValidConfirmPasswordIgnoreLeadingTrailingSpaces");
//        try {
//            String confirmPassword = "aA1! ".repeat(2);
//            System.out.println(confirmPassword);
//            input(validFirstName, validLastName, validEmail, validPassword, confirmPassword);
//
//            createAccountButton.click();
//            assert (isLoginSuccessful(wait));
//
//        } finally {
//            driver.quit();
//        }
//    }
//
//    @Test
//    public void testInvalidConfirmPasswordMissingRequiredCharacterClasses() {
//        System.out.println("testInvalidConfirmPasswordMissingRequiredCharacterClasses");
//        try {
//            String confirmPassword = "password";
//            System.out.println(confirmPassword);
//            input(validFirstName, validLastName, validEmail, validPassword, confirmPassword);
//
//            createAccountButton.click();
//            assert (!isLoginSuccessful(wait));
//
//        } finally {
//            driver.quit();
//        }
//    }
}
