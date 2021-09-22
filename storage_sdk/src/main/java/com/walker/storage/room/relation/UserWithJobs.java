package com.walker.storage.room.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.walker.storage.room.model.User;

import java.util.List;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public class UserWithJobs {

    @Embedded
    public User user;

    @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
    )
    public List<String> jobs;

}
