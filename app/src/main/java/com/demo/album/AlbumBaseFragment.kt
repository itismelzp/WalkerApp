package com.demo.album

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.R
import com.demo.customview.utils.ViewUtils
import com.demo.logger.MyLog

abstract class AlbumBaseFragment : Fragment(){
    companion object {
        private const val TAG = "BaseFragment"
        private const val EXIT_ANIMATION_DURATION = 333L
        private val SLIDE_RIGHT_ENTER_ANIMATION = R.anim.coui_open_slide_enter
        private val SLIDE_LEFT_EXIT_ANIMATION = R.anim.coui_open_slide_exit
        private val SLIDE_LEFT_ENTER_ANIMATION = R.anim.coui_close_slide_enter
        private val SLIDE_RIGHT_EXIT_ANIMATION = R.anim.coui_close_slide_exit

        /**
         * 动画名带oplus_rounded_corners前缀表示页面在动画过程左/右上角会带圆角，系统实现，在R及R以上生效
         * 动效确认Q及Q以下可以不需要该圆角效果
         */
        private val SLIDE_FROM_BOTTOM_ENTER_ANIMATION = R.anim.oplus_rounded_corners_base_fragment_push_up

        //底下的Fragment没有进行实际的动画，但为避免背景白屏，时长要大于等于上层fragment动画的时长
        private val FADE_EXIT_ANIMATION = R.anim.base_fragment_exit_empty_anim
        private val FADE_ENTER_ANIMATION = R.anim.base_fragment_enter_empty_anim

        private val SLIDE_TO_BOTTOM_EXIT_ANIMATION = R.anim.oplus_rounded_corners_base_fragment_push_down

        val DEFAULT_ANIM_ARRAY: IntArray = intArrayOf(
            SLIDE_RIGHT_ENTER_ANIMATION,
            SLIDE_LEFT_EXIT_ANIMATION,
            SLIDE_LEFT_ENTER_ANIMATION,
            SLIDE_RIGHT_EXIT_ANIMATION
        )

        val FROM_BOTTOM_ANIM_ARRAY: IntArray = intArrayOf(
            SLIDE_FROM_BOTTOM_ENTER_ANIMATION,
            FADE_EXIT_ANIMATION,
            FADE_ENTER_ANIMATION,
            SLIDE_TO_BOTTOM_EXIT_ANIMATION
        )

        private val TRANLATION_DELAY = 100L
        private val ALPHA_THIRTY_PERSENT = 80
        private val ALPHA_FORTY_PERSENT = 102
    }

    protected val clazzSimpleName: String by lazy { this.javaClass.simpleName }

    val window: Window?
        get() = activity?.window


    protected open val isPageClickPenetrableEnabled: Boolean = true

    var fragmentState: FragmentState? = null

    var animationEnable = true
    open var needRoundBackgroundOnAnimation: Boolean = false

    /**
     * 当 [AlbumBaseFragment.onDestroy] 执行时，是否需要执行 viewModelStore.clear，用于清理 ViewModel
     *
     * 默认值为 true，即默认会清理
     *
     * Marked，非 常规用法，因为相册 MVVM 使用有些飞，不得不以此兼容；相册大图界面使用常规 MVVM 实现，不需要清理，故添加此标志位用于拦截
     */
    protected open val needClearViewModelStoreOnDestroy: Boolean = true


    protected var contentView: View? = null
        private set
    private var isReuseView = false

    private var mResumeTime: Long = 0

    private val clickListener = View.OnClickListener {
        // 拦截界面穿透点击事件
        MyLog.d(TAG, "clickListener, Intercept click events")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyLog.d(TAG, "onCreate: fragment:<$clazzSimpleName> ,tag : $tag.")
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MyLog.d(TAG, "onCreateView: fragment:<${this@AlbumBaseFragment.javaClass.simpleName}> ,tag : $tag, contentView: $contentView.")
        return contentView?.apply {
            // 如果进行转场动画返回会出现contentView已存在parent而无法重新add的问题, 需在此强行中断动画
            (this.parent as? ViewGroup)?.endViewTransition(this)
            isReuseView = true
            MyLog.d(TAG, "onCreateView: fragment:<${this@AlbumBaseFragment.javaClass.simpleName}> is reuse last one view.")
        } ?: inflater.inflate(getLayoutId(), container, false).apply {
            isReuseView = false
            this@AlbumBaseFragment.contentView = this
        }
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkNecessaryPermission()
        if (!isReuseView) {
            (contentView as? ViewGroup)?.isMotionEventSplittingEnabled = false
            if (isPageClickPenetrableEnabled) {
                contentView?.setOnClickListener(clickListener)
            } else {
                contentView?.isClickable = false
            }
            doViewCreated(view, savedInstanceState)
        }
    }

