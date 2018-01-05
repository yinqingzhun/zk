package qs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import qs.model.Student;
@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
}
