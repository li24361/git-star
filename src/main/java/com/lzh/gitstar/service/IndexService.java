package com.lzh.gitstar.service;

import com.lzh.gitstar.domain.dto.Index;
import com.lzh.gitstar.domain.entity.UserIndex;
import com.lzh.gitstar.repo.UserIndexRepository;
import me.zhyd.oauth.model.AuthUser;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : lizhihao
 * @since : 2019/10/19, 星期六
 **/
@Service
public class IndexService {

    @Autowired
    private UserIndexRepository userIndexRepository;

    public void saveIndex(Index index) {
        UserIndex userIndex = new UserIndex();
        BeanUtils.copyProperties(index, userIndex);
        userIndexRepository.save(userIndex);
    }

    public List<UserIndex> listAll() {
        Iterable<UserIndex> all = userIndexRepository.findAll();
        return (List<UserIndex>) all;
    }
}
