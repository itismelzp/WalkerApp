package com.demo.fragment.adapter

import android.graphics.drawable.Drawable
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.demo.R
import com.demo.fragment.GridFragment
import com.demo.fragment.ImagePagerFragment
import com.demo.fragment.adapter.ImageData.IMAGE_DRAWABLES
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by lizhiping on 2023/1/6.
 * <p>
 * description
 */
class GridAdapter(
    val fragment: Fragment,
) : RecyclerView.Adapter<GridAdapter.ImageViewHolder>() {

    private val requestManager: RequestManager = Glide.with(fragment)
    private val viewHolderListener: ViewHolderListener = ViewHolderListenerImpl(fragment)

    interface ViewHolderListener {
        fun onLoadCompleted(view: ImageView?, adapterPosition: Int)
        fun onItemClicked(view: View, adapterPosition: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.image_card, parent, false)
        return ImageViewHolder(view, requestManager, viewHolderListener)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return IMAGE_DRAWABLES.size
    }

    class ViewHolderListenerImpl(val fragment: Fragment): ViewHolderListener {

        private val enterTransitionStarted = AtomicBoolean()

        override fun onLoadCompleted(view: ImageView?, adapterPosition: Int) {
            if (GridFragment.currentPosition != adapterPosition) {
                return
            }
            if (enterTransitionStarted.getAndSet(true)) {
                return
            }
            fragment.startPostponedEnterTransition()
        }

        override fun onItemClicked(view: View, adapterPosition: Int) {
            GridFragment.currentPosition = adapterPosition
            (fragment.exitTransition as TransitionSet).excludeTarget(view, true)

            val transitioningView = view.findViewById<ImageView>(R.id.card_image)
            fragment.parentFragmentManager.commit {
                setReorderingAllowed(true) // Optimize for shared element transition
                addSharedElement(transitioningView, transitioningView.transitionName)
                replace(R.id.fragment_container, ImagePagerFragment.newInstance(),
                    ImagePagerFragment::class.simpleName)
                addToBackStack(null)
            }

        }

    }

    class ImageViewHolder(
        itemView: View,
        private val requestManager: RequestManager,
        private val viewHolderListener: ViewHolderListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val image: ImageView = itemView.findViewById(R.id.card_image)
        private val cardView: ImageView = itemView.findViewById(R.id.card_image)

        init {
            cardView.setOnClickListener(this)
        }

        fun bind() {
            setImage(absoluteAdapterPosition)
            image.transitionName = "${IMAGE_DRAWABLES[absoluteAdapterPosition]}"
        }

        private fun setImage(adapterPosition: Int) {
            requestManager
                .load(IMAGE_DRAWABLES[adapterPosition])
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(image, adapterPosition)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(image, adapterPosition)
                        return false
                    }
                })
                .into(image)
        }

        override fun onClick(v: View) {
            viewHolderListener.onItemClicked(v, absoluteAdapterPosition)
        }

    }
}