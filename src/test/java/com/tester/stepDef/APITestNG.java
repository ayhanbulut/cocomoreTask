package com.tester.stepDef;
import static io.restassured.RestAssured.*;

import com.tester.utilities.ConfigurationReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

// 1.    Create a user
// 2.    Verify that user is created successfully (201)
// 3.    Rename created user
// 4.    Verify that update has been made for given user
// 5.    Create a post for given user
// 6.    Verify that post is created successfully for given user (201)
// 7.    Create a comment for given post
// 8.    Verify that comment is created successfully for given post (201)
// 9.    Verify that comment is connected correct user
// 10.   Delete created user
// 11.   Verify that user is deleted (204)

public class APITestNG {
    Response response;
    static int ID;
    static  int postID;

    String name = "MustafaE";
    String mail = "Musta04";
    String rename = "MustafaCocomore";
    String bodyUser = "{\n" +
            "    \"name\": \""+name+"\",\n" +
            "    \"gender\": \"Male\",\n" +
            "    \"email\": \""+mail+"@gmail.com\",\n" +
            "    \"status\": \"Active\"\n" +
            "}";
    String bodyUpdate = "{\n" +
            "    \"title\": \"mustafa123 post title\",\n" +
            "    \"body\": \"mustafa123 post body\"\n" +
            "}";
    String bodyRename = "{\n" +
            "    \"name\": \""+name+"\",\n" +
            "    \"email\": \""+mail+"@gmail.com\",\n" +
            "    \"status\": \"Active\"\n" +
            "}";
    String CommentBody="{\n" +
            "    \"name\": \"Mustafa\",\n" +
            "    \"email\": \""+mail+"@gmail.com\",\n" +
            "    \"body\": \"Cocomore\"\n" +
            "}";
    @BeforeClass
    public static void beforeClass() {

        baseURI = "https://gorest.co.in";
    }

    @Test(priority = 1)
    public void createUserAndVerify() {
// 1.    Create a user
        Response response=given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .and().header("Authorization", ConfigurationReader.get("token"))
                .body(bodyUser)
                .when().post("/public-api/users");
        response.prettyPrint();
        JsonPath jsonPath = response.jsonPath();
        int code = jsonPath.get("code");

// 2.    Verify that user is created successfully (201)
        Assert.assertEquals(201, code);
        ID = jsonPath.get("data.id");
    }
    @Test(priority = 2)
    public void RenameUser() {
// 3.    Rename created user
        response = given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .and().header("Authorization",ConfigurationReader.get("token"))
                .and().pathParam("id", ID)
                .and().body(bodyRename)
                .patch("/public-api/users/{id}");
        response.prettyPrint();

// 4.    Verify that update has been made for given user

        JsonPath jsonPath = response.jsonPath();

        int code = jsonPath.get("code");
        Assert.assertEquals(code, 200);
        System.out.println("ExpectedName = " + jsonPath.get("data.name"));
        Assert.assertEquals(name,jsonPath.get("data.name"));

    }
    @Test(priority = 3)
    public void createPost() {
// 5.    Create a post for given user
        response = given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .and().header("Authorization",ConfigurationReader.get("token"))
                .and().pathParam("id", ID)
                .and().body(bodyUpdate)
                .post("/public-api/users/{id}/posts");
        response.prettyPrint();
        JsonPath jsonPath = response.jsonPath();
        int code = jsonPath.get("code");
// 6.    Verify that post is created successfully for given user (201)
        Assert.assertEquals(201,code);
        postID=jsonPath.get("data.id");
        System.out.println("postID = " + postID);

    }
    @Test(priority = 4)
    public void commentForGivenPost(){

// 7.    Create a comment for given post
        response = given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .and().header("Authorization",ConfigurationReader.get("token"))
                .and().pathParam("id", postID)
                .and().body(CommentBody)
                .post("/public-api/posts/{id}/comments");
        response.prettyPrint();
// 8.    Verify that comment is created successfully for given post (201)
        JsonPath jsonPath = response.jsonPath();
        int code = jsonPath.get("code");
        Assert.assertEquals(201,code);
// 9.    Verify that comment is connected correct user
        int post_id=jsonPath.get("data.post_id");

        Assert.assertEquals(post_id,postID);

    }
    @Test(priority = 5)
    public void deleteUser(){
// 10.   Delete created user
        System.out.println(ID);
        response =  given().pathParam("id", ID)
                .and().header("Authorization",ConfigurationReader.get("token"))
                .when().delete("/public-api/users/{id}");
// 11.   Verify that user is deleted (204)
        JsonPath jsonPath = response.jsonPath();
        int code = jsonPath.get("code");
        Assert.assertEquals(204,code);
    }
}

