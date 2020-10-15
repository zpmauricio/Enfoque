package net.it96.enfoque.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Project (
    var id: String = "",
    var image: String = "",
    var name: String = "",
    var results: String = "",
    var goals90: String = "",
    var goals2W: String = "",
    var actions: String = "",
    var notes: String = ""
) : Parcelable