/********************************************************************************
 ** Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 ** All rights reserved.
 **
 ** File: - .java
 ** Description:
 **
 **
 ** Version: 1.0
 ** Date: 2018/04/09
 ** Author: yanchao.Chen@Apps.Gallery3D
 ** TAG: OPLUS_ARCH_EXTENDS
 ** ------------------------------- Revision History: ----------------------------
 ** <author>                        <date>       <version>    <desc>
 ** ------------------------------------------------------------------------------
 ** yanchao.Chen@Apps.Gallery3D     2018/04/09   1.0          OPLUS_ARCH_EXTENDS
 ********************************************************************************/
package com.oplus.face;

import android.content.Context;
import android.graphics.Rect;

import com.oplus.faceapi.FaceAttribute;
import com.oplus.faceapi.model.CvPixelFormat;
import com.oplus.faceapi.model.FaceAttrInfo;
import com.oplus.faceapi.model.FaceConfig;
import com.oplus.faceapi.model.FaceInfo;
import com.oplus.gallery.framework.abilities.scan.face.CvFaceEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FaceAttributeDetect {
    private static final String TAG = "FaceAttributeDetect";
    public static final int DEFAULT_HANDLE_STATE = 0;
    public static final int DETECTED_HANDLE_STATE = 1;
    public static final int STATISTICS_HANDLE_STATE = 2;
    private static final int RACE_YELLOW = 0;
    private static final int RACE_BLACK = 1;
    private static final int RACE_WHITE = 2;
    private static final int RACE_BROWN = 3;
    private static final int FEMALE = 0;
    private static final int MALE = 1;
    private static final int PERCENT = 100;
    private FaceAttribute mFaceAttribute = null;

    public static class FaceAttributeInfo {
        private int mAge;  //< score(0-100)
        private int mRace; //< Yellow 0, Black 1, White 2, Brown 3
        private int mSex; //1 male, 0 female
        private int mSkin; //< score(0-100)  The lower the score, the better the skin is.
        private Rect mFaceRect;

        private int mYellowScore;
        private int mBlackScore;
        private int mWhiteScore;
        private int mBrownScore;
        private int mQuality;
        private int mCover;

        public FaceAttributeInfo(FaceAttrInfo faceInfo) {
            mAge = faceInfo.age;
            mSex = faceInfo.sex;
            mRace = faceInfo.race;
            //上研sdk版本中只有cover的值是有效的
            mQuality = faceInfo.cover;
            mCover = faceInfo.cover;
        }

        public int getCover() {
            return mCover;
        }

        public float getBestScore() {
            return (float) mCover / PERCENT;
        }

        public Rect getFaceRect() {
            return mFaceRect;
        }

        public int getAge() {
            return mAge;
        }

        public int getRace() {
            List<Integer> raceScoreList = new ArrayList<>();
            raceScoreList.add(RACE_YELLOW, mYellowScore);
            raceScoreList.add(RACE_BLACK, mBlackScore);
            raceScoreList.add(RACE_WHITE, mWhiteScore);
            raceScoreList.add(RACE_BROWN, mBrownScore);
            return raceScoreList.indexOf(Collections.max(raceScoreList));
        }

        public int getSex() {
            return (mSex == MALE) ? MALE : FEMALE;
        }

        public int getSkin() {
            return mSkin;
        }
    }

    public FaceAttribute getFaceAttribute() {
        if (mFaceAttribute == null) {
            initFaceAttribute();
        }
        return mFaceAttribute;
    }

    private void initFaceAttribute() {
        init(null);
        mFaceAttribute = new FaceAttribute(null, FaceConfig.FaceImageResize.DEFAULT_CONFIG);
    }

    public boolean init(Context context) {
        CvFaceEngine.loadExistedNativeLibs();
        return true;
    }

    public void destroy() {
        if (mFaceAttribute != null) {
            mFaceAttribute.release();
            mFaceAttribute = null;
        }
    }

    public FaceAttributeInfo attribute(byte[] bgr, int imageWidth, int imageHeight, int imageStride, FaceInfo faceInfo) {
        FaceAttrInfo info = getFaceAttribute().attribute(
                bgr,
                CvPixelFormat.BGR888,
                imageWidth,
                imageHeight,
                imageStride,
                faceInfo);
        if (info != null) {
            return new FaceAttributeInfo(info);
        }
        return null;
    }
}
