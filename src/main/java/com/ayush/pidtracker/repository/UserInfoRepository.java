package com.ayush.pidtracker.repository;

import com.ayush.pidtracker.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByName(String username);

    @Query("SELECT u FROM UserInfo u WHERE u.id = ?1")
    Optional<UserInfo> findUserById(Integer Id);

    @Query("SELECT u FROM UserInfo u WHERE u.name = ?1")
    Optional<UserInfo> findUserByName(String name);

    @Query("SELECT u FROM UserInfo u WHERE u.currently_reviewing = ?1 and u.name <> ?2")
    Optional<UserInfo> getUserByStatus(Boolean status , String name);

    @Modifying
    @Query("UPDATE UserInfo u SET u.currently_reviewing = ?2 WHERE u.name = ?1")
    int changeUserStatus(String name, Boolean status);



}
