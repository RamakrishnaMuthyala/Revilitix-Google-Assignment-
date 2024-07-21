package base;

import com.google.utils.DriverManager;
import com.google.utils.ExtentUtility;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseClass {
	protected WebDriver driver;
	private static Properties properties;
	public static ExtentTest test;
	public static ExtentReports extent;
	String browser = getProperty("browser").toLowerCase();

	// Constructor to initialize the WebDriver
	public BaseClass() {
		this.driver = DriverManager.getDriver();
	}

	@BeforeSuite
	public void setUpExtentReports() throws IOException {
		ExtentUtility.getReporter();
	}

	@BeforeClass
	public void setUp() {
		driver = DriverManager.initializeDriver();
		if (driver == null) {
			throw new RuntimeException("Driver is not initialized.");
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method, ITestContext context) throws IOException {

		String testName = this.getClass().getSimpleName() + "::" + method.getName() + "::" + browser;
		test = ExtentUtility.startTest(testName, method.getName());
		test.assignCategory("Web Automation");

		String buildName = System.getProperty("releaseName");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("name", method.getName());
		capabilities.setCapability("build", buildName);
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestContext context) {

		ExtentUtility.endTest();
		// Adding system info
//        extent.addSystemInfo("isEmulation", String.valueOf(Session.isEmulation()));
//        extent.addSystemInfo("isDesktop", String.valueOf(Session.isDesktop()));
//        extent.addSystemInfo("Browser", Session.getBrowser());
//        extent.addSystemInfo("Environment", Session.getEnv());

//        // Set up WebDriver based on the browser parameter
//        driver = createWebDriver(browser);
//        LocalDriverManager.setWebDriver(driver);
	}

	/**
	 * Retrieves a value from the properties file.
	 * 
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

	@AfterClass
	public void tearDown() {
		DriverManager.quitDriver();
	}

	@AfterSuite
	public void tearDownExtentReports() {
		ExtentUtility.extent.flush();
		ExtentUtility.extent.close();
		DriverManager.quitDriver();
	}

	// Load properties file when the class is loaded
	static {
		properties = new Properties();
		String filePath = System.getProperty("user.dir") + "/src/main/resources/config.properties";
		try {
			FileInputStream inputStream = new FileInputStream(filePath);
			properties.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Method to get a property value based on the key
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	// Method to highlight an element using JavaScript
	public static void highlightElement(WebDriver driver, WebElement ele) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', 'border: 2px solid red;');", ele);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BaseClass(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

}
