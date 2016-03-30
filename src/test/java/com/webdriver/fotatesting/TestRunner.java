package com.webdriver.fotatesting;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by hongyuechi on 3/17/16.
 */
public class TestRunner {
    public static void main (String[] args) {
        Result result = JUnitCore.runClasses(Product.class);
        for (Failure failure: result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}
