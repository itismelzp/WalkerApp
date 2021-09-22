package com.walker.storage.room.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.walker.storage.room.model.Library;
import com.walker.storage.room.model.User;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public class UserAndLibrary {

    @Embedded
    public User user;

    @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
    )
    public Library library;

    @Override
    public String toString() {
        return "UserAndLibrary{" +
                "user=" + user +
                ", library=" + library +
                '}';
    }
}
