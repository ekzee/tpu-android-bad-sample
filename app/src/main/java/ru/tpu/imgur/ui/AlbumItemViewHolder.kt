package ru.tpu.imgur.ui

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import ru.tpu.imgur.R
import kotlin.math.min

private const val MAX_HEIGHT_PHOTO_SCREEN_FACTOR = 0.8f

class AlbumItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val photo: SimpleDraweeView = itemView.findViewById(R.id.photo_view)
    private val description: TextView = itemView.findViewById(R.id.description)

    fun showImage(image: UiImgurAlbum) {
        val resizeOptions = ResizeOptions(
            getWindowSize(photo.context).x,
            getPhotoViewHeight(
                getWindowSize(photo.context),
                image.width,
                image.height
            )
        )
        val params: ViewGroup.LayoutParams = photo.layoutParams
        params.height = resizeOptions.height
        params.width = resizeOptions.width
        photo.layoutParams = params

        val request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(image.url))
            .setResizeOptions(resizeOptions)
            .build()
        val controller = Fresco.newDraweeControllerBuilder()
            .setOldController(photo.controller)
            .setAutoPlayAnimations(true)
            .setImageRequest(request)
            .build()
        photo.controller = controller

        description.text = image.description
    }

    private fun getPhotoViewHeight(displaySize: Point, photoWidth: Int, photoHeight: Int): Int {
        val aspect = photoHeight / photoWidth.toFloat()
        // Ищем высоту ВЬЮ с учётом аспекта картинки.
        val neededHeight = (aspect * displaySize.x).toInt()
        // Ищем максимально дозволеную высоту картинки.
        val maxHeight = (displaySize.y * MAX_HEIGHT_PHOTO_SCREEN_FACTOR).toInt()
        return min(neededHeight, maxHeight)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun getWindowSize(context: Context): Point {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size)
        } else {
            size.x = display.width
            size.y = display.height
        }
        return size
    }
}