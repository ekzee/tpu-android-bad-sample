package ru.tpu.imgur

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.*
import ru.tpu.imgur.ui.UiImgurAlbum

class AlbumSearchPresenter(
        private val repository: AlbumSearchRepository,
        private val lifecycle: Lifecycle,
        private val widget: Widget
) : DefaultLifecycleObserver {
    init {
        lifecycle.addObserver(this)
        widget.retryLoadingListener = {
            loadCats()
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        loadCats()
    }

    private fun loadCats() {
        widget.showLoading()
        lifecycle.coroutineScope.launch {
            val uiAlbumsDeferred = async(Dispatchers.IO) {
                return@async try {
                    Result.success(repository.search("cats"))
                } catch (e: Throwable) {
                    Result.failure(e)
                }
            }
            uiAlbumsDeferred.await().fold(
                    onSuccess = { uiAlbums: List<UiImgurAlbum> -> widget.showLoaded(uiAlbums) },
                    onFailure = { widget.showFailed() }
            )
        }
    }

    interface Widget {

        var retryLoadingListener: (() -> Unit)?

        fun showLoading()

        fun showLoaded(uiAlbums: List<UiImgurAlbum>)

        fun showFailed()
    }
}