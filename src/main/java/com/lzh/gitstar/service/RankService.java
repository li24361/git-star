package com.lzh.gitstar.service;

import com.lzh.gitstar.domain.dto.UserScoreDto;
import com.lzh.gitstar.domain.entity.UserIndex;
import com.lzh.gitstar.repo.UserIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @Author: lizhihao
 * @Date: 2019/10/30
 */
@Service
public class RankService {

    public static final String KEY = "RANKING";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserIndexRepository userIndexRepository;

    @PostConstruct
    public void init() {
        //todo 数量太多要完蛋
        ArrayList<UserIndex> all = (ArrayList<UserIndex>)userIndexRepository.findAll();
        if (!CollectionUtils.isEmpty(all)) {
            all.stream()
                .map(u -> UserScoreDto.builder().userName(u.getLogin()).score(Double.valueOf(u.getAllScore()*-1)).build())
                .forEach(userScoreDto -> {
                    putUser(userScoreDto);
                });
        }
    }

    public void putUser(UserScoreDto userScore) {
        redisTemplate.opsForZSet().add(KEY, userScore.getUserName(), userScore.getScore());
    }

    public List<UserScoreDto> list() {
        Set<DefaultTypedTuple> range = redisTemplate.opsForZSet().rangeWithScores(KEY, 0, 100);
        AtomicLong i = new AtomicLong(1);
        return range.stream().map(t -> UserScoreDto.builder().rank(i.getAndIncrement()).userName((String)t.getValue()).score(t.getScore()).build()).collect(Collectors.toList());
    }

    public UserScoreDto getUserRank(String userName) {
        Long rank = redisTemplate.opsForZSet().rank(KEY, userName);
        return UserScoreDto.builder().userName(userName).rank(rank).build();
    }

}
