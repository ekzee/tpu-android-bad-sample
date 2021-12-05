package ru.tpu.imgur.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.tpu.imgur.NotificationsPresenter
import ru.tpu.imgur.R

private const val VIEW_TYPE_NOTIFICATIONS = 0
private const val VIEW_TYPE_ALBUM_ITEM = 1

class AlbumSearchAdapter :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        NotificationsPresenter.Widget {

    private var albums: List<UiImgurAlbum>? = null

    override var showNotificationClickListener: (() -> Unit)? = null
    override var alarmManagerClickListener: (() -> Unit)? = null
    override var workManagerClickListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_NOTIFICATIONS -> {
                NotificationsItemViewHolder(
                        layoutInflater.inflate(R.layout.item_notifications, parent, false)
                )
            }
            VIEW_TYPE_ALBUM_ITEM -> {
                AlbumItemViewHolder(
                        layoutInflater.inflate(R.layout.item_image_with_description, parent, false)
                )
            }
            else -> {
                throw IllegalArgumentException("view type is $viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_NOTIFICATIONS -> {
                val notificationsHolder = holder as NotificationsItemViewHolder
                notificationsHolder.showNotificationBtn.setOnClickListener {
                    showNotificationClickListener?.invoke()
                }
                notificationsHolder.alarmManagerBtn.setOnClickListener {
                    alarmManagerClickListener?.invoke()
                }
                notificationsHolder.workManagerBtn.setOnClickListener {
                    workManagerClickListener?.invoke()
                }
            }
            VIEW_TYPE_ALBUM_ITEM -> {
                val albumHolder = holder as AlbumItemViewHolder
                albumHolder.showImage(albums!![position])
            }
        }
    }

    override fun getItemCount(): Int {
        return (albums?.size ?: 0) + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_NOTIFICATIONS
        } else {
            VIEW_TYPE_ALBUM_ITEM
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(albums: List<UiImgurAlbum>?) {
        this.albums = albums
        notifyDataSetChanged()
    }
}

