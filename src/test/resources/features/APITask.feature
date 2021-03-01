
Feature: CRUD operations test

    @wip
    Scenario: end to end (create user, create post, create comment, delete user)

      When Create a user with given details
      |Mustafa|mustafa@gmail.com|Male|Active|
    Then Verify that user is created successfully for given details with status code 201
    When Rename created user
      |MustafaNewName|
    Then Verify that update has been made for given user with status code 201
    When Create a post for given user
      |Mustafa post title | Mustafa post body |
    Then Verify that post is created successfully for given user with status code 201

    When Create a comment for given post with given details
      | Commentar name | commentar email | comment body|
    Then Verify that comment is created successfully for given post with status code 201
    And Verify that comment is connected correct user
    When Delete created user
    Then Verify that user is deleted with status code 204

