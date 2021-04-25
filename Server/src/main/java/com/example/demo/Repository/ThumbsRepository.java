package com.example.demo.Repository;

import com.example.demo.DAO.ThumbsDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThumbsRepository extends JpaRepository<ThumbsDAO,Integer> {
    List<ThumbsDAO> findAllByArticleID(Integer articleID);
}
