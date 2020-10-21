package net.it96.enfoque.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Project (
    var imageUrl: String = "",
    var name: String = "",
    var results: String = "",
    var goals: ArrayList<NinetyDayGoal> = arrayListOf(),
    var actions: String = "",
    var notes: String = "",
) : Parcelable