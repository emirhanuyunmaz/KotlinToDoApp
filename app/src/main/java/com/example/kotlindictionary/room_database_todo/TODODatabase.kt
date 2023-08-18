package com.example.kotlindictionary.room_database_todo

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TODO::class], version = 1)
abstract class TODODatabase :RoomDatabase(){
    abstract fun todoDao():TODODao
}