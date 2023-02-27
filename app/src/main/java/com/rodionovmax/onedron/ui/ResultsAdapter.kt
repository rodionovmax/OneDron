package com.rodionovmax.onedron.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rodionovmax.onedron.databinding.ResultItemBinding
import com.rodionovmax.onedron.model.ResultDto

class ResultsAdapter(private val results: List<String>) :
    RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        /*with(holder) {
            with(results[position]) {
                binding.tvResult.text = this
            }
        }*/
        holder.bind(results[position])
    }

    override fun getItemCount() = results.size

    inner class ResultViewHolder(val binding: ResultItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: String) {
            binding.tvResult.text = result
        }
    }
}
