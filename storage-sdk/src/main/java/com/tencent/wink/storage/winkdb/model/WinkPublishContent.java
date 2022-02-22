package com.tencent.wink.storage.winkdb.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


import com.tencent.wink.storage.winkdb.converter.StringListTypeConverter;

import java.util.HashMap;
import java.util.List;

/**
 * 发布页数据类
 * <p>
 * Created by walkerzpli on 2021/8/10.
 */
@Entity(tableName = "wink_publish_content_table", indices = {@Index(value = {"missionId"}, unique = true)})
@TypeConverters({StringListTypeConverter.class})
public class WinkPublishContent {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "uin")
    public long uin;

    @ColumnInfo(name = "draftKey")
    public String mDraftKey;

    @ColumnInfo(name = "content")
    public String mContent;

    @NonNull
    @ColumnInfo(name = "missionId")
    public String mMissionId;

    @ColumnInfo(name = "selectedMedia")
    public List<String> mSelectedMedia;

    @ColumnInfo(name = "createTime")
    public long mCreateTime;

    @ColumnInfo(name = "hasSelectedCover")
    public boolean mHasSelectedCover;

    @ColumnInfo(name = "clientKey")
    public String mClientKey;

    @ColumnInfo(name = "isSyncQQ")
    public boolean isSyncQQ;

    @ColumnInfo(name = "goodsId")
    public String mGoodsId;

    @ColumnInfo(name = "goodsName")
    public String mGoodsName;

    @ColumnInfo(name = "taskId")
    public long mTaskId;

    @ColumnInfo(name = "clientTraceId")
    public String mClientTraceId;

    // 存储额外信息，避免数据库频繁加字段升级
    @ColumnInfo(name = "extraInfo")
    public HashMap<String, String> mExtraInfo;

    public WinkPublishContent(@NonNull String missionId) {
        this.mMissionId = missionId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("WinkPublishContent{");
        sb.append("id=").append(id);
        sb.append(", uin=").append(uin);
        sb.append(", mDraftKey='").append(mDraftKey).append('\'');
        sb.append(", mContent='").append(mContent).append('\'');
        sb.append(", mMissionId='").append(mMissionId).append('\'');
        sb.append(", mSelectedMedia=").append(mSelectedMedia);
        sb.append(", mCreateTime=").append(mCreateTime);
        sb.append(", mHasSelectedCover=").append(mHasSelectedCover);
        sb.append(", mClientKey='").append(mClientKey).append('\'');
        sb.append(", isSyncQQ=").append(isSyncQQ);
        sb.append(", mGoodsId='").append(mGoodsId).append('\'');
        sb.append(", mGoodsName='").append(mGoodsName).append('\'');
        sb.append(", mTaskId=").append(mTaskId);
        sb.append(", mClientTraceId='").append(mClientTraceId).append('\'');
        sb.append(", mExtraInfo=").append(mExtraInfo);
        sb.append('}');
        return sb.toString();
    }
}
