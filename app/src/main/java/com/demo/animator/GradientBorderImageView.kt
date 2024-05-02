package com.demo.animator

/**
 * description
 * <p>
 * Created by walkerzpli on 2024/4/5.
 */
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

class GradientBorderImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    private val gradientColors = intArrayOf(Color.parseColor("#FFFFFF"), Color.parseColor("#EFC695"))
    private val gradientPositions = floatArrayOf(0f, 1f)
    private var gradientAngle = 0f
    private val gradientShader = LinearGradient(0f, 0f, 0f, height.toFloat(), gradientColors, gradientPositions, Shader.TileMode.CLAMP)
    private val paint = Paint().apply {
        isAntiAlias = true
        shader = gradientShader
    }

    private val path = Path()
    private val rectF = RectF()
    private val cornerRadius = 50f // 圆角半径

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        startGradientAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        path.reset()
        rectF.set(0f, 0f, width.toFloat(), height.toFloat())
        path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW)
        canvas.clipPath(path)
        canvas.drawPath(path, paint)
    }

    private fun startGradientAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 360f)
        animator.duration = 5000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener { valueAnimator ->
            gradientAngle = valueAnimator.animatedValue as Float
            updateGradientShader()
            invalidate()
        }
        animator.start()
    }

    private fun updateGradientShader() {
        val matrix = Matrix()
        matrix.setRotate(gradientAngle, width / 2f, height / 2f)
        gradientShader.setLocalMatrix(matrix)
    }
}
