package com.example.demo.Repository;

import com.example.demo.DAO.CommentDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface CommentRepository extends JpaRepository<CommentDAO, Integer> {
    List<CommentDAO> findByArticleID(Integer id);
}
