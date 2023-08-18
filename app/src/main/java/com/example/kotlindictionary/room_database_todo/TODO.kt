package com.example.kotlindictionary.room_database_todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TODO(

    @ColumnInfo(name="comment")
    var comment:String,

    @ColumnInfo(name = "date")
    var date:String,

    @ColumnInfo("time")
    var time:String
) {
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}