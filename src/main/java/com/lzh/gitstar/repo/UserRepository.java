package com.lzh.gitstar.repo;

import com.lzh.gitstar.domain.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : lizhihao
 * @since : 2019/10/19, 星期六
 **/
public interface UserRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByLogin(String var1);

}
