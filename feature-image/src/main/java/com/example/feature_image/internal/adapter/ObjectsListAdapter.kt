package com.example.feature_image.internal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core_database.entities.ObjectOnImage
import com.example.core_database.entities.ObjectOnImageType
import com.example.feature_image.R
import com.example.feature_image.databinding.ItemObjectsListBinding

internal class ObjectsListAdapter(
    private val objectsList: List<Pair<String, ObjectOnImage>>,
    private val filterCallback: (type: ObjectOnImageType) -> Unit,
    private val locateCallback: (key: String) -> Unit
) : RecyclerView.Adapter<ObjectsListAdapter.ObjectViewHolder>() {

    inner class ObjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemObjectsListBinding = ItemObjectsListBinding.bind(view)

        fun bind(position: Int) {
            val currentObject = objectsList[position]

            binding.objectTypeText.text = currentObject.first

            binding.filterByObjectButton.setOnClickListener {
                filterCallback.invoke(currentObject.second.type)
            }

            binding.locateObjectButton.setOnClickListener {
                locateCallback.invoke(currentObject.first)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectViewHolder =
        ObjectViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_objects_list,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = objectsList.size

    override fun onBindViewHolder(holder: ObjectViewHolder, position: Int) {
        holder.bind(position)
    }
}