package com.demo.animator

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.animation.ValueAnimator
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.widget.AppCompatImageView

class BorderGlowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private var borderWidth: Int = 20
    private val startColor = Color.parseColor("#FFFFFF")
    private val endColor = Color.parseColor("#EFC695")
    private val glowAnimator: ValueAnimator = ValueAnimator.ofInt(0, 255).apply {
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        duration = 4000
        addUpdateListener {
            val alpha = it.animatedValue as Int
            val interpolatedColor = ArgbEvaluator().evaluate(alpha / 255f, startColor, endColor) as Int
            paint.color = interpolatedColor
            invalidate()
        }
    }

    private val rectF: RectF = RectF()
    private var gradient: LinearGradient? = null
    private var gradientOffset: Float = 0f
    private val gradientAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        repeatCount = ValueAnimator.INFINITE
        duration = 5000
        addUpdateListener {
            gradientOffset = it.animatedValue as Float
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable
        if (drawable == null) return

        if (rectF.isEmpty) {
            rectF.set(0f, 0f, width.toFloat(), height.toFloat())
        }

        val bitmap = (drawable as BitmapDrawable).bitmap
        val roundBitmap = getRoundedCornerBitmap(bitmap, 20)
        canvas.drawBitmap(roundBitmap, 0f, 0f, null)

        paint.strokeWidth = borderWidth.toFloat()

        if (gradient == null) {
            gradient = LinearGradient(
                rectF.left, rectF.top, rectF.right, rectF.top,
                startColor, endColor, Shader.TileMode.CLAMP
            )
        }

        val matrix = Matrix()
        matrix.setRotate(gradientOffset * 360, rectF.centerX(), rectF.centerY())
        gradient?.setLocalMatrix(matrix)

        paint.shader = gradient
        canvas.drawRoundRect(rectF, 20f, 20f, paint)
    }

    private fun getRoundedCornerBitmap(bitmap: Bitmap, radius: Int): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint().apply {
            isAntiAlias = true
        }

        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        canvas.drawRoundRect(rectF, radius.toFloat(), radius.toFloat(), paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    fun startGlowAnimation() {
        glowAnimator.start()
        gradientAnimator.start()
    }

    fun stopGlowAnimation() {
        glowAnimator.cancel()
        gradientAnimator.cancel()
    }
}




