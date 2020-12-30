package net.it96.enfoque.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note (
    val id : String = "0",
    var description : String = "",
    val projectName : String = ""
) : Parcelable