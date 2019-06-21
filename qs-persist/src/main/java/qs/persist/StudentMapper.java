package qs.persist;

import org.springframework.stereotype.Repository;
import qs.model.po.Student;

import java.util.List;
@Repository
public interface StudentMapper {

    List<Student> getAll();

    int deleteByPrimaryKey(Integer id);

    int insert(Student record);

    int insertSelective(Student record);


    Student selectByPrimaryKey(Integer id);


    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);
}