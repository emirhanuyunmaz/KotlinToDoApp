package com.example.kotlindictionary

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.kotlindictionary.databinding.ActivityMainBinding
import com.example.kotlindictionary.databinding.AlertDialogDesignBinding
import com.example.kotlindictionary.databinding.DatePickerBinding
import com.example.kotlindictionary.databinding.TimePickerBinding

import com.example.kotlindictionary.recycler_view_adapter.ToDoRecyclerViewAdapter
import com.example.kotlindictionary.recycler_view_swipe.SwipeController
import com.example.kotlindictionary.room_database_todo.TODO
import com.example.kotlindictionary.room_database_todo.TODODao
import com.example.kotlindictionary.room_database_todo.TODODatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var db:TODODatabase
    private lateinit var todoDao: TODODao
    private lateinit var job: Job
    private lateinit var jobMain:Job
    private lateinit var jobDelete:Job
    private lateinit var jobUpdate:Job
    private lateinit var jobCancel:Job
    private lateinit var alertDialog: AlertDialogDesignBinding
    private lateinit var timePicker: TimePickerBinding
    private lateinit var datePicker: DatePickerBinding
    private lateinit var todoArrayList: ArrayList<TODO>
    private lateinit var adapter:ToDoRecyclerViewAdapter
    private lateinit var swipeController: SwipeController
    private lateinit var itemTouchHelper: ItemTouchHelper
    private var updateClick=false
    private var todoArrID :Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            TODODatabase::class.java, "TodoDatabase"
        ).build()

        todoDao=db.todoDao()

        todoArrayList= ArrayList()

        adapter= ToDoRecyclerViewAdapter(todoArrayList)

        binding.recyclerView.layoutManager=LinearLayoutManager(this@MainActivity)
        binding.recyclerView.adapter=adapter


        //------------------------

        jobMain= CoroutineScope(Dispatchers.IO).launch {

            todoArrayList= ArrayList(todoDao.getAll())

            withContext(Dispatchers.Main){
                adapter= ToDoRecyclerViewAdapter(todoArrayList)
                binding.recyclerView.layoutManager=LinearLayoutManager(this@MainActivity)
                binding.recyclerView.adapter=adapter
            }

        }


        //-------------------------------

        swipeController = object : SwipeController(this@MainActivity) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when(direction){

                    ItemTouchHelper.RIGHT->{
                        //Update is todo
                        todoArrID=viewHolder.adapterPosition
                        var selectedToDo=todoArrayList[todoArrID!!]
                        var todoID=todoArrayList[todoArrID!!].id
                        builderToDoAlert()
                        alertDialog.alertEditTextTodoComment.setText(selectedToDo.comment)
                        alertDialog.alertEditTextTodoDate.setText(selectedToDo.date)
                        alertDialog.alertEditTextTodoTime.setText(selectedToDo.time)


                        updateClick=true

                    }

                    ItemTouchHelper.LEFT->{
                        //Delete is todo

                        jobDelete= CoroutineScope(Dispatchers.IO).launch {

                            var todoArrID=viewHolder.adapterPosition
                            var todoID=todoArrayList[todoArrID].id
                            var getSearchToDo=todoDao.getSearch(todoID)
                            todoDao.delete(getSearchToDo)
                            todoArrayList= ArrayList(todoDao.getAll())
                            withContext(Dispatchers.Main){

                                adapter= ToDoRecyclerViewAdapter(todoArrayList)
                                binding.recyclerView.layoutManager=LinearLayoutManager(this@MainActivity)
                                binding.recyclerView.adapter=adapter
                            }

                        }


                    }

                }

            }
        }

        //Swipe

        itemTouchHelper=ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        //-----------------------------


    }
    private fun builderToDoAlert(){
        val builder=AlertDialog.Builder(this@MainActivity)
        alertDialog=AlertDialogDesignBinding.inflate(LayoutInflater.from(this@MainActivity),null,false)
        builder.setView(alertDialog.root)

        val dialog=builder.setTitle("Add TODO").setPositiveButton("Save", DialogInterface.OnClickListener { dialog, which ->

            if(updateClick){

                var todoID=todoArrayList[todoArrID!!].id
                jobUpdate= CoroutineScope(Dispatchers.IO).launch {
                    var selectedToDo=todoDao.getSearch(todoID)

                    var todoComment=alertDialog.alertEditTextTodoComment.text.toString()
                    var todoDate=alertDialog.alertEditTextTodoDate.text.toString()
                    var todoTime=alertDialog.alertEditTextTodoTime.text.toString()

                    selectedToDo.comment=todoComment
                    selectedToDo.date=todoDate
                    selectedToDo.time=todoTime
                    todoDao.update(selectedToDo)

                    todoArrayList= ArrayList(todoDao.getAll())
                    withContext(Dispatchers.Main){
                        adapter= ToDoRecyclerViewAdapter(todoArrayList)
                        binding.recyclerView.adapter=adapter
                    }
                }

                updateClick=false
            }else{
                job=CoroutineScope(Dispatchers.IO).launch {
                    var todoComment=alertDialog.alertEditTextTodoComment.text.toString()
                    var todoDate=alertDialog.alertEditTextTodoDate.text.toString()
                    var todoTime=alertDialog.alertEditTextTodoTime.text.toString()
                    var todo=TODO(todoComment,todoDate,todoTime)
                    todoDao.insetToDo(todo)
                    todoArrayList= ArrayList(todoDao.getAll())
                    withContext(Dispatchers.Main){

                        adapter= ToDoRecyclerViewAdapter(todoArrayList)
                        binding.recyclerView.adapter=adapter

                    }
                }
                Toast.makeText(this@MainActivity, "Add Todo", Toast.LENGTH_SHORT).show()
            }



        }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->

            if (updateClick){
                jobCancel= CoroutineScope(Dispatchers.IO).launch {
                    todoArrayList= ArrayList(todoDao.getAll())
                    withContext(Dispatchers.Main){
                        adapter= ToDoRecyclerViewAdapter(todoArrayList)
                        binding.recyclerView.adapter=adapter
                    }

                }

            }
            updateClick=false
            Toast.makeText(this@MainActivity, "Cancel", Toast.LENGTH_SHORT).show()

        }).create()
        dialog.show()
    }

    fun addTODO(view:View){
        builderToDoAlert()
    }


    fun setDate(view:View){
        val builder=AlertDialog.Builder(this@MainActivity)
        datePicker=DatePickerBinding.inflate(LayoutInflater.from(this@MainActivity),null,false)
        builder.setView(datePicker.root)

        val dialog=builder.setTitle("Select Date").setPositiveButton("Save", DialogInterface.OnClickListener { dialog, which ->
            //Selected Date
            var getDate=(datePicker.datePicker.dayOfMonth.toString() +"."+datePicker.datePicker.month.toString()+"."+datePicker.datePicker.year.toString())
            alertDialog.alertEditTextTodoDate.setText(getDate)

        }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            //Cancel Date

        }).create()
        dialog?.show()
    }

    fun setTime(view : View){
        val builder=AlertDialog.Builder(this@MainActivity)
        timePicker=TimePickerBinding.inflate(LayoutInflater.from(this@MainActivity),null,false)

        builder.setView(timePicker.root)

        val dialog=builder.setTitle("Select Time").setPositiveButton("Save", DialogInterface.OnClickListener { dialog, which ->
            //Selected Time
            var getTime=timePicker.timePicker1.hour.toString()+":"+timePicker.timePicker1.minute.toString()
            alertDialog.alertEditTextTodoTime.setText(getTime)

        }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            //Cancel Time
        }).create()
        dialog.show()
    }




}