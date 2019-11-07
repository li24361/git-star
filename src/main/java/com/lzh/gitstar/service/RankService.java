package com.lzh.gitstar.service;

import com.lzh.gitstar.domain.dto.UserRank;
import com.lzh.gitstar.domain.dto.UserScoreDto;
import com.lzh.gitstar.domain.entity.UserIndex;
import com.lzh.gitstar.repo.UserIndexRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
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
        clearUser();
        List<UserIndex> all = userIndexRepository.findAll(PageRequest.of(0, 200)).getContent();
        if (!CollectionUtils.isEmpty(all)) {
            all.stream()
                .forEach(userIndex -> {
                    putUser(userIndex);
                });
        }
    }

    public void putUser(UserIndex userIndex) {
        redisTemplate.opsForZSet().add(KEY, userIndex.getLogin(), userIndex.getAllScore());
        redisTemplate.opsForValue().set(userIndex.getLogin(), userIndex);
    }

    public void clearUser() {
        redisTemplate.delete(KEY);
    }

    public List<UserRank> listByCache() {
        Set<DefaultTypedTuple> range = redisTemplate.opsForZSet().reverseRangeWithScores(KEY, 0, 199);
        AtomicLong i = new AtomicLong(1);
        return range.stream()
            .map(t -> redisTemplate.opsForValue().get(t.getValue()))
            .filter(Objects::nonNull)
            .map(u -> {
                UserRank userRank = new UserRank();
                BeanUtils.copyProperties(u, userRank);
                userRank.setRank(i.getAndIncrement());
                return userRank;
            })
            .collect(Collectors.toList());
    }

    public UserScoreDto getUserRank(String userName) {
        Long rank = redisTemplate.opsForZSet().rank(KEY, userName);
        return UserScoreDto.builder().userName(userName).rank(rank).build();
    }

}
