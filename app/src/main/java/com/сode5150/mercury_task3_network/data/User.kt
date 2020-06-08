package com.сode5150.mercury_task3_network.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val login: String
): Parcelable