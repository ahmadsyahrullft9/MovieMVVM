package net.tiap.moviemvvm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import net.tiap.moviemvvm.R
import net.tiap.moviemvvm.data.repo.NetworkState
import net.tiap.moviemvvm.data.vo.Movie
import net.tiap.moviemvvm.viewholder.MovieViewHolder
import net.tiap.moviemvvm.viewholder.NetworkStateViewHolder

class PopularMoviePagedAdapter(public val context: Context) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_ITEM_TYPE = 1
    val NETWORK_ITEM_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        if (viewType == MOVIE_ITEM_TYPE) {
            val view = layoutInflater.inflate(R.layout.item_movie_list, parent, false)
            return MovieViewHolder(view)
        } else {
            val view = layoutInflater.inflate(R.layout.item_network_state, parent, false)
            return NetworkStateViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_ITEM_TYPE) {
            (holder as MovieViewHolder).onBind(getItem(position), context)
        } else {
            (holder as NetworkStateViewHolder).onBind(networkState)
        }
    }

    fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_ITEM_TYPE
        } else {
            MOVIE_ITEM_TYPE
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val prevNetworkState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow: Boolean = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && prevNetworkState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }
}