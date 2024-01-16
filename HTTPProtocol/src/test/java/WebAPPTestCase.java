import org.junit.jupiter.api.Test;

public class WebAPPTestCase {

    /*
        Given
            HTTP
                headers:
                    content-type: application/json
                method: POST
                path: /api/users

            User info:
            {
                "email": "abc@gmail.com",
                "name": "abc",
                "password": "hello",
            }

        When
            Register a user

        Then
            HTTP
                status code: 200
                headers:
                    content-type: application/json
                    content-encoding: UTF-8

            User info:
            {
                "id": 1,
                "email": "abc@gmail.com",
                "name": "abc"
            }
     */
    @Test
    void UserRegistration(){
    }

    /*
        // {statusCode: 400, headers: {content-type: plain/text, content-encoding: UTF-8}, responseBody: Duplicate email}
        // {statusCode: 400, headers: {content-type: plain/text, content-encoding: UTF-8}, responseBody: Registration's format incorrect.}
        Given
            HTTP
                headers:
                    content-type: application/json
                method: POST
                path: /api/users

            User info:
            {
                "email": "abc@gmail.com",
                "name": "abc",
                "password": "hello",
            }

        When
            Register a user

        Then
            HTTP
                status code: {statusCode}
                headers: {headers}

            User info: {responseBody}
     */
    @Test
    void UserRegistrationOnFail(){

    }

    /*
        Given
            HTTP
                headers:
                    content-type: application/json
                method: POST
                path: /api/users/login

            User info:
            {
                "email": "abc@gmail.com",
                "password": "hello",
            }

        When
            User login

        Then
            HTTP
                status code: 200
                headers:
                    content-type: application/json
                    content-encoding: UTF-8

            User info:
            {
                "id": 1,
                "email": "abc@gmail.com",
                "name": "abc",
                "token": "67cbbe1b-0c10-46e4-b4a8-9e8505c2453b"
            }
     */
    @Test
    void UserLogin(){

    }

    /*
        // {statusCode: 400, headers: {content-type: plain/text, content-encoding: UTF-8}, responseBody: Credentials Invalid}
        // {statusCode: 400, headers: {content-type: plain/text, content-encoding: UTF-8}, responseBody: Login's format incorrect.}
        Given
            HTTP
                headers:
                    content-type: application/json
                method: POST
                path: /api/users

            User info:
            {
                "email": "abc@gmail.com",
                "password": "hello",
            }

        When
            User login

        Then
            HTTP
                status code: {statusCode}
                headers: {headers}

            User info: {responseBody}
     */
    @Test
    void userLoginOnFail(){

    }

    /*
        Given
            HTTP
                headers:
                    content-type: application/json
                    Authorization: Bearer <token>
                method: PATCH
                path: /api/users/1

            User info:
            {
                "newName": "newAbc"
            }

        When
            User rename

        Then
            HTTP
                status code: 204
     */
    @Test
    void UserRename(){

    }

    /* // {statusCode: 401, headers: {content-type: plain/text, content-encoding: UTF-8}, responseBody: Can't authenticate who you are.}
       // {statusCode: 403, headers: {content-type: plain/text, content-encoding: UTF-8}, responseBody: Forbidden}
       // {statusCode: 400, headers: {content-type: plain/text, content-encoding: UTF-8}, responseBody: Name's format invalid.}
        Given
            HTTP
                headers:
                    content-type: application/json
                    Authorization: Bearer <token>
                method: PATCH
                path: /api/users/0

            User info:
            {
                "newName": "newAbc"
            }

        When
            User rename

        Then
            HTTP
                status code: {statusCode}
                headers: {headers}

            User info: {responseBody}
     */
    @Test
    void UserRenameOnFail(){

    }

    /*
        Given
            HTTP
                headers:
                    Authorization: Bearer <token>
                method: GET
                path: /api/users
                query: keyword=abc

        When
            User query

        Then
            HTTP
                status code: 200
                headers:
                    content-type: application/json
                    content-encoding: UTF-8

            User info:
            [
                {
                    "id": 0,
                    "email": "abc@gmail.com",
                    "name": "abc",
                },
                {
                    "id": 1,
                    "email": "def@gmail.com",
                    "name": "def",
                }
            ]
     */
    @Test
    void UserQuery(){

    }

    /*
        Given
            HTTP
                headers:
                    Authorization: Bearer <token>
                method: GET
                path: /api/users
                query: keyword=abc

        When
            User query

        Then
            HTTP
                statusCode: 401
                headers:
                    content-type: plain/text
                    content-encoding: UTF-8
                responseBody:
                    Can't authenticate who you are.
     */
    @Test
    void UserQueryOnFail(){

    }
}
