package ru.course.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetStudentTest {
    private static StudentDTO student;
    private static StudentAPI api;
    private static int id, mark;
    private static ArrayList<Integer> marks;
    private static String name;

    @BeforeAll
    static void up() {
        api = new StudentAPI();
        name = "scratchy";
        id = (int) (Math.random() * Integer.MAX_VALUE);
    }

    @AfterEach
    void down() {
        api.deleteStudent(id);
    }

    @Test
    public void getStudentTest() {
        mark =  (int) (Math.random() * (5-2+1))+2;
        marks = new ArrayList<>(List.of(2, 3, 4, 5));
        marks.set(mark-2,mark);
        api.createStudent(new StudentDTO(id, name, marks));
        Response response = api.getStudent(id);
        StudentDTO body = response.as(StudentDTO.class);

        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(response.contentType(), equalTo("application/json"));
        assertThat(id, equalTo(body.getId()));
        assertThat(name, equalTo(body.getName()));
        assertThat(marks, equalTo(body.getMarks()));
    }

    @Test
    public void getStudent404() {
        Response response = api.getStudent(--id);
        assertThat(response.contentType(), equalTo(""));
        assertThat(response.getStatusCode(), equalTo(404));
        assertThat(response.body().asString(), equalTo(""));
    }
}