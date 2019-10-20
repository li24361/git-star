/**
  * Copyright 2019 bejson.com 
  */
package com.lzh.gitstar.domain.graphql;

import com.lzh.gitstar.common.FormulaConst;
import lombok.Data;

/**
 * Auto-generated: 2019-10-17 22:26:50
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Nodes {

    private String nameWithOwner;
    private int forkCount;
    private PrimaryLanguage primaryLanguage;
    private Stargazers stargazers;
    private Watchers watchers;


    public Float getStarIndex() {
        return stargazers.getTotalCount() * FormulaConst.starRadio + watchers.getTotalCount() * FormulaConst.watchRadio + forkCount * FormulaConst.forkRadio;
    }

}