/********************************************************************************
 ** Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 ** All rights reserved.
 **
 ** File: - CvFaceInfo.java
 ** Description:
 **     CvFaceInfo
 **
 ** Version: 1.0
 ** Date: 2016-11-21
 ** Author: xiuhua.ke
 ** TAG: OPLUS_ARCH_EXTENDS
 ** ------------------------------- Revision History: ----------------------------
 ** <author>                        <date>       <version>    <desc>
 ** ------------------------------------------------------------------------------
 ** xiuhua.ke                       2016-11-21     1.0          OPLUS_ARCH_EXTENDS
 ********************************************************************************/
package com.oplus.gallery.framework.abilities.scan.face;

import android.graphics.Rect;

import com.oplus.faceapi.model.IFaceFeatureInfo;
import com.oplus.gallery.framework.abilities.scan.utils.GalleryScanUtils;

import java.util.ArrayList;

public class CvFaceInfo implements IFaceFeatureInfo {
    public long mId;
    public boolean mIsChosen;
    public Rect mRect;
    //group
    public long mGroupId = GalleryScanUtils.GROUP_ID_0;
    public String mGroupName = null;
    public int mGroupVisible;
    public int mThumbWidth;
    public int mThumbHeight;
    //add for path and single face filter
    public String mData;
    float mBestScore;
    float mScore;
    float mYaw;
    float mPitch;
    float mRoll;
    float mEyeDist;
    boolean mIsSmallFace;
    //image
    long mDateTaken;
    long mDateModify;
    long mDateAdded;
    long mMediaId;
    int mMediaType;
    //add for feature
    byte[] mFeature;
    boolean mIsSingleFace;
    float mLuminance;
    long mFaceScanDate;

//    public static ArrayList<CvFaceInfo> buildFaceInfoList(Cursor cursor) {
//        ArrayList<CvFaceInfo> list = new ArrayList<>();
//        final int groupIdIndex = cursor.getColumnIndex(GalleryStore.ScanFace.GROUP_ID);
//        final int bestScoreIndex = cursor.getColumnIndex(GalleryStore.ScanFace.BEST_SCORE);
//        final int eyeDistIndex = cursor.getColumnIndex(GalleryStore.ScanFace.EYE_DIST);
//        final int idIndex = cursor.getColumnIndex(GalleryStore.ScanFace._ID);
//        final int pitchIndex = cursor.getColumnIndex(GalleryStore.ScanFace.PITCH);
//        final int rollIndex = cursor.getColumnIndex(GalleryStore.ScanFace.ROLL);
//        final int scoreIndex = cursor.getColumnIndex(GalleryStore.ScanFace.SCORE);
//        final int yawIndex = cursor.getColumnIndex(GalleryStore.ScanFace.YAW);
//        final int isChosenIndex = cursor.getColumnIndex(GalleryStore.ScanFace.IS_CHOSEN);
//        while (cursor.moveToNext()) {
//            CvFaceInfo face = new CvFaceInfo();
//            face.mGroupId = cursor.getLong(groupIdIndex);
//            face.mBestScore = cursor.getFloat(bestScoreIndex);
//            face.mEyeDist = cursor.getFloat(eyeDistIndex);
//            face.mId = cursor.getLong(idIndex);
//            face.mPitch = cursor.getFloat(pitchIndex);
//            face.mRoll = cursor.getFloat(rollIndex);
//            face.mScore = cursor.getFloat(scoreIndex);
//            face.mYaw = cursor.getFloat(yawIndex);
//            face.mIsChosen = cursor.getFloat(isChosenIndex) == 1;
//            list.add(face);
//        }
//        return list;
//    }
//
//    public static ArrayList<CvFaceInfo> buildFaceInfoListWithFeature(Cursor cursor) {
//        ArrayList<CvFaceInfo> list = new ArrayList<>();
//        final int idIndex = cursor.getColumnIndex(GalleryStore.ScanFace._ID);
//        final int groupIdIndex = cursor.getColumnIndex(GalleryStore.ScanFace.GROUP_ID);
//        final int featureIndex = cursor.getColumnIndex(GalleryStore.ScanFace.FEATURE);
//        final int bestScoreIndex = cursor.getColumnIndex(GalleryStore.ScanFace.BEST_SCORE);
//        final int isSingleFaceIndex = cursor.getColumnIndex(GalleryStore.ScanFace.IS_SINGLE_FACE);
//        final int dataIndex = cursor.getColumnIndex(GalleryStore.ScanFace.DATA);
//        final int thumbWIndex = cursor.getColumnIndex(GalleryStore.ScanFace.THUMB_W);
//        final int thumbHIndex = cursor.getColumnIndex(GalleryStore.ScanFace.THUMB_H);
//        final int leftIndex = cursor.getColumnIndex(GalleryStore.ScanFace.LEFT);
//        final int topIndex = cursor.getColumnIndex(GalleryStore.ScanFace.TOP);
//        final int rightIndex = cursor.getColumnIndex(GalleryStore.ScanFace.RIGHT);
//        final int bottomIndex = cursor.getColumnIndex(GalleryStore.ScanFace.BOTTOM);
//        final int mediaTypeIndex = cursor.getColumnIndex(GalleryStore.ScanFace.MEDIA_TYPE);
//
//        while (cursor.moveToNext()) {
//            CvFaceInfo face = new CvFaceInfo();
//            face.mId = cursor.getLong(idIndex);
//            face.mGroupId = cursor.getLong(groupIdIndex);
//            face.mFeature = cursor.getBlob(featureIndex);
//            face.mBestScore = cursor.getFloat(bestScoreIndex);
//            face.mIsSingleFace = cursor.getInt(isSingleFaceIndex) == 1;
//            face.mData = cursor.getString(dataIndex);
//            face.mThumbWidth = cursor.getInt(thumbWIndex);
//            face.mThumbHeight = cursor.getInt(thumbHIndex);
//            if (cursor.getInt(mediaTypeIndex) == GalleryStore.LocalMediaColumns.MEDIA_TYPE_IMAGE) {
//                face.mMediaType = GalleryStore.LocalMediaColumns.MEDIA_TYPE_IMAGE;
//            } else if (cursor.getInt(mediaTypeIndex) == GalleryStore.LocalMediaColumns.MEDIA_TYPE_VIDEO) {
//                face.mMediaType = GalleryStore.LocalMediaColumns.MEDIA_TYPE_VIDEO;
//            }
//            face.mRect = new Rect();
//            face.mRect.left = cursor.getInt(leftIndex);
//            face.mRect.top = cursor.getInt(topIndex);
//            face.mRect.right = cursor.getInt(rightIndex);
//            face.mRect.bottom = cursor.getInt(bottomIndex);
//            list.add(face);
//        }
//        return list;
//    }
//
//    public static ArrayList<CvFaceInfo> buildFaceRectInfoList(Cursor cursor) {
//        ArrayList<CvFaceInfo> list = new ArrayList<>();
//        final int idIndex = cursor.getColumnIndex(GalleryStore.ScanFace._ID);
//        final int thumbWIndex = cursor.getColumnIndex(GalleryStore.ScanFace.THUMB_W);
//        final int thumbHIndex = cursor.getColumnIndex(GalleryStore.ScanFace.THUMB_H);
//        final int leftIndex = cursor.getColumnIndex(GalleryStore.ScanFace.LEFT);
//        final int topIndex = cursor.getColumnIndex(GalleryStore.ScanFace.TOP);
//        final int rightIndex = cursor.getColumnIndex(GalleryStore.ScanFace.RIGHT);
//        final int bottomIndex = cursor.getColumnIndex(GalleryStore.ScanFace.BOTTOM);
//        while (cursor.moveToNext()) {
//            CvFaceInfo face = new CvFaceInfo();
//            face.mId = cursor.getLong(idIndex);
//            face.mThumbWidth = cursor.getInt(thumbWIndex);
//            face.mThumbHeight = cursor.getInt(thumbHIndex);
//            face.mRect = new Rect();
//            face.mRect.left = cursor.getInt(leftIndex);
//            face.mRect.top = cursor.getInt(topIndex);
//            face.mRect.right = cursor.getInt(rightIndex);
//            face.mRect.bottom = cursor.getInt(bottomIndex);
//            list.add(face);
//        }
//        return list;
//    }

    public Rect getRect() {
        return mRect;
    }

    public void setGroupId(long groupId) {
        mGroupId = groupId;
    }

    public long getId() {
        return mId;
    }

    public boolean isChosen() {
        return mIsChosen;
    }

    public void setChosen(boolean chosen) {
        mIsChosen = chosen;
    }

    @Override
    public byte[] getFeature() {
        return mFeature;
    }

    @Override
    public void setGroupId(int groupId) {
        mGroupId = groupId;
    }

    @Override
    public int getGroupId() {
        return (int) mGroupId;
    }

    @Override
    public void setFeatureScore(float score) {
        //reserved for future use
    }

    public float getQuality() {
        return 0;
    }
}
