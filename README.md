# Selenium Image Download Project

## Overview

This project demonstrates how to use Selenium WebDriver in Java to automate the process of searching for an image on Google, downloading the first image, and verifying its existence in the Downloads folder. It integrates with ExtentReports for detailed reporting and uses a flexible WebDriver management system.

## Prerequisites

- Java 8 or higher
- Maven
- Google Chrome or Microsoft Edge browser
- WebDriverManager library for managing browser drivers

## Setup Instructions

1. **Clone the Repository**

    ```sh
    git clone <repository-url>
    cd SeleniumImageDownloadProject
    ```

2. **Build the Project Using Maven**

    ```sh
    mvn clean install
    ```

3. **Run the Tests Using TestNG**

    ```sh
    mvn test
    ```

## Project Structure

- **`src/main/java/com/google/utils/`**: Contains utility classes for WebDriver management and reporting.
  - **`DriverManager.java`**: Manages WebDriver instances for Chrome and Edge browsers. Configures and initializes WebDriver instances, and handles browser-specific settings.
  - **`ExtentUtility.java`**: Handles ExtentReports setup, screenshot capture, and adds system and test information to the report.
- **`src/main/resources/`**: Contains configuration files.
  - **`config.properties`**: Configuration properties for the test framework, such as browser type and other settings.
  - **`data.properties`**: Properties for test data and additional settings like image quality.
  - **`extent-config.xml`**: Configuration file for ExtentReports.
- **`src/test/java/base/`**: Contains the base class for test cases.
  - **`BaseClass.java`**: Provides setup and teardown methods for tests, including WebDriver initialization, ExtentReports integration, and utility methods for test execution.
- **`ReportGenerator/`**: Directory where ExtentReports will generate test reports. The directory structure will include subfolders for screenshots and the test report HTML file.
- **`src/test/java/com/example/tests/`**: Contains test cases.
  - **`ImageDownloadTest.java`**: Test case for downloading an image from Google Images. Contains the logic to interact with the browser, search for an image, and verify its download.

## Class Descriptions

### DriverManager

- **Purpose**: Manages WebDriver instances for Chrome and Edge browsers. Initializes drivers based on configuration and manages browser-specific settings.
- **Key Methods**:
  - `initializeDriver()`: Determines which browser to use based on configuration and initializes the corresponding WebDriver.
  - `initializeChromeDriver()`: Sets up and initializes ChromeDriver with specific options.
  - `initializeEdgeDriver()`: Sets up and initializes EdgeDriver with specific options.
  - `getDriver()`: Retrieves the WebDriver instance for the current thread.
  - `quitDriver()`: Quits the WebDriver instance and cleans up resources.

### ExtentUtility

- **Purpose**: Integrates with ExtentReports to generate detailed test reports and manage screenshots.
- **Key Methods**:
  - `getReporter()`: Initializes and returns the ExtentReports instance.
  - `getTest()`: Retrieves the current ExtentTest instance.
  - `startTest(String testName, String desc)`: Starts a new test instance in ExtentReports.
  - `endTest()`: Ends the current test instance in ExtentReports.
  - `takeScreenShotForExtentReport(WebDriver driver)`: Captures a screenshot and saves it for the Extent report. Adjusts image quality based on configuration.
  - `addScreenshotToReport(String screenshotPath)`: Adds a screenshot to the Extent report.
  - `getPropertyValue(String key)`: Retrieves a value from the properties file.

### BaseClass

- **Purpose**: Provides setup and teardown methods for test execution. Manages WebDriver initialization, ExtentReports setup, and utility methods for tests.
- **Key Methods**:
  - `setUpExtentReports()`: Initializes ExtentReports before the test suite.
  - `setUp()`: Initializes WebDriver and sets browser settings before each test class.
  - `beforeMethod(Method method, ITestContext context)`: Starts a new test in ExtentReports before each test method. Sets up DesiredCapabilities for the test.
  - `afterMethod(Method method, ITestContext context)`: Ends the current test in ExtentReports after each test method.
  - `tearDown()`: Quits the WebDriver after all tests in the class.
  - `tearDownExtentReports()`: Flushes and closes ExtentReports after all tests in the suite.
  - `getProperty(String key)`: Retrieves a property value from the `config.properties` file.
  - `highlightElement(WebDriver driver, WebElement ele)`: Highlights a WebElement with a red border using JavaScript.

## Test Implementation

### ImageDownloadTest

- **Purpose**: Contains test cases for searching and downloading images from Google.
- **Example Test Method**:

    ```java
    @Test
    public void downloadImage() {
        driver.get("https://www.google.com");
        // Example actions and assertions to search for and download an image
        WebElement imageElement = driver.findElement(By.xpath("//img[@alt='example']"));
        ExtentUtility.highlightElement(driver, imageElement);
        // Additional test steps...
    }
    ```

## Configuration

- **`config.properties`**: Used for setting browser types and other framework configurations.
- **`data.properties`**: Contains test-specific settings like image quality.
- **`extent-config.xml`**: Defines the configuration for ExtentReports.

## Screenshots and Reports

- **Screenshots**: Captured during test execution and saved in the `ReportGenerator` directory.
- **ExtentReports**: Detailed reports generated in the `ReportGenerator` directory. Includes test logs, screenshots, and system information.

## Notes

- Adjust sleep times and the number of down arrow presses in the context menu navigation as needed to fit your system and browser behavior.
- Ensure that the WebDriver executables are compatible with the browser versions installed on your system.


