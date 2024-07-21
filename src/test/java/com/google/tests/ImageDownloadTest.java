package com.google.tests;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.pages.GoogleHomePage;
import com.google.pages.GoogleImagesPage;
import com.google.utils.DriverManager;
import com.google.utils.TestUtils;
import utils.ExtentUtility;
import base.BaseClass;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.SearchResultsPage;

public class ImageDownloadTest extends BaseClass {

    @Test
    public void testImageDownload() {
        
        try {
        	GoogleHomePage googleHomePage = new GoogleHomePage(driver);
            googleHomePage.open();
            googleHomePage.searchFor(getProperty("searchText"));
            GoogleImagesPage googleImagesPage = new GoogleImagesPage(driver);
            googleImagesPage.clickImagesTab();
            googleImagesPage.clickFirstImage();
            googleImagesPage.downloadFirstImage();
            // Verify the existence of the downloaded file
            googleImagesPage.verifyFileExistence();
        } catch (Exception e) {
           
        }
        
    }
}
