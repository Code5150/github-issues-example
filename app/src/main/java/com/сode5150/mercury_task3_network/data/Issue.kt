package com.сode5150.mercury_task3_network.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Issue(
    val body: String,
    @SerializedName("closed_at")
    val closedAt: Date?,
    @SerializedName("created_at")
    val createdAt: Date,
    val number: Int,
    val title: String,
    @SerializedName("updated_at")
    val updatedAt: Date?,
    val user: User
): Parcelable