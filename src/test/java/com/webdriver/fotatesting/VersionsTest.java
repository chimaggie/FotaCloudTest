package com.webdriver.fotatesting;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by hongyuechi on 3/10/16.
 */
public class VersionsTest {
    static WebDriver driver = new FirefoxDriver();

    @BeforeClass
    public static void beforeEverything() throws Exception {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get("http://107.155.52.113:8080/#/login");
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin1");
        driver.findElement(By.xpath("//form//button[@type='submit']")).click();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        driver.quit();
    }

    @Before
    public void setup() throws Exception {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get("http://107.155.52.113:8080/#/versions");
    }

    @After
    public void sleepTime() throws Exception {
//        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        Thread.sleep(2000);
    }

    @Test (expected = NoSuchElementException.class)
    public void delete() throws Exception{
        try {
            driver.findElement(By.xpath("//tr[td=\"auto test 1\" and td=\"126\" and contains(.,\"autotest\")]//a[span= \" Delete\"]")).click();
        } catch (Exception e) {
            System.out.println("no item matched autotest1===126");
        }
        driver.findElement(By.xpath("//button//span[@translate=\"entity.action.delete\"]")).click();
        Thread.sleep(3000);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//tr[td=\"auto test 1\" and td=\"126\" and contains(.,\"autotest\")]"));
    }

    @ Test
    public void upload() throws Exception {
        // something
        String inZip = "/Users/hongyuechi/Documents/lecar/fota_update/sample-rom-version-1s2.0.zip";
        String outZip = "/Users/hongyuechi/Documents/lecar/fota_update/sample-rom-version-1s2.0auto.zip";
        String infoPath = "sample-rom-version-1s2.0/info.properties";
        Map<String, String> updates = new HashMap<String, String>();
        updates.put("model", "auto test 1");
        updates.put("sku", "126");
        ZipFileHandler.updateZipPropEntry(inZip, outZip, infoPath, updates);

        driver.findElement(By.name("name")).sendKeys("autotest");
        WebElement upload = driver.findElement(By.name("file"));
        upload.sendKeys(outZip);
        driver.findElement(By.xpath("//button[@ng-click=\"upload(file)\"]")).click();

        String product = driver.findElement(By.xpath("//tr[td=\"auto test 1\" and td=\"126\" and contains(.,\"autotest\")]/td[1]")).getText();
        String sku = driver.findElement(By.xpath("//tr[td=\"auto test 1\" and td=\"126\" and contains(.,\"autotest\")]/td[2]")).getText();
        String name = driver.findElement(By.xpath("//tr[td=\"auto test 1\" and td=\"126\" and contains(.,\"autotest\")]/td[6]")).getText();
        String fileName = name.substring(0, name.indexOf("-"));
        System.out.println("ProductTest is: " + product + ", sku is " + sku + ", file name is: " + fileName);

        Map<String, String> getItems = new HashMap<String, String>();
        getItems.put("model", product);
        getItems.put("sku", sku);

        org.junit.Assert.assertEquals(updates, getItems);

//        List<WebElement> items = driver.findElements(By.xpath("//tr[td=\"auto test 1\" and td=\"126\" and contains(.,\"autotest\")]"));
//        for (WebElement item: items) {
//            System.out.println(item.getText());
//        }

//        String name = driver.findElement(By.xpath("//td[contains(text(),'autotest')]")).getText();
//        String uploadName = name.substring(0, name.indexOf("-"));
//        Assert.assertEquals("autotest", uploadName);
    }

    public void uploadLagacy() {
        try {
            ZipFileHandler.modifyFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver.findElement(By.name("name")).sendKeys("autotest");
        WebElement upload = driver.findElement(By.name("file"));
        upload.sendKeys("~/Documents/lecar/fota_update/sample-rom-version-1s2.0auto.zip");
        driver.findElement(By.xpath("//button[@ng-click=\"upload(file)\"]")).click();
        String name = driver.findElement(By.xpath("//td[contains(text(),'autotest')]")).getText();
        String uploadName = name.substring(0, name.indexOf("-"));
        Assert.assertEquals("autotest", uploadName);
    }
}
