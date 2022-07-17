package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.net.ServerSocket;

public class Base {
	public static IOSDriver<IOSElement> driver;
	public static AppiumDriverLocalService service;
	public static String appiumServiceUrl  = null; 
	
	public static  void startServer() {
		  // Build the Appium Service
		AppiumServiceBuilder builder = new  AppiumServiceBuilder();
	    builder.withAppiumJS(new File("/usr/local/bin/appium"));
	    builder.withIPAddress("127.0.0.1");
	    builder.usingAnyFreePort();
	    builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
	    builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");

	    // Start the server with the builder
	    try {
	    	service = AppiumDriverLocalService.buildService(builder);
	        service.start();
	    } catch (NullPointerException e) {
	        e.printStackTrace();
	    }

	     appiumServiceUrl = service.getUrl().toString();
	    System.out.println("Appium URL " + appiumServiceUrl);

	}

	public  static void startEmulator() throws IOException, InterruptedException {
		Runtime.getRuntime().exec("src/main/java/resources/startEmulator.sh");
		Thread.sleep(5000);
	}

	public static  void stopServer() {
		// stop the server with the builder
		try {
			service.stop();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}	}

	public static IOSDriver capabilities(String appKey) throws IOException, InterruptedException {

//		Properties prop= new Properties();
//		FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"/src/main/java/resources/global.properties");
//		prop.load(fis);
//
//		File appDir = new File("src/main/java/resources");
//		File app = new File(appDir,(String) prop.getProperty(appKey));
//		DesiredCapabilities cap = new DesiredCapabilities();
//		//String deviceDet = prop.getProperty(device);
//		String deviceDet = System.getProperty("deviceName");
//		System.out.println("deviceDet: "+deviceDet);
//		if(deviceDet.contains("emulator")) {
//			startEmulator();
//		}
//
//		System.out.println("I am at 115");
//		cap.setCapability(MobileCapabilityType.DEVICE_NAME, deviceDet);
//		System.out.println("I am at 117");
//		cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
//		System.out.println(app.getAbsolutePath());
//		System.out.println("I am at 119");
//		cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
//		System.out.println("I am at 121");
//		cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,14);
//		System.out.println("appiumServiceUrl"+appiumServiceUrl);
//		System.out.println("driver value before"+driver);
//		System.out.println("cap"+cap);
//		String urlAppium = appiumServiceUrl.replace("http://127.0.0.1", "http://localhost");
//		driver = new AndroidDriver<>(new URL(urlAppium),cap);
//		System.out.println("I am at 123");
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//		System.out.println("driver value after"+driver);
//		return driver;
		
		String appName = System.getProperty("appName");
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, "14.2");
		cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
		cap.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 12 Pro");
		cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
		cap.setCapability(IOSMobileCapabilityType.LAUNCH_TIMEOUT, 50000);
		cap.setCapability("commandTimeouts", 12000);
		if(appName.equalsIgnoreCase("UIKitCatalog"))
		cap.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir")+"/app/UIKitCatalog.app");
		else 
			cap.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir")+"/app/longtap.app");
		String urlAppium = appiumServiceUrl.replace("http://127.0.0.1", "http://localhost");
		driver = new IOSDriver<IOSElement>(new URL(urlAppium),cap);		
		return driver;
		


	}

	public static void getScreenshot(String tcName) throws IOException {
		try {
			System.out.println("II am Here:"+tcName);
			System.out.println("driver value"+driver);
			File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcFile,new File("src/Screenshots/"+tcName+".png"));
		}catch (Exception e){
			System.out.println("Exception while taking screenshot "+e.getMessage());
		}



	}
	
}
