package net.it96.enfoque.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Goal (
    val id : String = "0",
    var description : String = "",
    val projectName : String = ""
) : Parcelable