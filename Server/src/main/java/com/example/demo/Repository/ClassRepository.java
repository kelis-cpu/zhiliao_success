package com.example.demo.Repository;

import com.example.demo.DAO.ClassDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassRepository extends JpaRepository<ClassDAO,Integer> {

    public List<ClassDAO>findByToSubject(String subject);
    public ClassDAO findByClassID(Integer classId);
}
