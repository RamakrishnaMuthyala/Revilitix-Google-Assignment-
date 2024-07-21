package com.google.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.google.utils.ExtentUtility;
import com.relevantcodes.extentreports.LogStatus;
import base.BaseClass;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class GoogleImagesPage extends BaseClass {

    private By imagesTab = By.xpath("(//div[contains(text(),'Images')])[1]");
    private By firstImage = By.xpath("(//div[@class='H8Rx8c']//img)[1]");
    private SoftAssert softAssert = new SoftAssert(); // Create a SoftAssert object

    public String downloadPath;
    public String modifiedFileName;

    public GoogleImagesPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Clicks on the 'Images' tab.
     */
    public void clickImagesTab() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement imagesTabElement = wait.until(ExpectedConditions.elementToBeClickable(imagesTab));
            highlightElement(driver, imagesTabElement); // Highlight the element for visibility
 

            // Assertion to verify the click action
            softAssert.assertTrue(imagesTabElement.isDisplayed(), "Images tab is not displayed.");
            imagesTabElement.click(); // Click the Images tab
            ExtentUtility.getTest().log(LogStatus.PASS, "Images Tab Clicked",
                    ExtentUtility.getTest().addScreenCapture(ExtentUtility.takeScreenShotForExtentReport(driver)));
        } catch (Exception e) {
            ExtentUtility.getTest().log(LogStatus.FAIL, "Failed to click Images Tab: " + e.getMessage(),
                    ExtentUtility.getTest().addScreenCapture(ExtentUtility.takeScreenShotForExtentReport(driver)));
        }
    }

    /**
     * Clicks on the first image.
     */
    public void clickFirstImage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement firstImageElement = wait.until(ExpectedConditions.elementToBeClickable(firstImage));
            highlightElement(driver, firstImageElement); // Highlight the element for visibility
            firstImageElement.click(); // Click the first image

            // Assertion to verify the image is displayed
            softAssert.assertTrue(firstImageElement.isDisplayed(), "First image is not displayed.");
            ExtentUtility.getTest().log(LogStatus.PASS, "First Image Clicked",
                    ExtentUtility.getTest().addScreenCapture(ExtentUtility.takeScreenShotForExtentReport(driver)));
        } catch (Exception e) {
            ExtentUtility.getTest().log(LogStatus.FAIL, "Failed to click First Image: " + e.getMessage(),
                    ExtentUtility.getTest().addScreenCapture(ExtentUtility.takeScreenShotForExtentReport(driver)));
        }
    }

    /**
     * Downloads the first image by simulating right-click and saving it.
     */
    public void downloadFirstImage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement firstImageElement = wait.until(ExpectedConditions.elementToBeClickable(firstImage));
            Actions actions = new Actions(driver);
            actions.contextClick(firstImageElement).perform(); // Right-click on the first image

            // Short implicit wait to allow context menu to appear
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

            Robot robot = new Robot();
            for (int i = 0; i < 7; i++) {
                robot.keyPress(KeyEvent.VK_DOWN);
                robot.keyRelease(KeyEvent.VK_DOWN);
                Thread.sleep(400); // Reduced sleep time to avoid unnecessary delay
            }
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            // Generate a filename for the image
            String fileName = firstImageElement.getAttribute("alt");
            if (fileName == null || fileName.trim().isEmpty()) {
                fileName = getProperty("fileNameIfNotExist");
            }
            fileName = fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
            String timestamp = now.format(formatter);
            modifiedFileName = fileName + "_" + timestamp;

            // Set the path where the image will be saved
            downloadPath = System.getProperty("user.dir") + File.separator + modifiedFileName + ".png";
            StringSelection filePath = new StringSelection(downloadPath);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(filePath, null);
            System.out.println("Download Path: " + downloadPath);

            // Simulate key presses for paste operation and save the file
            Thread.sleep(2000);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);

            Thread.sleep(1000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            // Assertion to verify that the file is downloaded by checking file path
            File downloadedFile = new File(downloadPath);
            softAssert.assertTrue(downloadedFile.exists(), "Failed to download the image.");

            ExtentUtility.getTest().log(LogStatus.PASS, "First image successfully downloaded",
                    ExtentUtility.getTest().addScreenCapture(ExtentUtility.takeScreenShotForExtentReport(driver)));
        } catch (AWTException | InterruptedException e) {
            ExtentUtility.getTest().log(LogStatus.FAIL, "Failed to download First Image: " + e.getMessage(),
                    ExtentUtility.getTest().addScreenCapture(ExtentUtility.takeScreenShotForExtentReport(driver)));
        }
    }

    /**
     * Verifies that the downloaded file exists.
     */
    public void verifyFileExistence() {
        try {
            File downloadedFile = new File(downloadPath);
            softAssert.assertTrue(downloadedFile.exists(), "Image download failed.");
            ExtentUtility.getTest().log(LogStatus.PASS,
                    "Verified the existence of the downloaded file: " + modifiedFileName + " in " + downloadPath);
        } catch (AssertionError e) {
            ExtentUtility.getTest().log(LogStatus.FAIL, "Failed to verify file existence: " + e.getMessage(),
                    ExtentUtility.getTest().addScreenCapture(ExtentUtility.takeScreenShotForExtentReport(driver)));
        } finally {
            softAssert.assertAll(); // Ensure all soft assertions are reported
        }
    }
}
