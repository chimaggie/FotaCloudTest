package com.webdriver.fotatesting;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.util.concurrent.TimeUnit;

public class FotaLogin {
    static WebDriver driver = new FirefoxDriver();

    @BeforeClass
    public static void beforeEverything() throws Exception {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get("http://107.155.52.113:8080/#/login");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        driver.quit();
    }
    @Before
    public void setup() throws Exception {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get("http://107.155.52.113:8080/#/login");
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("password")).clear();
    }

    @After
    public void sleepTime() throws Exception {
        Thread.sleep(1000);
    }

    @Test
    public void logInSucc() throws Exception{
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin1");
        driver.findElement(By.xpath("//form//button[@type='submit']")).click();
        Thread.sleep(1000);
        Assert.assertThat(driver.findElement(By.xpath("//h1[contains(.,\"Welcome\")]")).getText(),CoreMatchers.containsString("Welcome"));
    }
    @Test
    public void logInFail() throws Exception {
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("");
        driver.findElement(By.xpath("//form//button[@type='submit']")).click();
        Thread.sleep(1000);
        String actual = driver.findElement(By.xpath("//div[strong=\"Failed to sign in!\"]")).getText();
        Assert.assertThat(actual, CoreMatchers.containsString("Fail"));
    }
}
