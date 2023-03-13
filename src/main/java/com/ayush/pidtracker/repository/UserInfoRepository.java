package com.ayush.pidtracker.repository;

import com.ayush.pidtracker.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByName(String username);

    @Query("SELECT u FROM UserInfo u WHERE u.id = ?1")
    Optional<UserInfo> findUserById(Integer Id);

    @Query("SELECT u FROM UserInfo u WHERE u.currently_reviewing = ?1 and u.id <> ?2")
    Optional<UserInfo> getUserByStatus(Boolean status , Integer Id);

//    @Modifying
//    @Query("update UserInfo u set u.status = ?2 where u.id = ?1")
//    int changeUserStatus(Integer id, Boolean status);



}
