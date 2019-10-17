/**
  * Copyright 2019 bejson.com 
  */
package com.lzh.gitstar.domain.graphql;
import lombok.Data;

import java.util.Date;

/**
 * Auto-generated: 2019-10-17 22:26:50
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class RateLimit {

    private int cost;
    private int nodeCount;
    private int remaining;
    private Date resetAt;

}