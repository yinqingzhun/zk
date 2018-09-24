package qs.service.implement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qs.config.db.DbChoosing;
import qs.config.db.EnumDataSourceName;
import qs.config.db.EnumDataSourceType;
import qs.model.Student;
import qs.repository.StudentRepository;
import qs.service.StudentService;

import java.util.List;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {
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
        return value;
    }

    @DbChoosing(EnumDataSourceName.USER)
    //@Transactional
    Student failedToSave(Student student) {
        Student value = studentRepository.save(student);
        int i = 1 / 0;
        return value;
    }
    @DbChoosing(EnumDataSourceName.USER)
    @Transactional
    public void generate() {
        Student student = new Student();
        student.setName(UUID.randomUUID().toString());
        student.setEnabled(true);

        try {
            failedToSave(student);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }


    @DbChoosing(value = EnumDataSourceName.TOPIC,type = EnumDataSourceType.read)
    public void 发帖(){
        读取发帖人详情();//read DB:USER
        保存帖子();//write DB:TOPIC
    }

    @DbChoosing(value = EnumDataSourceName.USER,type=EnumDataSourceType.read)
    private void 读取发帖人详情(){
    }

    @DbChoosing(value = EnumDataSourceName.TOPIC,type = EnumDataSourceType.write)
    private void 保存帖子(){
    }



}
