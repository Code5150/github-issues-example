package com.сode5150.mercury_task3.network.data

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Issue(
    @PrimaryKey val number: Int,
    @SerializedName("created_at") val createdAt: Date,
    @SerializedName("updated_at") val updatedAt: Date?,
    @SerializedName("closed_at") val closedAt: Date?,
    val title: String,
    val body: String,
    val user: User
): Parcelable