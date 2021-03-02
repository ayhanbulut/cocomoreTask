package com.tester.stepDef;

import com.tester.utilities.ConfigurationReader;
import io.cucumber.java.Before;

import static io.restassured.RestAssured.baseURI;

public class Hooks {
    @Before
    public static void beforeClass() {
        baseURI = ConfigurationReader.get("baseURI");
}
}
