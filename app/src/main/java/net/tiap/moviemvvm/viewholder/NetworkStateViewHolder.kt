package net.tiap.moviemvvm.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.tiap.moviemvvm.data.repo.NetworkState
import kotlinx.android.synthetic.main.item_network_state.view.*

class NetworkStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(networkState: NetworkState?) {
        if (networkState != null) {
            if (networkState == NetworkState.LOADING) {
                itemView.progress_bar.visibility = View.VISIBLE
            } else {
                itemView.progress_bar.visibility = View.GONE
            }
            if (networkState == NetworkState.ERROR || networkState == NetworkState.ENDOFLIST) {
                itemView.txt_error.visibility = View.VISIBLE
                itemView.txt_error.text = networkState.message
            } else {
                itemView.txt_error.visibility = View.GONE
            }
        } else {
            itemView.progress_bar.visibility = View.GONE
            itemView.txt_error.visibility = View.VISIBLE
            itemView.txt_error.text = "network state is null"
        }
    }
}