package net.it96.enfoque.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KeyResult (
    var description : String = ""
) : Parcelable