package com.netlogistik.reservas_back.repository;

import com.netlogistik.reservas_back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "select * from user a where a.email =:email", nativeQuery = true)
    User findByEmail(String email);
}
