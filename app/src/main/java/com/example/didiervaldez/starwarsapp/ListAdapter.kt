package com.example.didiervaldez.starwarsapp

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

/**
 * Created by luanmega on 27/11/17.
 */
data class ListAdapter(var todoList:List<ToDo>, var activity: TODOActivity) : BaseAdapter(){

    override fun getItem(position: Int): Any {
        return todoList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return todoList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(activity,R.layout.layout_todo_items,null)

        val txt_number = view.findViewById<TextView>(R.id.txt_number) as TextView
        val txt_Titulo = view.findViewById<TextView>(R.id.titulo) as TextView
        val txt_Descripcion = view.findViewById<TextView>(R.id.descripcion) as TextView
        val txt_Fecha = view.findViewById<TextView>(R.id.fecha) as TextView

        txt_number.text = (position+1).toString()+"."
        txt_Titulo.text = todoList.get(position).Titulo
        txt_Descripcion.text = todoList.get(position).Description
        txt_Fecha.text = todoList.get(position).Fecha

        return view
    }
}