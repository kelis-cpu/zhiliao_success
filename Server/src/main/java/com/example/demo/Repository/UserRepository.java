package com.example.demo.Repository;

import com.example.demo.DAO.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<UserDAO,Integer> {

    @Transactional
    public UserDAO findByUsername(String user_name);
}
