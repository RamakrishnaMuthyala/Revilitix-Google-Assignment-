package com.google.tests;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.google.utils.DriverManager;

import base.BaseClass;

import com.google.pages.GoogleHomePage;

public class GoogleHomePageTest extends BaseClass {

    private WebDriver driver;
    private GoogleHomePage googleHomePage;

    @Override
    @BeforeClass
    public void setUp() {
        super.setUp(); // Call the setup from BaseClass
        googleHomePage = new GoogleHomePage(driver);
    }
    @Test
    public void testGoogleHomePage() {
        googleHomePage.open();
        googleHomePage.searchFor("Selenium WebDriver");
        // Add further assertions or interactions here
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
