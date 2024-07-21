package com.google.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.utils.ExtentUtility;
import com.relevantcodes.extentreports.LogStatus;

import base.BaseClass;

public class GoogleHomePage extends BaseClass {

    // Locator for the Google search box
    private By searchBox = By.name("q");

    // Constructor to initialize WebDriver and call the BaseClass constructor
    public GoogleHomePage(WebDriver driver) {
        super(driver); // Initialize WebDriver in the base class
    }

    // Method to navigate to the Google homepage
    public void open() {
        try {
            // Navigate to the URL specified in the properties file
            driver.get(getProperty("url"));

            // Log the successful navigation and capture a screenshot
            ExtentUtility.getTest().log(LogStatus.PASS, "Opened URL: " + getProperty("url"),
                    ExtentUtility.getTest().addScreenCapture(ExtentUtility.takeScreenShotForExtentReport(driver)));

            System.out.println("Navigated to Google homepage.");
        } catch (Exception e) {
            // Log any error that occurs during navigation
            ExtentUtility.getTest().log(LogStatus.FAIL, "Error navigating to URL: " + e.getMessage());
            System.out.println("Error navigating to URL: " + e.getMessage());
        }
    }

    // Method to perform a search on Google
    public void searchFor(String searchTerm) {
        try {
            // Create a WebDriverWait instance to handle dynamic elements
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Wait for the search box to be clickable
            WebElement searchBoxElement = wait.until(ExpectedConditions.elementToBeClickable(searchBox));
            
            // Highlight the search box element for visibility
            highlightElement(driver, searchBoxElement);
            
            // Enter the search term and submit the search
            searchBoxElement.sendKeys(searchTerm);
            searchBoxElement.submit();

            // Log the search action and capture a screenshot
            ExtentUtility.getTest().log(LogStatus.PASS, "Searched for: " + searchTerm,
                    ExtentUtility.getTest().addScreenCapture(ExtentUtility.takeScreenShotForExtentReport(driver)));

            System.out.println("Performed search for: " + searchTerm);
        } catch (Exception e) {
            // Log any error that occurs during the search
            ExtentUtility.getTest().log(LogStatus.FAIL, "Error during search: " + e.getMessage());
            System.out.println("Error during search: " + e.getMessage());
        }
    }
}
