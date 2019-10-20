package com.lzh.gitstar.repo;

import com.lzh.gitstar.domain.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * @author : lizhihao
 * @since : 2019/10/19, 星期六
 **/
public interface UserRepository extends CrudRepository<UserInfo, Long> {
}
