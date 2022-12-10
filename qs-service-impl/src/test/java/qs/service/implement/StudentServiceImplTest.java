package qs.service.implement;

import org.junit.Before;
import org.junit.Test;
import qs.model.po.Student;
import qs.persist.StudentMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class StudentServiceImplTest {

    private StudentServiceImpl studentServiceImplUnderTest;

    @Before
    public void setUp() {
        studentServiceImplUnderTest = new StudentServiceImpl();
        studentServiceImplUnderTest.studentMapper = mock(StudentMapper.class);
    }

    @Test
    public void testGetAll() {
        // Setup
        // Configure StudentMapper.getAll(...).
        final Student student = new Student();
        student.setId(0);
        student.setName("name");
        student.setAge(0);
        student.setPhone("phone");
        final List<Student> students = Arrays.asList(student);
        when(studentServiceImplUnderTest.studentMapper.getAll()).thenReturn(students);

        // Run the test
        final List<Student> result = studentServiceImplUnderTest.getAll();

        // Verify the results

    }

    @Test
    public void testGetAll_StudentMapperReturnsNoItems() {
        // Setup
        when(studentServiceImplUnderTest.studentMapper.getAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<Student> result = studentServiceImplUnderTest.getAll();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    public void testGet() {
        // Setup
        // Configure StudentMapper.selectByPrimaryKey(...).
        final Student student = new Student();
        student.setId(0);
        student.setName("name");
        student.setAge(0);
        student.setPhone("phone");
        when(studentServiceImplUnderTest.studentMapper.selectByPrimaryKey(0)).thenReturn(student);

        // Run the test
        final Student result = studentServiceImplUnderTest.get(0);

        // Verify the results
    }

    @Test
    public void testSave() {
        // Setup
        final Student student = new Student();
        student.setId(0);
        student.setName("name");
        student.setAge(0);
        student.setPhone("phone");

        when(studentServiceImplUnderTest.studentMapper.updateByPrimaryKeySelective(any(Student.class))).thenReturn(0);
        when(studentServiceImplUnderTest.studentMapper.insertSelective(any(Student.class))).thenReturn(0);

        // Run the test
        final Student result = studentServiceImplUnderTest.save(student);

        // Verify the results
        verify(studentServiceImplUnderTest.studentMapper).updateByPrimaryKeySelective(any(Student.class));
        //verify(studentServiceImplUnderTest.studentMapper).insertSelective(any(Student.class));
    }
}
