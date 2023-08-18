package com.example.kotlindictionary.room_database_todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TODODao {

    @Query("SELECT * FROM TODO")
    fun getAll():List<TODO>

    @Query("SELECT * FROM TODO WHERE id IN (:enterId) ")
    fun getSearch(enterId:Int):TODO

    @Insert
    fun insetToDo(vararg todo:TODO)

    @Delete
    fun delete(todo: TODO)

    @Query("DELETE  FROM  TODO")
    fun deleteAll()

    @Update
    fun update(vararg todo: TODO)



}