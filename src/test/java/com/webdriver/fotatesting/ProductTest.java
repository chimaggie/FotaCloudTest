package com.webdriver.fotatesting;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by hongyuechi on 3/7/16.
 */
public class ProductTest {
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
        driver.get("http://107.155.52.113:8080/#/products");
    }
    @After
    public void sleepTime() {
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    public int collectRow() {
//        driver.get("http://107.155.52.113:8080/#/products");
        driver.findElement(By.xpath("//button[span=100]")).click();

        WebElement mytable = driver.findElement(By.xpath("//table//tbody"));
        List<WebElement> row_table = mytable.findElements(By.tagName("tr"));

//        for (int row = 0; row < row_count; row++) {
//            List<WebElement> Columns_row = row_table.get(row).findElements(By.tagName("td"));
//            int col_count = Columns_row.size();
//            System.out.println("number of cells in row: " + row + " are " + col_count);
//
//            for (int col = 0; col < col_count; col++) {
//                String celltext = Columns_row.get(col).getText();
//                System.out.println("cell value of row number " + row + "and col number " + col + " is " + celltext);
//            }
//        }
        return row_table.size();
    }

    @Test
    public void create() {
        int orgRow = collectRow();

//        driver.get("http://107.155.52.113:8080/#/products");
        driver.findElement(By.cssSelector("a.btn[href='#/products/new']")).click();
        driver.findElement(By.id("field_name")).sendKeys("testtest");
        driver.findElement(By.id("field_mid")).sendKeys("1");
        driver.findElement(By.id("field_skunumber")).sendKeys("555");
        driver.findElement(By.cssSelector("form[name='editForm'] button[type='submit']")).click();
        try {
            Thread.sleep(1000);
        }
        catch (Exception e) {
        }
        Assert.assertEquals(orgRow+1, collectRow());
        System.out.println("item added.");
    }
    @Test
    public void view() {
        try{
            Thread.sleep(1000);
        }
        catch (Exception e){}
        driver.findElement(By.xpath("//tr[td='test']//span[@translate='entity.action.view']")).click();
        String text = driver.findElement(By.xpath("//h2")).getText();
        Assert.assertEquals("Product test", text);
    }
    @Test
    public void delete() {
        try {
            driver.findElement(By.xpath("//*[contains(@href,'testtest')]//span[@translate=\"entity.action.delete\"]")).click();
            driver.findElement(By.xpath("//form//div//button[@type='submit']")).click();
            System.out.println("item deleted");
        }
        catch (Exception e){
            System.out.println("No specific product to delete");
        }
    }

}
