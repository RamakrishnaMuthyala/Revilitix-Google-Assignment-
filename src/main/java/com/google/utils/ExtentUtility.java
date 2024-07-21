package com.google.utils;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class ExtentUtility {
    final static Logger LOGGER = LogManager.getLogger(ExtentUtility.class);
    public static ExtentReports extent;
    public static ExtentTest test;
    private static ThreadLocal<String>methodName=new ThreadLocal<String>();
    public static String reportFolder = "";
    
    private static Map<Integer, ExtentTest> a = new HashMap<>();

    // Private constructor to prevent instantiation
    private ExtentUtility() {}

    /**
     * Initializes and returns the ExtentReports instance.
     * @return ExtentReports object
     * @throws IOException if configuration file loading fails
     */
    public static synchronized ExtentReports getReporter() throws IOException {
        if (extent == null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
            Date date = new Date();
            String releaseName = getPropertyValue("releaseName");
            reportFolder = releaseName + "-" + dateFormat.format(date);
            String reportPath = new File("ReportGenerator/" + reportFolder + "/TestReport.html").getPath();

            extent = new ExtentReports(reportPath, true, Locale.ENGLISH);
            extent.loadConfig(new File("src/test/resources/extent-config.xml"));
            extent.addSystemInfo("Environment", "QA");
            extent.addSystemInfo("Selenium", "3.141.59");
            System.setProperty("releaseName", reportFolder);
        }
        return extent;
    }

    /**
     * Retrieves the current test instance.
     * @return ExtentTest object representing the current test
     */
    public static synchronized ExtentTest getTest() {
        return a.get((int) Thread.currentThread().getId());
    }

    /**
     * Ends the current test instance.
     */
    public static synchronized void endTest() {
        extent.endTest(a.get((int) Thread.currentThread().getId()));
    }

    /**
     * Starts a new test instance.
     * @param testName Name of the test
     * @return ExtentTest object representing the started test
     */
    public static synchronized ExtentTest startTest(String testName) {
        return startTest(testName, "");
    }

    /**
     * Starts a new test instance with a description.
     * @param testName Name of the test
     * @param desc Description of the test
     * @return ExtentTest object representing the started test
     */
    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest testInstance = extent.startTest(testName, desc);
        a.put((int) Thread.currentThread().getId(), testInstance);
        return testInstance;
    }

    /**
     * Captures a screenshot for the Extent Report.
     * @param driver WebDriver instance to capture screenshot from
     * @return Path of the captured screenshot
     */
    public static String takeScreenShotForExtentReport(WebDriver driver) {
        Calendar cal = Calendar.getInstance();
        long timestamp = cal.getTimeInMillis();
        File screenshotFile = null;

        try {
            // Get image quality from properties file
            String imgQuality = getPropertyValue("imageQuality");

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Img Quality: " + imgQuality);
            }

            // Take screenshot
            screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Define file path based on image quality
            String filePath;
            if ("low".equalsIgnoreCase(imgQuality)) {
                filePath = "ReportGenerator/" + ExtentUtility.reportFolder + "/Screenshots/image_" + timestamp + ".png";
            } else if ("high".equalsIgnoreCase(imgQuality)) {
                filePath = "ReportGenerator/" + ExtentUtility.reportFolder + "/Screenshots/img_" + timestamp + ".png";
            } else {
                filePath = "ReportGenerator/" + ExtentUtility.reportFolder + "/Screenshots/screenshot_" + timestamp + ".png";
            }

            // Save screenshot to file
            FileUtils.copyFile(screenshotFile, new File(filePath));

            // Verify if file exists
            File file = new File(filePath);
            if (file.exists()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(filePath + " exists");
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(filePath + " does not exist");
                }
            }

            return "Screenshots/screenshot_" + timestamp + ".png";
        } catch (Exception e) {
            LOGGER.error("Error while capturing screenshot", e);
            return null;
        } finally {
            // Clean up the screenshot file if it exists
            if (screenshotFile != null && screenshotFile.exists()) {
                //screenshotFile.delete();
            }
        }
    }
    
    /**
     * Adds a screenshot to the ExtentTest instance.
     * @param screenshotPath Relative path to the screenshot
     */
    public static void addScreenshotToReport(String screenshotPath) {
        try {
            ExtentTest currentTest = getTest();
            if (currentTest != null) {
                currentTest.log(com.relevantcodes.extentreports.LogStatus.INFO, "Screenshot: " + currentTest.addScreenCapture(screenshotPath));
            }
        } catch (Exception e) {
            LOGGER.error("Error while adding screenshot to ExtentReport", e);
        }
    }

    

    /**
     * Retrieves a value from the properties file.
     * @param key Key to look up in the properties file
     * @return Value associated with the given key
     * @throws IOException if reading from the properties file fails
     */
    public static String getPropertyValue(String key) throws IOException {
        String value = "";
        FileInputStream fileInputStream = null;
        try {
            String filePath = System.getProperty("user.dir") + "/src/main/resources/data.properties";
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException("data.properties file not found at: " + filePath);
            }
            fileInputStream = new FileInputStream(file);
            Properties property = new Properties();
            property.load(fileInputStream);
            value = property.getProperty(key);
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
        return value;
    }

}



/*
protected void takeScreenShot() {
    // Get the method name from the stack trace
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    
    // Ensure there's at least 3 elements in the stack trace
    if (stackTrace.length < 3) {
        throw new IllegalStateException("Stack trace length is insufficient to determine method name.");
    }

    // Extract the method name
    String fullMethodName = stackTrace[2].toString();
     methodName.set(stackTrace[2].toString().replaceAll(".*(\\..*)*\\(.*\\(.*","$2"));

    // Capture screenshot
    TakesScreenshot scrShot = (TakesScreenshot) DriverManager.getDriver();
    File snapShot = scrShot.getScreenshotAs(OutputType.FILE);

    // Define destination file path
    String screenshotDirectory = "screenshots";  // Define your screenshot directory here
    String prettyTime = Session.getPrettyTime(); // Assuming Session.getPrettyTime() gives a formatted timestamp
    File destFile = new File("src/test/resources/img/" + screenshotDirectory + "/" + methodName + "_" + prettyTime + ".png");

    try {
        // Create directories if they don't exist
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        // Copy the screenshot to the destination file
        FileUtils.copyFile(snapShot, destFile);
        
    } catch (IOException e) {
        // Log the error for better debugging
        e.printStackTrace();
        // Optionally, add a logger here if you have a logging framework
    }
}

*/
