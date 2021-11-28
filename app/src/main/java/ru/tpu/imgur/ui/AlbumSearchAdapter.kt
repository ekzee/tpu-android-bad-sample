package ru.tpu.imgur.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.tpu.imgur.R

class AlbumSearchAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	private var albums: List<UiImgurAlbum>? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		return AlbumItemViewHolder(
			layoutInflater.inflate(R.layout.item_image_with_description, parent, false)
		)
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		val albumHolder = holder as AlbumItemViewHolder
		albumHolder.showImage(albums!![position])
	}

	override fun getItemCount(): Int {
		return albums?.size ?: 0
	}

	@SuppressLint("NotifyDataSetChanged")
	fun setItems(albums: List<UiImgurAlbum>?) {
		this.albums = albums
		notifyDataSetChanged()
	}
}

