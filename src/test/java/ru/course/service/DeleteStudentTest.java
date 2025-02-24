package ru.course.service;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DeleteStudentTest {
    private static StudentDTO student;
    private static StudentAPI api;
    private static int id, mark;
    private static ArrayList<Integer> marks;
    private static String name;

    @BeforeAll
    static void fromScratch() {
        api = new StudentAPI();
        name = "scratchy";
        id = (int) (Math.random() * Integer.MAX_VALUE);
        mark =  (int) (Math.random() * (5-2+1))+2;
        marks = new ArrayList<>(List.of(2, 3, 4, 5));
        marks.set(mark-2,mark);
        api.createStudent(new StudentDTO(id, name, marks));
    }

    @AfterAll
    static void down() {
        api.deleteStudent(id);
    }

    @Test
    public void deleteStudentTest() {
        Response response = api.deleteStudent(id);
        assertThat(response.statusCode(), equalTo(200));
    }

    @Test
    public void deleteStudent404Test() {
        api.deleteStudent(id);
        Response response = api.deleteStudent(id);
        assertThat(response.statusCode(), equalTo(404));
    }
}