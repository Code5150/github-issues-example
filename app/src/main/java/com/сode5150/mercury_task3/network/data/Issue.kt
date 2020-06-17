package com.сode5150.mercury_task3.network.data

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

const val STATE_OPEN: String = "open"
const val STATE_CLOSED: String = "closed"

@Parcelize
data class Issue(
    @PrimaryKey val number: Int,
    @SerializedName("created_at") val createdAt: Date,
    @SerializedName("updated_at") val updatedAt: Date?,
    @SerializedName("closed_at") val closedAt: Date?,
    val title: String,
    val body: String,
    val user: User,
    val state: String
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false
        else (other as Issue).let {
            if (number != it.number) return false
            if (createdAt != it.createdAt) return false
            if (updatedAt != it.updatedAt) return false
            if (closedAt != it.closedAt) return false
            if (title != it.title) return false
            if (body != it.body) return false
            if (user.login != it.user.login) return false
            if (state != it.state) return false
            return true
        }
    }

    override fun hashCode(): Int {
        return number
    }
}