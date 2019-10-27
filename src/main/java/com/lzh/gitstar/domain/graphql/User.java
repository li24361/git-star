/**
  * Copyright 2019 bejson.com 
  */
package com.lzh.gitstar.domain.graphql;

import java.util.Date;

/**
 * Auto-generated: 2019-10-17 22:26:50
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@lombok.Data
public class User {

    private String avatarUrl;
    private Followers followers;
    private String company;
    private Date createdAt;
    private Organizations organizations;
    private Repositories repositories;
    private Repositories repositoriesContributedTo;
    private Repositories topRepositories;
    private Repositories pinnedItems;
    private ContributionsCollection contributionsCollection;


}