    //这个函数在Fragment出栈时可能不会被调用，注册逻辑放在这里要慎重
    open fun doViewCreated(view: View, savedInstanceState: Bundle?) {
        checkIfNeedShowPrivacyDialog()
    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        MyLog.d(TAG, "onResume: <$clazzSimpleName>")
        mResumeTime = System.currentTimeMillis()
        if (hasOptionsMenu()) {
            activity?.invalidateOptionsMenu()
        }
    }

    override fun onPause() {
        super.onPause()
        MyLog.d(TAG, "onPause: <$clazzSimpleName>")
        if (mResumeTime > 0) {
            mResumeTime = 0
        }
    }

    protected open fun getUserActionCurPage(trackType: String? = null): String? = null

    override fun onDestroyView() {
        super.onDestroyView()
        MyLog.d(TAG, "onDestroyView: <$clazzSimpleName>")
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentState?.removeParent()
        contentView = null
        if (needClearViewModelStoreOnDestroy) {
            viewModelStore.clear()
        }
        MyLog.d(TAG, "onDestroy: <$clazzSimpleName>")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        MyLog.d(TAG, "onCreateOptionsMenu: <$clazzSimpleName>")
    }

    fun sendResult(resultCode: Int, intent: Intent?) {
        targetFragment?.onActivityResult(targetRequestCode, resultCode, intent) ?: run {
            activity?.setResult(resultCode, intent)
        }
    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return ObserverFactory(lifecycle, super.getDefaultViewModelProviderFactory())
    }

    private fun checkNecessaryPermission() {

    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return if (animationEnable || (nextAnim == Resources.ID_NULL)) {
            null
        } else {
            // 多次pop会出现多页面的pop动画，通过将pop动画替换为一个333ms的无操作动画，避免动画混乱
            // 如果要顶替其他动画，duration要改为对应动画持续时间
            ValueAnimator.ofInt(0, 0).apply {
                duration = EXIT_ANIMATION_DURATION
            }
        }
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        // 参考onCreateAnimator里的逻辑
        if (!animationEnable || (nextAnim == Resources.ID_NULL)) {
            return null
        }
        val animation: Animation?
        try {
            animation = AnimationUtils.loadAnimation(context, nextAnim)
        } catch (e: Resources.NotFoundException) {
            MyLog.e(TAG, "onCreateAnimation. $clazzSimpleName loadAnimation failed, transit=$transit, enter=$enter, nextAnim=$nextAnim", e)
            return null
        }
        animation?.setAnimationListener(object : Animation.AnimationListener {
            private var originTranslationZ = 0f
            override fun onAnimationStart(animation: Animation) {
                originTranslationZ = view?.translationZ ?: 0f
                view?.translationZ = animation.zAdjustment.toFloat()

                when (nextAnim) {
                    SLIDE_LEFT_EXIT_ANIMATION, SLIDE_LEFT_ENTER_ANIMATION -> {
                        view?.foreground = ColorDrawable(Color.BLACK)
                        val animator = if (nextAnim == SLIDE_LEFT_EXIT_ANIMATION) {
                            ObjectAnimator.ofInt(view?.foreground, "alpha", 0, ALPHA_THIRTY_PERSENT)
                        } else {
                            ObjectAnimator.ofInt(view?.foreground, "alpha", ALPHA_THIRTY_PERSENT, 0)
                        }
                        animator.duration = animation.duration
                        animator.start()
                    }
                    FADE_ENTER_ANIMATION, FADE_EXIT_ANIMATION -> {
                        view?.foreground = ColorDrawable(Color.BLACK)
                        val animator = if (nextAnim == FADE_EXIT_ANIMATION) {
                            ObjectAnimator.ofInt(view?.foreground, "alpha", 0, ALPHA_FORTY_PERSENT)
                        } else {
                            ObjectAnimator.ofInt(view?.foreground, "alpha", ALPHA_FORTY_PERSENT, 0)
                        }
                        animator.duration = animation.duration
                        animator.start()
                    }
                }
                updateRoundBackground(isTransiting = true)
            }

            override fun onAnimationEnd(animation: Animation) {
                view?.postDelayed(
                    { view?.translationZ = originTranslationZ },
                    TRANLATION_DELAY
                )

                when (nextAnim) {
                    SLIDE_LEFT_EXIT_ANIMATION, SLIDE_LEFT_ENTER_ANIMATION -> {
                        view?.foreground = null
                    }
                }
                updateRoundBackground(isTransiting = false)
            }

            override fun onAnimationRepeat(animation: Animation) {
                // do nothing
            }
        })

        return animation
    }

