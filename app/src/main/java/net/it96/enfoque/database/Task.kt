package net.it96.enfoque.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task (
    val id : String = "0",
    var description : String = "",
    var date : String = "",
    val projectName : String = "",
    val userEmail : String = ""
) : Parcelable