package nn.pager.events

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import nn.pager.databinding.ItemEventBinding

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventVH>(){

    private val differCallback = object: DiffUtil.ItemCallback<EventEntity>() {
        override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
            return oldItem.title == newItem.title
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventVH {
        val binding =
            ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventVH(binding)
    }
    override fun onBindViewHolder(holder: EventVH, position: Int) {
        val item = differ.currentList[position]
        holder.binding.apply {
            if (item.desc.isNullOrBlank()) {
                itemDescription.visibility = View.GONE
                itemTitle.text = item.title
                itemTime.text = item.time
                itemCourse.text = item.course
            }
            else {
                itemTitle.text = item.title
                itemTime.text = item.time
                itemDescription.text = item.desc
                itemCourse.text = item.course

            }
        }
    }

    inner class EventVH(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}