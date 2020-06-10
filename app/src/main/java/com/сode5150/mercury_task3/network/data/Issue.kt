package com.сode5150.mercury_task3.network.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

const val ISSUES_TABLE_NAME: String = "issues"
@Parcelize
@Entity(tableName = ISSUES_TABLE_NAME)
data class Issue(
    @PrimaryKey val number: Int,
    @SerializedName("created_at") @ColumnInfo(name = "created_at") val createdAt: Date,
    @SerializedName("updated_at") @ColumnInfo(name = "updated_at") val updatedAt: Date?,
    @SerializedName("closed_at") @ColumnInfo(name = "closed_at") val closedAt: Date?,
    val title: String,
    val body: String,
    @Embedded val user: User
): Parcelable