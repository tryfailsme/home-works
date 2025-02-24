package ru.course.service;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TopStudentTest {
    private static StudentDTO student;
    private static StudentAPI api;
    private static int id, mark;
    private static ArrayList<Integer> marks;
    private static StudentDTO firstStd, secondStd, thirdStd, fourthStd;

    @BeforeAll
    static void up() {
        api = new StudentAPI();
        id = (int) (Math.random() * Integer.MAX_VALUE);

        firstStd = new StudentDTO(id, "scratchy", List.of(5, 5, 5, 4));
        secondStd = new StudentDTO(id + 1, "scratchier", List.of(5, 4, 5, 4));
        thirdStd = new StudentDTO(id + 2, "scratchiest", List.of(5, 5, 4, 4));
        fourthStd = new StudentDTO(id + 3, "mega_scratchy", List.of(5, 4, 5, 4, 5, 4));
    }

    @AfterEach
    void down() {
        api.deleteStudent(id);
        api.deleteStudent(id + 1);
        api.deleteStudent(id + 2);
        api.deleteStudent(id + 3);
    }

    @Test
    public void getTopStudentNullTest() {
        Response response = api.getTopStudent();
        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.asString(), equalTo(""));
    }

    @Test
    public void getTopStudentTwoTest() {
        api.createStudent(firstStd);
        api.createStudent(secondStd);
        Response response = api.getTopStudent();

        StudentDTO[] students = response.as(StudentDTO[].class);
        assertThat(response.statusCode(), equalTo(200));
        assertThat(firstStd.getId(), equalTo(students[0].getId()));
        assertThat(firstStd.getName(), equalTo(students[0].getName()));
        assertThat(firstStd.getMarks(), equalTo(students[0].getMarks()));
    }


    @Test
    public void getTopStudentTwoSameTest() {
        api.createStudent(secondStd);
        api.createStudent(thirdStd);
        Response response = api.getTopStudent();

        StudentDTO[] students = response.as(StudentDTO[].class);
        assertThat(response.statusCode(), equalTo(200));
        assertThat(secondStd.getId(), equalTo(students[0].getId()));
        assertThat(secondStd.getName(), equalTo(students[0].getName()));
        assertThat(secondStd.getMarks(), equalTo(students[0].getMarks()));
        assertThat(thirdStd.getId(), equalTo(students[1].getId()));
        assertThat(thirdStd.getName(), equalTo(students[1].getName()));
        assertThat(thirdStd.getMarks(), equalTo(students[1].getMarks()));
    }

    @Test
    public void getTopStudentTwoDifferent() {
        api.createStudent(secondStd);
        api.createStudent(fourthStd);
        Response response = api.getTopStudent();

        StudentDTO[] students = response.as(StudentDTO[].class);
        assertThat(response.statusCode(), equalTo(200));
        assertThat(fourthStd.getId(), equalTo(students[0].getId()));
        assertThat(fourthStd.getName(), equalTo(students[0].getName()));
        assertThat(fourthStd.getMarks(), equalTo(students[0].getMarks()));
    }
}