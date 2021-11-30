package ru.tpu.imgur

import androidx.annotation.WorkerThread
import ru.tpu.imgur.api.ImgurApi
import ru.tpu.imgur.api.ImgurApiAlbum
import ru.tpu.imgur.api.ImgurApiImage
import ru.tpu.imgur.ui.UiImgurAlbum

class AlbumSearchRepository(
    private val imgurApi: ImgurApi
) {
    @WorkerThread
    fun search(query: String): List<UiImgurAlbum> {
        val apiAlbums = imgurApi.search(query, "jpg").execute().body()!!.data!!
        val uiAlbums = apiAlbums.mapNotNull { imgurApiAlbum: ImgurApiAlbum ->
            imgurApiAlbum.images?.forEach { imgurApiImage: ImgurApiImage ->
                if (imgurApiImage.type != "image/jpeg" && imgurApiImage.type != "image/png") {
                    return@forEach
                }
                return@mapNotNull UiImgurAlbum(
                    url = imgurApiImage.link,
                    width = imgurApiImage.width,
                    height = imgurApiImage.height,
                    description = imgurApiAlbum.title
                )
            }
            return@mapNotNull null
        }
        return uiAlbums
    }
}