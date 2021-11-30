package ru.tpu.imgur

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tpu.imgur.api.ImgurApi
import ru.tpu.imgur.api.ImgurApiAlbum
import ru.tpu.imgur.api.ImgurApiImage
import ru.tpu.imgur.api.ImgurAppAuthInterceptor
import ru.tpu.imgur.databinding.FragmentImgurAlbumsBinding
import ru.tpu.imgur.ui.AlbumSearchAdapter
import ru.tpu.imgur.ui.UiImgurAlbum

class ImgurAlbumsFragment : Fragment() {

    val imgurApi: ImgurApi

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ImgurAppAuthInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgur.com/3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        imgurApi = retrofit.create(ImgurApi::class.java)
    }

    private var _binding: FragmentImgurAlbumsBinding? = null
    private val binding: FragmentImgurAlbumsBinding
        get() = _binding!!

    private var _adapter: AlbumSearchAdapter? = null
    private val adapter: AlbumSearchAdapter
        get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _adapter = AlbumSearchAdapter()
        _binding = FragmentImgurAlbumsBinding.inflate(inflater, container, false)

        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = adapter

        binding.errorRetry.setOnClickListener {
            load()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    private fun load() {
        showLoading()
        GlobalScope.launch {
            try {
                delay(2000)
                val apiAlbums = imgurApi.search("cats", "jpg").execute().body()!!.data!!
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
                MainScope().launch {
                    showLoaded(uiAlbums)
                }
            } catch (e: Exception) {
                MainScope().launch {
                    showFailed()
                }
            }
        }
    }

    private fun showLoading() {
        binding.loadingContainer.visibility = View.VISIBLE
        binding.loading.visibility = View.VISIBLE
        binding.errorContainer.visibility = View.GONE
    }

    private fun showLoaded(uiAlbums: List<UiImgurAlbum>) {
        binding.loadingContainer.visibility = View.GONE

        adapter.setItems(uiAlbums)

        binding.list.scrollToPosition(0)
    }

    private fun showFailed() {
        binding.loadingContainer.visibility = View.VISIBLE
        binding.loading.visibility = View.GONE
        binding.errorContainer.visibility = View.VISIBLE
    }
}