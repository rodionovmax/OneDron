package com.rodionovmax.onedron.other

import android.content.Context
import androidx.databinding.InverseMethod
import com.rodionovmax.onedron.R

object Converter {
    fun dowIndexToName(context: Context, index: Int): String {
        return when (index) {
            0 -> context.getString(R.string.tagline)
            1 -> context.getString(R.string.facebook_ad)
            2 -> context.getString(R.string.elevator_pitch)
            3 -> context.getString(R.string.executive_summary)
            4 -> context.getString(R.string.improve_seo)
            5 -> context.getString(R.string.sample_review)
            6 -> context.getString(R.string.hashtags)
            7 -> context.getString(R.string.chatgpt_recommendations)
            else -> ""
        }
    }

    @InverseMethod(value = "dowIndexToName")
    fun dowNameToIndex(context: Context, name: String): Int {
        return when (name) {
            context.getString(R.string.tagline) -> 0
            context.getString(R.string.facebook_ad) -> 1
            context.getString(R.string.elevator_pitch) -> 2
            context.getString(R.string.executive_summary) -> 3
            context.getString(R.string.improve_seo) -> 4
            context.getString(R.string.sample_review) -> 5
            context.getString(R.string.hashtags) -> 6
            context.getString(R.string.chatgpt_recommendations) -> 7
            else -> -1
        }
    }

    fun objectiveToRequestCategory(context: Context, value: String): String {
        return when(value) {
            context.getString(R.string.tagline) -> context.getString(R.string.request_tagline)
            context.getString(R.string.facebook_ad) -> context.getString(R.string.request_facebook_ad)
            context.getString(R.string.elevator_pitch) -> context.getString(R.string.request_elevator_pitch)
            context.getString(R.string.executive_summary) -> context.getString(R.string.request_executive_summary)
            context.getString(R.string.improve_seo) -> context.getString(R.string.request_seo)
            context.getString(R.string.sample_review) -> context.getString(R.string.request_customer_review)
            context.getString(R.string.hashtags) -> context.getString(R.string.request_social_hashtags)
            context.getString(R.string.chatgpt_recommendations) -> context.getString(R.string.request_chatgpt_recommendation)
            else -> ""
        }
    }
}