package qs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qs.model.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    List<Student> getList();


    Student save(Student student);


    void generate();
}
