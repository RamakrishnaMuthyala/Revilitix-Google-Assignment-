package com.google.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.BaseClass;

/**
 * DriverManager class is responsible for initializing, managing, and quitting
 * WebDriver instances. It supports multiple browsers (e.g., Chrome and Edge)
 * based on the configuration.
 */
public class DriverManager extends BaseClass {
	// ThreadLocal variable to ensure each thread has its own WebDriver instance
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	// Logger to record WebDriver-related events
	private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);

	/**
	 * Initializes the WebDriver based on the specified browser type from the
	 * configuration. It determines which browser to use and sets up the
	 * corresponding WebDriver.
	 *
	 * @return WebDriver instance for the specified browser
	 */
	public static WebDriver initializeDriver() {
		// Retrieve the browser type from configuration (e.g., config.properties)
		String browser = getProperty("browser").toLowerCase();

		// Switch based on the browser type and initialize the corresponding WebDriver
		switch (browser) {
		case "edge":
			return initializeEdgeDriver();
		case "chrome":
		default:
			return initializeChromeDriver();
		}
	}

	/**
	 * Initializes the Chrome WebDriver with specific options and configurations.
	 *
	 * @return WebDriver instance for Chrome
	 */
	private static WebDriver initializeChromeDriver() {

//    	// Set the download directory and auto download preference
//        String downloadPath = System.getProperty("user.dir") + "/downloads";
		// ChromeOptions to set various Chrome-specific configurations
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--inprivate"); // Open Chrome in InPrivate mode
		options.addArguments("--disable-popup-blocking"); // Disable pop-up blocking
		options.setAcceptInsecureCerts(true); // Accept insecure certificates
		options.addArguments("--start-maximized"); // Start Chrome maximized
		options.addArguments("--incognito"); // Open Chrome in incognito mode
//        options.addArguments("--disable-popup-blocking");  // Disable pop-up blocking
//        options.setAcceptInsecureCerts(true);  // Accept insecure certificates
//        options.addArguments("--remote-allow-origins=*");  // Allow remote origins
//        options.addArguments("--start-maximized");  // Start Chrome maximized

//        //options.addArguments("download.default_directory=" + downloadPath);
//        options.addArguments("download.prompt_for_download=false");
//        options.addArguments("download.directory_upgrade=true");
		options.addArguments("safebrowsing.enabled=true");
		try {
			// Set up the ChromeDriver using WebDriverManager
			WebDriverManager.chromedriver().setup();
			// Initialize ChromeDriver with the specified options
			driver.set(new ChromeDriver(options));
			logger.info("Chrome Browser is launched");
		} catch (Exception e) {
			// Log any errors encountered during initialization
			logger.error("Failed to initialize Chrome WebDriver", e);
			throw new RuntimeException("Chrome WebDriver initialization failed", e);
		}

		return driver.get();
	}

	/**
	 * Initializes the Edge WebDriver with specific options and configurations.
	 *
	 * @return WebDriver instance for Edge
	 */
	private static WebDriver initializeEdgeDriver() {
		// EdgeOptions to set various Edge-specific configurations
		EdgeOptions options = new EdgeOptions();
		options.addArguments("--inprivate"); // Open Edge in InPrivate mode
		options.addArguments("--disable-popup-blocking"); // Disable pop-up blocking
		options.setAcceptInsecureCerts(true); // Accept insecure certificates
		options.addArguments("--start-maximized"); // Start Edge maximized

		try {
			// Set up the EdgeDriver using WebDriverManager
			WebDriverManager.edgedriver().setup();
			// Initialize EdgeDriver with the specified options
			driver.set(new EdgeDriver(options));
			logger.info("Edge Browser is launched");
		} catch (Exception e) {
			// Log any errors encountered during initialization
			logger.error("Failed to initialize Edge WebDriver", e);
			throw new RuntimeException("Edge WebDriver initialization failed", e);
		}

		return driver.get();
	}

	/**
	 * Retrieves the WebDriver instance for the current thread. Initializes the
	 * driver if it is not already set.
	 *
	 * @return WebDriver instance for the current thread
	 */
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			initializeDriver();
		}
		return driver.get();
	}

	/**
	 * Quits the WebDriver instance for the current thread and cleans up resources.
	 */
	public static void quitDriver() {
		WebDriver webDriver = driver.get();
		if (webDriver != null) {
			try {
				// Quit the WebDriver instance
				webDriver.close();
				webDriver.quit();
			} catch (Exception e) {
				// Log any errors encountered during quitting
				logger.error("Failed to quit WebDriver", e);
			} finally {
				// Remove the WebDriver instance from ThreadLocal
				driver.remove();
			}
		}
	}
}
