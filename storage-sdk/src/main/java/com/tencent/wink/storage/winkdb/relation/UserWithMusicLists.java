package com.tencent.wink.storage.winkdb.relation;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.tencent.wink.storage.winkdb.model.MusicList;
import com.tencent.wink.storage.winkdb.model.User;

import java.util.List;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public class UserWithMusicLists {

    @Embedded
    public User user;

    @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
    )
    public List<MusicList> musicListList;

    @NonNull
    @Override
    public String toString() {
        return "UserWithMusicLists{" +
                "user=" + user +
                ", musicListList=" + musicListList +
                '}';
    }
}
