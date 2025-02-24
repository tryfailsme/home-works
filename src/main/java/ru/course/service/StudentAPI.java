package ru.course.service;

import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.*;

class StudentAPI extends Specification{
    Response getStudent(Integer id){;
        return given()
                .spec(requestSpecification())
                .pathParam("id", id)
                .when()
                .get("/student/{id}")
                .then()
                .spec(responseSpecification())
                .log().all()
                .extract().response();
    }

    Response createStudent(Object student) {
        return given()
                .spec(requestSpecification())
                .body(student)
                .when()
                .post("/student")
                .then()
                .spec(responseSpecification())
                .log().all()
                .extract().response();
    }

    Response deleteStudent(Integer id) {
        return given()
                .spec(requestSpecification())
                .pathParam("id", id)
                .when()
                .delete("/student/{id}")
                .then()
                .spec(responseSpecification())
                .log().all()
                .extract().response();
    }

    Response getTopStudent(){;
        return given()
                .spec(requestSpecification())
                .when()
                .get("/topStudent")
                .then()
                .spec(responseSpecification())
                .log().all()
                .extract().response();
    }

}
