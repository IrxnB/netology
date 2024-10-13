package com.example.netology

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netology.databinding.FragmentStatsBinding
import com.example.netology.databinding.StatsItemBinding


class Stats : Fragment() {

    private lateinit var binding: FragmentStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val db = DbHelper(requireContext()).readableDatabase
        binding = FragmentStatsBinding.inflate(inflater, container, false)

        val cursor = db.query("games",
            arrayOf("success_count", "total_count"),
            null,
            null,
            null,
            null,
            "created_at DESC",
            "10")

        val list = mutableListOf<StatItem>()

        cursor.moveToFirst()
        do {
            val success = cursor.getInt(0)
            val total = cursor.getInt(1)
            list.add(StatItem(success, total, success * 100 / total ))
        } while (cursor.moveToNext())



        binding.list.adapter = ItemsViewAdapter(list)
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    inner class ItemsViewAdapter(private val items: MutableList<StatItem>, ): RecyclerView.Adapter<ItemsViewAdapter.ItemViewHolder>() {

        lateinit var onItemClick: (Int) -> Unit

        inner class ItemViewHolder(val binding: StatsItemBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: StatItem) {
                binding.percentValue.text = item.percent.toString() + "%"
                binding.successValue.text = item.success.toString()
                binding.totalValue.text = item.total.toString()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = StatsItemBinding.inflate(inflater, parent, false)
            return ItemViewHolder(binding)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(items[position])
        }
    }

}