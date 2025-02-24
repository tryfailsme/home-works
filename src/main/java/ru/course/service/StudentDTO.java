package ru.course.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class StudentDTO {
    Integer id;
    String name;
    List<Integer> marks;
//    StudentDTO(String name, List<Integer> marks){
//        this(null, name, marks);
//    }
//    StudentDTO(Integer id, String name, List<Integer> marks){
//        this.id = id;
//        this.name =name;
//        this.marks = marks;
//    }
}