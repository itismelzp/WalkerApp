/********************************************************************************
 ** Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 ** All rights reserved.
 **
 ** File: - CvFaceCluster.java
 ** Description:
 **     CvFaceCluster
 **
 ** Version: 1.0
 ** Date: 2016-11-25
 ** Author: xiuhua.ke
 ** TAG: OPLUS_ARCH_EXTENDS
 ** ------------------------------- Revision History: ----------------------------
 ** <author>                        <date>       <version>    <desc>
 ** ------------------------------------------------------------------------------
 ** 80082820                       2016-11-22     1.0          OPLUS_ARCH_EXTENDS
 ********************************************************************************/
package com.oplus.gallery.framework.abilities.scan.face;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;

import com.oplus.face.FaceAttributeDetect;
import com.oplus.faceapi.FaceCluster;
import com.oplus.faceapi.FaceDetect;
import com.oplus.faceapi.FaceLibrary;
import com.oplus.faceapi.FaceVerify;
import com.oplus.faceapi.FaceVideoCluster;
import com.oplus.faceapi.model.CvPixelFormat;
import com.oplus.faceapi.model.FaceConfig;
import com.oplus.faceapi.model.FaceInfo;
import com.oplus.faceapi.model.FaceOrientation;
import com.oplus.faceapi.model.FaceVideoInfo;
import com.oplus.faceapi.model.IFaceFeatureInfo;
import com.oplus.faceapi.utils.ColorConvertUtil;
import com.oplus.gallery.framework.abilities.scan.utils.GalleryScanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CvFaceEngine {
    private static final String TAG = "CvFaceEngine";
    private static final String CLOUD_SYNC_FACE_STATE = "cloud_sync_face_state";
    private static final int PIXEL_BYTE_SIZE = 3;
    private static final int ROTATION_0 = 0;
    private static final int ROTATION_90 = 90;
    private static final int ROTATION_180 = 180;
    private static final int ROTATION_270 = 270;
    private Context mContext;
    private FaceDetect mCvFaceDetector;
    private FaceVerify mCvFaceVerify;
    private FaceCluster mCvFaceCluster;
    private FaceVideoCluster mVideoFaceCluster = null;
    private FaceAttributeDetect mFaceAttributeDetect;
    private long mTime;

    public CvFaceEngine(Context context) {
        mContext = context;
    }

    public void setFaceAttributeDetect(FaceAttributeDetect faceAttributeDetect) {
        mFaceAttributeDetect = faceAttributeDetect;
    }

    public boolean init(int defaultVersion, int currentVersion) {
        Log.d(TAG, "init defaultVersion is: " + defaultVersion + ", currentVersion is: " + currentVersion);
        mTime = System.currentTimeMillis();
        boolean result = loadDefaultComponents(defaultVersion);
        Log.d(TAG, "init time:" + (System.currentTimeMillis() - mTime) + "ms" + ",result:" + result);
        return result;
    }

    public static void loadExistedNativeLibs() {
        loadCvFaceDefaultLib();
    }

    private static int loadCvFaceDefaultLib() {
        try {
            System.loadLibrary("hci_face_album_api");
            System.loadLibrary("jni_hci_face_album_api");
        } catch (Exception e) {
            Log.e(TAG, "load hci_face_album_api and jni_hci_face_album_api failed E:" + e);
        }
        return 0;
    }

    private boolean loadDefaultComponents(int defaultVersion) {
        Log.d(TAG, "loadDefaultComponents");
        loadCvFaceDefaultLib();
        String version = initCvFaceEngine();
        FaceLibrary.setDebug(false);
        Log.d(TAG, "loadDefaultComponentsLocal = " + version);
//        if (GalleryScanUtils.setCvFaceVersion(mContext, version)) {
//            ComponentPrefUtils.setIntPref(mContext, ComponentPrefUtils.FACE_COMPONENT_VERSION_KEY, defaultVersion);
//        }
        return true;
    }

    private String initCvFaceEngine() {
        mCvFaceDetector = new FaceDetect(null, FaceConfig.FaceImageResize.DEFAULT_CONFIG, FaceConfig.FaceKeyPointCount.POINT_COUNT_106);
        mCvFaceVerify = new FaceVerify(null);
        mCvFaceCluster = new FaceCluster(null);
        mVideoFaceCluster = new FaceVideoCluster(null, null, null);
        return mCvFaceCluster.getClusterVersion();
    }

    private FaceOrientation getFaceOrientation(int rotation) {
        switch (rotation) {
            case ROTATION_0:
                return FaceOrientation.UP;
            case ROTATION_90:
                return FaceOrientation.LEFT;
            case ROTATION_180:
                return FaceOrientation.DOWN;
            case ROTATION_270:
                return FaceOrientation.RIGHT;
            default:
                return FaceOrientation.UNKNOWN;
        }
    }

    private byte[] getBitmapBits(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int thumbWidth = bitmap.getWidth();
        int thumbHeight = bitmap.getHeight();
        byte[] bgr = new byte[thumbWidth * thumbHeight * PIXEL_BYTE_SIZE];
        ColorConvertUtil.getBGRFromBitmap(bitmap, bgr);
        return bgr;
    }

    /**
     * @param mediaId
     * @param bitmap
     * @return
     */
    public ImageFeature[] getImageFeatures(long mediaId, Bitmap bitmap, int rotation) {
        if (bitmap == null) {
            return null;
        }
        int thumbWidth = bitmap.getWidth();
        int thumbHeight = bitmap.getHeight();
        // face detection
        mTime = System.currentTimeMillis();
        //get bitmap byte
        byte[] bgr = getBitmapBits(bitmap);
        FaceInfo[] faceInfos = mCvFaceDetector.detect(bgr, CvPixelFormat.BGR888, thumbWidth, thumbHeight,
            thumbWidth * PIXEL_BYTE_SIZE, getFaceOrientation(rotation));
        Log.d(TAG, "face detection time:" + (System.currentTimeMillis() - mTime) + "ms");
        if ((faceInfos == null) || (faceInfos.length <= 0)) {
            return null;
        }
        // feature extraction
        mTime = System.currentTimeMillis();
        ImageFeature[] imageFeatures = new ImageFeature[faceInfos.length];
        List<ImageFeature> list = new ArrayList<>();
        for (int i = 0; i < imageFeatures.length; i++) {
            //get image face feature and face attribute
            ImageFeature imageFeature = obtainImageFeature(bgr, faceInfos[i], thumbWidth, thumbHeight);
            if (imageFeature == null) {
                continue;
            }
            imageFeature.mIsGroupPhoto = (faceInfos.length >= GalleryScanUtils.LIMIT_FACE_COUNT_OF_GROUP_PHOTO);
            imageFeature.mMediaId = mediaId;
            imageFeatures[i] = imageFeature;
            list.add(imageFeature);
        }
        Log.d(TAG, "feature extraction time:" + (System.currentTimeMillis() - mTime) + "ms" + ", length: " + faceInfos.length
            + ", list.size: " + list.size());
        if ((!list.isEmpty()) && (list.size() != imageFeatures.length)) {
            imageFeatures = list.toArray(new ImageFeature[list.size()]);
        } else if (list.isEmpty()) {
            return null;
        }
        return imageFeatures;
    }

    private ImageFeature obtainImageFeature(byte[] bgr, FaceInfo faceInfo, int thumbWidth, int thumbHeight) {
        byte[] feature = mCvFaceVerify.getFeature(bgr, CvPixelFormat.BGR888,
            thumbWidth, thumbHeight, thumbWidth * PIXEL_BYTE_SIZE, faceInfo);
        if (feature == null) {
            Log.w(TAG, "getImageFeatures, feature is null!");
            return null;
        }
//        if (GalleryScanUtils.isTestMode()) {
            Log.i(TAG, "getImageFeatures, feature:" + Arrays.toString(feature));
//        }
        ImageFeature imageFeature = new ImageFeature();
        imageFeature.mFeature = feature;
        imageFeature.mCvFace = faceInfo;
        imageFeature.mThumbWidth = thumbWidth;
        imageFeature.mThumbHeight = thumbHeight;
        //NOTE:attribute must be called otherwise the face group method will crash!!!!
        imageFeature.setFaceAttributeInfo(mFaceAttributeDetect.attribute(bgr, thumbWidth, thumbHeight, thumbWidth * PIXEL_BYTE_SIZE, faceInfo));
        return imageFeature;
    }

    static class ImageFeature {
        long mMediaId;
        String mFilePath;
        byte[] mFeature;
        FaceInfo mCvFace;
        int mThumbWidth;
        int mThumbHeight;
        boolean mIsGroupPhoto;
        float mBestScore;
        private FaceAttributeDetect.FaceAttributeInfo mFaceAttributeInfo;

        public void setFaceAttributeInfo(FaceAttributeDetect.FaceAttributeInfo faceAttributeInfo) {
            mFaceAttributeInfo = faceAttributeInfo;
            if (mFaceAttributeInfo != null) {
                mBestScore = faceAttributeInfo.getBestScore();
            }
        }

        public FaceAttributeDetect.FaceAttributeInfo getFaceAttributeInfo() {
            return mFaceAttributeInfo;
        }

//        @SuppressLint("Range")
//        public void parseCursor(Cursor cursor) {
//            mCvFace = new FaceInfo();
//            mCvFace.id = cursor.getInt(cursor.getColumnIndex(GalleryStore.ScanFaceColumns._ID));
//            mFilePath = cursor.getString(cursor.getColumnIndex(GalleryStore.ScanFaceColumns.DATA));
//            mThumbWidth = cursor.getInt(cursor.getColumnIndex(GalleryStore.ScanFaceColumns.THUMB_W));
//            mThumbHeight = cursor.getInt(cursor.getColumnIndex(GalleryStore.ScanFaceColumns.THUMB_H));
//            mCvFace.score = cursor.getFloat(cursor.getColumnIndex(GalleryStore.ScanFaceColumns.SCORE));
//            mCvFace.yaw = cursor.getFloat(cursor.getColumnIndex(GalleryStore.ScanFaceColumns.YAW));
//            mCvFace.pitch = cursor.getFloat(cursor.getColumnIndex(GalleryStore.ScanFaceColumns.PITCH));
//            Rect rect = new Rect();
//            rect.set(cursor.getInt(cursor.getColumnIndex(GalleryStore.ScanFaceColumns.LEFT)),
//                cursor.getInt(cursor.getColumnIndex(GalleryStore.ScanFaceColumns.TOP)),
//                cursor.getInt(cursor.getColumnIndex(GalleryStore.ScanFaceColumns.RIGHT)),
//                cursor.getInt(cursor.getColumnIndex(GalleryStore.ScanFaceColumns.BOTTOM)));
//            mCvFace.faceRect = rect;
//            mBestScore = cursor.getFloat(cursor.getColumnIndex(GalleryStore.ScanFaceColumns.BEST_SCORE));
//        }

        public float getBestScore() {
            return mBestScore;
        }
    }

    /**
     * @param imageFeatureList
     * @return
     */
    public int[] group(ArrayList<ImageFeature> imageFeatureList) {
        if (imageFeatureList.isEmpty()) {
            return null;
        }
        mTime = System.currentTimeMillis();
        int imageFeatureListSize = imageFeatureList.size();
        byte[][] features = new byte[imageFeatureListSize][];
        for (int i = 0; i < imageFeatureListSize; i++) {
            features[i] = imageFeatureList.get(i).mFeature;
        }
        int[] groups = new int[imageFeatureListSize];
        int result = mCvFaceCluster.faceCluste(features, groups);
        Log.d(TAG, "group time:" + (System.currentTimeMillis() - mTime) + "ms" + ",   result:" + result);
        return groups;
    }

    /**
     * @param imageFeatureList
     * @param groups
     * @return
     */
    public int[] group(ArrayList<ImageFeature> imageFeatureList, int[] groups) {
        if (imageFeatureList.isEmpty()) {
            return null;
        }
        mTime = System.currentTimeMillis();
        int imageFeatureListSize = imageFeatureList.size();
        byte[][] features = new byte[imageFeatureListSize][];
        for (int i = 0; i < imageFeatureListSize; i++) {
            features[i] = imageFeatureList.get(i).mFeature;
        }
        int result = mCvFaceCluster.faceCluste(features, groups);
        Log.d(TAG, "increment group time:" + (System.currentTimeMillis() - mTime) + ",result:" + result);
        return groups;
    }

    /**
     * group with faceList
     *
     * @param faceList
     * @return int[]
     */
    public int[] groupFace(List<CvFaceInfo> faceList) {
        if (faceList.isEmpty()) {
            return null;
        }
        mTime = System.currentTimeMillis();
        int imageFeatureListSize = faceList.size();
        int[] groups = new int[imageFeatureListSize];
        for (int i = 0; i < imageFeatureListSize; i++) {
            groups[i] = (int) faceList.get(i).mGroupId;
        }
        List<IFaceFeatureInfo> iFaceFeatureInfoList = new ArrayList<>(faceList);
        int result = mCvFaceCluster.faceCluste(iFaceFeatureInfoList, groups);
        Log.d(TAG, "group time:" + (System.currentTimeMillis() - mTime) + ",result:" + result);
        return groups;
    }

    public int getBestCoverIndex(int groupId) {
        return mCvFaceCluster.getBestCover(groupId);
    }

    public int[] getTopBestCoverIndex(int groupId, int top) {
        return mCvFaceCluster.getBestCover(groupId, top);
    }

    /**
     * Given the two byte[] feature to get similar of two face,
     * Maybe it will throw RuntimeException when Calling cv_verify_compare_feature() method failed!
     * If run exception,you can find the reason by ResultCode
     *
     * @param feature1 of first face
     * @param feature2 of other face
     * @return similar of two face (float[0-1])
     */
    public float compareFeature(byte[] feature1, byte[] feature2) {
        return mCvFaceVerify.compareFeature(feature1, feature2);
    }

    public void release() {
        Log.d(TAG, "release");
        if (mCvFaceDetector != null) {
            mCvFaceDetector.release();
        }
        if (mCvFaceVerify != null) {
            mCvFaceVerify.release();
        }
        if (mCvFaceCluster != null) {
            mCvFaceCluster.release();
        }
        if (mVideoFaceCluster != null) {
            mVideoFaceCluster.release();
            mVideoFaceCluster = null;
        }
        if (mFaceAttributeDetect != null) {
            mFaceAttributeDetect.destroy();
            mFaceAttributeDetect = null;
        }
    }

    //------------------ for video face ---------------------//

    /**
     * @param mediaId
     * @param videoPath
     * @return ImageFeature[]
     */
    public ImageFeature[] getVideoFeatures(long mediaId, String videoPath) {
        FaceVideoInfo[] faceVideoInfos = videoDetect(videoPath);
        ImageFeature[] imageFeatures = null;
        if (faceVideoInfos != null) {
            imageFeatures = new ImageFeature[faceVideoInfos.length];
            for (int i = 0; i < faceVideoInfos.length; ++i) {
                imageFeatures[i] = faceVideoInfoToImageFeature(faceVideoInfos[i]);
                imageFeatures[i].mMediaId = mediaId;
                imageFeatures[i].mIsGroupPhoto = (faceVideoInfos.length >= GalleryScanUtils.LIMIT_FACE_COUNT_OF_GROUP_PHOTO);
            }
        }
        return imageFeatures;
    }

    private ImageFeature faceVideoInfoToImageFeature(FaceVideoInfo faceVideoInfo) {
        ImageFeature imageFeature = new ImageFeature();
        imageFeature.mFeature = faceVideoInfo.feature;
        FaceInfo info = new FaceInfo();
        info.eyeDist = faceVideoInfo.eyeDist;
        info.facePoints = faceVideoInfo.points;
        info.faceRect = faceVideoInfo.rect;
        info.id = faceVideoInfo.groupId;
        info.pitch = faceVideoInfo.pitch;
        info.roll = faceVideoInfo.roll;
        info.yaw = faceVideoInfo.yaw;
        info.score = faceVideoInfo.score;
        imageFeature.mCvFace = info;
        imageFeature.setFaceAttributeInfo(new FaceAttributeDetect.FaceAttributeInfo(faceVideoInfo.attr_info));
        return imageFeature;
    }

    /**
     * @param videoPath
     * @return videoInfoArray [face count]
     */
    private FaceVideoInfo[] videoDetect(String videoPath) {
        if (TextUtils.isEmpty(videoPath)) {
            return null;
        }
        if (mVideoFaceCluster == null) {
            Log.e(TAG, "videoDetect, mVideoFaceCluster is null");
            return null;
        }
        FaceVideoInfo[] videoInfoArray = mVideoFaceCluster.videoCluster(videoPath);
        if (videoInfoArray != null) {
            Log.d(TAG, "videoDetect, videoInfoArray.length = " + videoInfoArray.length);
        }
        return videoInfoArray;
    }
}
