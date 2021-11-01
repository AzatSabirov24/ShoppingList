package com.azat_sabirov.shoppinglist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azat_sabirov.shoppinglist.HtmlManager
import com.azat_sabirov.shoppinglist.R
import com.azat_sabirov.shoppinglist.databinding.NoteListItemBinding
import com.azat_sabirov.shoppinglist.entities.NoteItem

class NoteAdapter(private val listener: Listener) : ListAdapter<NoteItem, NoteAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = NoteListItemBinding.bind(view)

        fun setData(note: NoteItem, listener: Listener) = with(binding) {
            titleTv.text = note.title
            descriptionTv.text = HtmlManager.getFromHtml(note.content.trim())
            timeTv.text = note.time

            deleteIb.setOnClickListener {
                listener.deleteItem(note.id!!)
            }

            itemView.setOnClickListener {
                listener.onClickItem(note)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.note_list_item, parent, false)
                )
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<NoteItem>() {
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener{
        fun deleteItem(id: Int)
        fun onClickItem(note: NoteItem)
    }
}