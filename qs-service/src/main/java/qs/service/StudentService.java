package qs.service;

import qs.model.po.Student;

import java.util.List;

public interface StudentService {

    List<Student> getAll();
    
    Student get(int id);


    Student save(Student student);


   
}
