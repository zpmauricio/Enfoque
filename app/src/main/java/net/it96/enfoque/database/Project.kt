package net.it96.enfoque.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Project (
    var imageUrl: String = "",
    var name: String = ""
) : Parcelable