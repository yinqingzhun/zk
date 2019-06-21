package qs.service.implement;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qs.config.db.DbChoosing;
import qs.config.db.EnumDataSourceName;
import qs.model.po.Student;
import qs.persist.StudentMapper;
import qs.service.StudentService;

import java.util.List;
 
@DbChoosing(value=EnumDataSourceName.USER )
@Service
public class StudentServiceImpl implements StudentService {
    Logger logger = LoggerFactory.getLogger(StudentService.class);
    @Autowired
    StudentMapper studentMapper;

    public List<Student> getAll() {
        List<Student> list = studentMapper.getAll();
        return list;
    }

    @Override
    public Student get(int id) {
        return studentMapper.selectByPrimaryKey(id);
    }


    @Transactional
    public Student save(Student student) {
        Preconditions.checkNotNull(student);
        if (student.getId() != null)
            studentMapper.updateByPrimaryKeySelective(student);
        else
            studentMapper.insertSelective(student);
        
        return student;
    }

  

 
 


}
