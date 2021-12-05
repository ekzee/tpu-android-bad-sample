package ru.tpu.imgur

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.tpu.imgur.databinding.FragmentImgurAlbumsBinding
import ru.tpu.imgur.ui.AlbumSearchAdapter
import ru.tpu.imgur.ui.UiImgurAlbum

class ImgurAlbumsFragment : Fragment(), AlbumSearchPresenter.Widget {

    val searchRepository: AlbumSearchRepository = App.scope.albumSearchRepository

    private var _binding: FragmentImgurAlbumsBinding? = null
    private val binding: FragmentImgurAlbumsBinding
        get() = _binding!!

    private var _adapter: AlbumSearchAdapter? = null
    private val adapter: AlbumSearchAdapter
        get() = _adapter!!

    override var retryLoadingListener: (() -> Unit)? = null

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
            retryLoadingListener?.invoke()
        }

        AlbumSearchPresenter(
                searchRepository,
                lifecycle,
                this
        )

        NotificationsPresenter(
                requireContext(),
                adapter,
                App.scope.commonChannelId
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    override fun showLoading() {
        binding.loadingContainer.visibility = View.VISIBLE
        binding.loading.visibility = View.VISIBLE
        binding.errorContainer.visibility = View.GONE
    }

    override fun showLoaded(uiAlbums: List<UiImgurAlbum>) {
        binding.loadingContainer.visibility = View.GONE

        adapter.setItems(uiAlbums)

        binding.list.scrollToPosition(0)
    }

    override fun showFailed() {
        binding.loadingContainer.visibility = View.VISIBLE
        binding.loading.visibility = View.GONE
        binding.errorContainer.visibility = View.VISIBLE
    }
}