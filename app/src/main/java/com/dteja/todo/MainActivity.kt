package com.dteja.todo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // remove item
                listOfTasks.removeAt(position)
                // notify the adapter
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }

        loadItems()

        val recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextFiled = findViewById<EditText>(R.id.addTaskField)

        findViewById<Button>(R.id.button).setOnClickListener {
            //1. getting the user input
            val userInputText = inputTextFiled.text.toString()

            //2. add to the list
            listOfTasks.add(userInputText)
            saveItems()

            //3. Notify the adapter
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //4. reset the textField
            inputTextFiled.setText("")

        }
    }

    fun getDataFile(): File {
        return File(filesDir, "TODO.txt")
    }

    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }


}