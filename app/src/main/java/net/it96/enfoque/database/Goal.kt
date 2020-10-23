package net.it96.enfoque.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Goal (
    var description : String = ""
) : Parcelable