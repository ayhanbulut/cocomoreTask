
package com.tester.stepDef;

import com.tester.utilities.ConfigurationReader;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.given;

public class APITaskStepDefs {
    Response response;
    static int ID;
    static  int postID;

    String name = "Mustafaq10";
    String mail = "mustafa10";
    String rename = "MustafaCoc1";
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


    @When("Create a user with given details")
    public void create_a_user_with_given_details(io.cucumber.datatable.DataTable dataTable) throws InterruptedException {
         response=given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .and().header("Authorization", ConfigurationReader.get("token"))
                .body(bodyUser)
                .when().post("/public-api/users");
        response.prettyPrint();

    }

    @Then("Verify that user is created successfully for given details with status code {int}")
    public void verify_that_user_is_created_successfully_for_given_details_with_status_code(Integer int1) {
        JsonPath jsonPath = response.jsonPath();
        int code = jsonPath.get("code");
        Assert.assertEquals(201, code);
        ID = jsonPath.get("data.id");
        }

    @When("Rename created user")
    public void rename_created_user(io.cucumber.datatable.DataTable dataTable) {
        response = given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .and().header("Authorization", ConfigurationReader.get("token"))
                .and().pathParam("id", ID)
                .and().body(bodyRename)
                .patch("/public-api/users/{id}");
        response.prettyPrint();

    }

    @Then("Verify that update has been made for given user with status code {int}")
    public void verify_that_update_has_been_made_for_given_user_with_status_code(Integer int1) {
        JsonPath jsonPath = response.jsonPath();
        int code = jsonPath.get("code");
        Assert.assertEquals(code, 200);
        Assert.assertEquals(name,jsonPath.get("data.name"));
    }

    @When("Create a post for given user")
    public void create_a_post_for_given_user(io.cucumber.datatable.DataTable dataTable) {
        response = given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .and().header("Authorization", ConfigurationReader.get("token"))
                .and().pathParam("id", ID)
                .and().body(bodyUpdate)
                .post("/public-api/users/{id}/posts");
        response.prettyPrint();
    }

    @Then("Verify that post is created successfully for given user with status code {int}")
    public void verify_that_post_is_created_successfully_for_given_user_with_status_code(Integer int1) {
        JsonPath jsonPath = response.jsonPath();
        int code = jsonPath.get("code");
        Assert.assertEquals(201,code);
        postID=jsonPath.get("data.id");

         }

    @When("Create a comment for given post with given details")
    public void create_a_comment_for_given_post_with_given_details(io.cucumber.datatable.DataTable dataTable) {
        response = given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .and().header("Authorization", ConfigurationReader.get("token"))
                .and().pathParam("id", postID)
                .and().body(CommentBody)
                .post("/public-api/posts/{id}/comments");
        response.prettyPrint();
    }

    @Then("Verify that comment is created successfully for given post with status code {int}")
    public void verify_that_comment_is_created_successfully_for_given_post_with_status_code(Integer int1) {
        JsonPath jsonPath = response.jsonPath();
        int code = jsonPath.get("code");
        Assert.assertEquals(201,code);
        }

    @Then("Verify that comment is connected correct user")
    public void verify_that_comment_is_connected_correct_user() {
        JsonPath jsonPath = response.jsonPath();
        int post_id=jsonPath.get("data.post_id");

        Assert.assertEquals(post_id,postID);
    }

    @When("Delete created user")
    public void delete_created_user() {
        response =  given().pathParam("id", ID)
                .and().header("Authorization", ConfigurationReader.get("token"))
                .when().delete("/public-api/users/{id}");
    }

    @Then("Verify that user is deleted with status code {int}")
    public void verify_that_user_is_deleted_with_status_code(Integer int1) {
        JsonPath jsonPath = response.jsonPath();
        int code = jsonPath.get("code");
        Assert.assertEquals(204,code);
    }

}
