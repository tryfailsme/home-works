package ru.course.service;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

class Specification {
    static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .addFilter(new RequestLoggingFilter())
                .setBaseUri("http://localhost:8080/")
                .setContentType(ContentType.JSON)
                .build();
    }
    static ResponseSpecification responseSpecification() {
        return new ResponseSpecBuilder()
                .build();
    }
}
