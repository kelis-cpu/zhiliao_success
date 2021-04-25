package com.example.demo.Repository;

import com.example.demo.DAO.ArticleDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<ArticleDAO,Integer> {

    @Query(nativeQuery = true,value="select * from articles order by articleID DESC limit 10")
    List<ArticleDAO>find();
    ArticleDAO findByArticleID(Integer id);
    List<ArticleDAO> findByUsrname(String username);
    @Query(nativeQuery = true,value = "select articleID from articles where usrname like CONCAT('%',:query,'%') or content like CONCAT('%',:query,'%')")
    List<ArticleDAO>search(@Param("query") String query);


}
