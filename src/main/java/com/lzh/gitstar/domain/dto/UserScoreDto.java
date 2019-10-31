package com.lzh.gitstar.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: lizhihao
 * @Date: 2019/10/30
 */
@Data
@Builder
public class UserScoreDto {

    private String userName;

    private Double score;

    private Long rank;


}
