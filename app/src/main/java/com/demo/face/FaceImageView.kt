package com.demo.face

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View.MeasureSpec.getSize
import androidx.appcompat.widget.AppCompatImageView

/**
 * Created by lizhiping on 2023/12/20.
 *
 *
 * description
 */
class FaceImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private val facePaint = Paint()
    private val rectPaint = Paint()
    private val rect = Rect()
    private val faceRectList = mutableListOf<Rect>()
    private var width = 0
    private var height = 0

    init {
        initView()
    }

    private fun initView() {
        facePaint.isAntiAlias = true
        facePaint.isDither = true
        facePaint.style = Paint.Style.STROKE
        facePaint.strokeWidth = 5f
        facePaint.color = Color.RED
        faceRectList.add(Rect(0, 0, 200, 200))
        rectPaint.color = Color.GREEN
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 6f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = getSize(widthMeasureSpec)
        height = getSize(heightMeasureSpec)
        Log.d(
            TAG,
            "[onMeasure] mWidth: ${getSize(widthMeasureSpec)}, mHeight: ${getSize(heightMeasureSpec)}"
        )
        rect[0, 0, width] = height
        Log.d(TAG, "[onMeasure] mRect: $rect")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (rect in faceRectList) {
            canvas.drawRect(rect, facePaint)
        }
        canvas.drawRect(rect, rectPaint)
    }

    fun setFaceRectList(rectList: List<Rect>?, originWidth: Int, originHeight: Int) {
        if (rectList.isNullOrEmpty()) {
            return
        }
        faceRectList.apply {
            clear()
            addAll(rectList)
        }
        for (rect in faceRectList) {
            resizeRect(rect, originWidth, originHeight)
        }
        postInvalidate()
    }

    private fun resizeRect(rect: Rect, originWidth: Int, originHeight: Int) {
        var widthRatio = width.toFloat() / originWidth
        var heightRatio = height.toFloat() / originHeight

        var widthDiff = 0
        var heightDiff = 0
        // originHeight/originWidth > mHeight/mWidth
        if (originHeight * width > originWidth * height) {
//            widthDiff = (mWidth - ((mHeight.toFloat() / originHeight) * originWidth).toInt()) / 2
        } else {
//            heightDiff = (mHeight - ((mWidth.toFloat() / originWidth) * originHeight).toInt()) /
        }

        Log.d(
            TAG,
            "[resizeRect] [mWidth: $width, mHeight: $height], [originWidth: $originWidth, originHeight: $originHeight], widthDiff: $widthDiff, heightDiff: $heightDiff"
        )
        Log.d(
            TAG,
            "[resizeRect] before rect: $rect, width: ${rect.right - rect.left}, height: ${rect.bottom - rect.top}"
        )
        rect[(rect.left * widthRatio).toInt() + widthDiff, (rect.top * heightRatio).toInt(), (rect.right * widthRatio).toInt() - widthDiff] =
            (rect.bottom * heightRatio).toInt()
        Log.d(
            TAG,
            "[resizeRect] after rect: $rect, width: ${rect.right - rect.left}, height: ${rect.bottom - rect.top}"
        )
    }

    companion object {
        private const val TAG = "FaceImageView"
    }
}