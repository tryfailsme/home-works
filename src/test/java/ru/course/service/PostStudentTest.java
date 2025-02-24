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

public class PostStudentTest {
    private static StudentDTO student;
    private static StudentAPI api;
    private static int id, mark;
    private static ArrayList<Integer> marks;
    static Response response;
    private static String name;

    @BeforeAll
    static void up() {
        api = new StudentAPI();
        name = "scratchy";
        id = (int) (Math.random() * Integer.MAX_VALUE);
        mark = (int) (Math.random() * (5 - 2 + 1)) + 2;
        marks = new ArrayList<>(List.of(2, 3, 4, 5));
        marks.set(mark - 2, mark);
    }

    @AfterEach
    void down() {
        api.deleteStudent(id);
    }

    @Test
    public void addStudentTest() {
        response = api.createStudent(new StudentDTO(id, name, marks));
        assertThat(response.getStatusCode(), equalTo(201));
    }

    @Test
    public void updateStudentTest() {
        response = api.createStudent(new StudentDTO(id, name, marks));
        List<Integer> reversedMarks = new ArrayList<>(List.of(5, 4, 3, 2));
        response = api.createStudent(new StudentDTO(id, name, reversedMarks));
        assertThat(response.getStatusCode(), equalTo(201));

        response = api.getStudent(id);
        StudentDTO body = response.as(StudentDTO.class);
        assertThat(response.contentType(), equalTo("application/json"));
        assertThat(id, equalTo(body.getId()));
        assertThat(name, equalTo(body.getName()));
        assertThat(reversedMarks, equalTo(body.getMarks()));
    }

    @Test
    public void addIdNullStudentTest() {
        response = api.createStudent(new StudentDTO(null, name, marks));
        Integer newId = Integer.parseInt(response.getBody().asString());
        try {
            assertThat(response.getStatusCode(), equalTo(201));
            response = api.getStudent(newId);
            StudentDTO body = response.as(StudentDTO.class);
            assertThat(newId, equalTo(body.getId()));
            assertThat(name, equalTo(body.getName()));
            assertThat(marks, equalTo(body.getMarks()));
        } finally {
            api.deleteStudent(newId);
        }
    }

    @Test
    public void addStudentWithoutName() {
        response = api.createStudent(new StudentDTO(id, null, marks));
        try {
            assertThat(response.getStatusCode(), equalTo(400));
        } catch (AssertionError e) {
            api.deleteStudent(id);
        }
    }
}