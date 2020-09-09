package net.it96.enfoque.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "project_table")
data class Project (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "project_name")
    var name: String,
    @ColumnInfo(name = "results")
    var results: String,
    @ColumnInfo(name = "goals90days")
    var goals90: String,
    @ColumnInfo(name = "goals2weeks")
    var goals2W: String,
    @ColumnInfo(name = "actions")
    var actions: String,
    @ColumnInfo(name = "notes")
    var notes: String
)