    private fun updateRoundBackground(isTransiting: Boolean) {
        if (!needRoundBackgroundOnAnimation) return

        view?.apply {
            // 仅在非分屏模式下，且在转场动画过程中需要圆角
            val cornerRadius = ViewUtils.dpToPx(4)
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline) {
                    outline.setRoundRect(Rect(0, 0, width, height), cornerRadius.toFloat())
                }
            }
            clipToOutline = true
        }
    }


    /**
     * 当前业务逻辑必须使用必要权限才可以的时候，使用它
     */
    open fun onNecessaryPermissionOK() {
    }


    abstract fun getLayoutId(): Int

//    @Suppress("UNCHECKED_CAST", "LongParameterList")
//    fun <T : AlbumBaseFragment> startByStack(
//        @FragmentStartType startType: Int? = null,
//        resId: Int = R.id.base_fragment_container,
//        postCard: PostCard,
//        tag: String = DEFAULT_TAG,
//        requestCode: Int? = null,
//        data: Bundle? = null,
//        anim: IntArray = intArrayOf(
//            Resources.ID_NULL,
//            Resources.ID_NULL,
//            Resources.ID_NULL,
//            Resources.ID_NULL
//        )
//    ): T? {
//        return (RouterManager.routerCenter.getRouter(postCard)?.getRouterClass() as? Class<out AlbumBaseFragment>)?.run {
//            startByStack(startType, resId, this, tag, requestCode, data, anim)
//        }
//    }
//
//    @Suppress("UNCHECKED_CAST", "LongParameterList")
//    fun <T : AlbumBaseFragment> startByStack(
//        @FragmentStartType startType: Int? = null,
//        resId: Int = R.id.base_fragment_container,
//        fragmentClass: Class<out AlbumBaseFragment>,
//        tag: String = DEFAULT_TAG,
//        requestCode: Int? = null,
//        data: Bundle? = null,
//        anim: IntArray = NO_ANIM_ARRAY
//    ): T? {
//        return (activity as? BaseActivity)?.run {
//            supportFragmentManager.start(
//                startType,
//                resId,
//                fragmentClass,
//                tag,
//                requestCode,
//                data,
//                this,
//                true,
//                anim
//            ) as? T
//        }
//    }

    private class ObserverFactory(
        val lifecycle: Lifecycle,
        private val factory: ViewModelProvider.Factory
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factory.create(modelClass).also {
                if (it is LifecycleObserver) {
                    lifecycle.addObserver(it)
                }
            }
        }
    }


    /**
     * 是否需要隐私政策授权弹框，只适用于单个授权
     */
    open fun checkIfNeedShowPrivacyDialog() {
        if (TextUtils.isEmpty(getPrivacyAuthorizeType())) {
            return
        }
        if (!isPrivacyAuthorized(getPrivacyAuthorizeType())) {
            context?.apply {
                MyLog.d(TAG, "checkIfNeedShowPrivacyDialog, showPrivacyDialog")
                showPrivacyDialog(this)
            }
        }
    }

    /**
     * 显示隐私政策授权框，具体的显示逻辑由子类重写
     */
    open fun showPrivacyDialog(context: Context) {}

    /**
     * 隐私政策的功能类型，由子类重写
     */
    open fun getPrivacyAuthorizeType(): String = ""

    /**
     * 是否同意传入的隐私权限
     * 没配置能力对象，即ability为空，返回false
     * 配置能力对象，但是接口返回空，返回false
     * 最后根据授权标志位返回记录的boolean值
     */
    private fun isPrivacyAuthorized(privacyType: String = getPrivacyAuthorizeType()): Boolean {
        return true
    }


}