package qs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qs.model.Student;
import qs.repository.StudentRepository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Service
public class StudentService {
    Logger logger = LoggerFactory.getLogger(StudentService.class);
    @Autowired
    StudentRepository studentRepository;

    public List<Student> getList() {
        List<Student> list = studentRepository.findAll();
        return list;
    }

    @Transactional
    public Student save(Student student) {
        Student value = studentRepository.save(student);
        int i = 1 / 0;
        return value;
    }


    public void generate() {
        Student student = new Student();
        student.setClassId(1);
        student.setName(UUID.randomUUID().toString());
        student.setEnabled(true);

        try {
            save(student);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
}
