package com.lzh.gitstar.repo;

import com.lzh.gitstar.domain.entity.UserIndex;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author : lizhihao
 * @since : 2019/10/19, 星期六
 **/
public interface UserIndexRepository extends CrudRepository<UserIndex, Long> {

//    public List<UserIndex> findAllOrderByAllScoreDesc();
}
