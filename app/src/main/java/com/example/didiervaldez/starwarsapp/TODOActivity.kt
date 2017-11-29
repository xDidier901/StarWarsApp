package com.example.didiervaldez.starwarsapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.google.firebase.database.*
//import kotlinx.android.synthetic.main.activity_todo.*
import android.view.ContextMenu.ContextMenuInfo
import android.view.MenuInflater
import android.widget.AdapterView.AdapterContextMenuInfo
import android.R.array
import android.content.Intent
import android.provider.SyncStateContract
import android.support.v7.app.AlertDialog
import android.widget.*
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.layout_todo_items.*
import org.jetbrains.anko.*

//
//class TODOActivity : AppCompatActivity() {
//    var data: FirebaseDatabase = FirebaseDatabase.getInstance()
//    private var mDatabase: DatabaseReference = data.getReference("starwarsapp-6c83e")
//    var name: String? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_todo)
//
//        listaToDo()
//        fabAdd.setOnClickListener{ view ->
//            alert {
//                customView {
//                    verticalLayout {
//                        var header = textView{
//                            text = "Agregar nuevo TODo"
//                        }
//                        val titulo = editText {
//                            hint = "Titulo"
//                        }
//                        val descripcion = editText {
//                            hint = "Descripcion"
//                        }
//                        val fecha = editText {
//                            hint = "Fecha"
//                        }
//                        positiveButton("Agregar") { insertToDo(mDatabase, titulo.text.toString(), descripcion.text.toString(), fecha.text.toString()) }
//                    }
//                }
//            }.show()
//        }
//        listToDOs.setOnItemClickListener { adapterView, view, position, id ->
//            toast("" + adapterView.getItemAtPosition(position))
//            name = "" + listToDOs.selectedItem
//            //borrarUsuario(adapterView.getItemAtPosition(position).toString())
//        }
//        registerForContextMenu(listToDOs);
//    }
//
//    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//        //toast("seleccionado: " + name)
//        if (v.id === R.id.listToDOs) {
//            val inflater = menuInflater
//            menu.setHeaderTitle("Selecciona una opcion")
//            inflater.inflate(R.menu.menu_opciones, menu)
//        }
//
//    }
//
//    override fun onContextItemSelected(item: MenuItem?): Boolean {
//        val info = item?.menuInfo as AdapterContextMenuInfo
//        var itemOnList = listToDOs.getItemAtPosition(info.position) as ToDo //*o*
//        //toast(""+itemOnList.Description)
//        if (item.toString() == "Borrar") {
//            //toast(name.toString())
//            //this.closeContextMenu()
//            borrar(itemOnList.uuid)
//            //listaToDo()
//        } else if (item.toString() == "Editar") {
//            //cambiar a nuevo activity
//            val intent = Intent(this, UpdateActivity::class.java)
//            intent.putExtra("Descripcion", "${itemOnList.Description.toString()}")
//            intent.putExtra("Titulo", "${itemOnList.Titulo.toString()}")
//            intent.putExtra("Fecha", "${itemOnList.Fecha.toString()}")
//            intent.putExtra("uuid", "${itemOnList.uuid.toString()}")
//            startActivity(intent)
//        }
//        return super.onContextItemSelected(item)
//    }
//
//    private fun borrar(uuid: String) {
//
//        var firebaseData = FirebaseDatabase.getInstance().reference
//        val item = firebaseData.child("starwarsapp-6c83e").child("TODOs").child(uuid)
//        item.removeValue()
//        toast("Item borrado")
//        listaToDo()
//    }
//
//    //Insertar evento a firebase
//    private fun insertToDo(firebaseData: DatabaseReference, titulo: String, descripcion: String, fecha: String) {
//        //Recibir valores y agregarlos a la lista
//        try{
//            val availableTODOs: List<ToDo> = mutableListOf(
//                    ToDo("${titulo.toString()}", "${descripcion.toString()}", "${fecha.toString()}")
//            )
//            //insertar lista en firebase
//            availableTODOs.forEach {
//                val key = firebaseData.child("TODOs").push().key
//                it.uuid = key
//                firebaseData.child("TODOs").child(key).setValue(it)
//                toast("Evento añadido con exito")
//                listaToDo()
//            }
//        }
//        catch(e:Exception){
//            toast(e.message.toString())
//        }
//    }
//
//    //obtiene todos los elementos de firebase
//    fun listaToDo(){
//        val menuListener = object: ValueEventListener{
//            val menu: MutableList<ToDo> = mutableListOf()
//            override fun onDataChange(dataSnapshot: DataSnapshot?) {
//                var children = dataSnapshot!!.children
//                dataSnapshot?.children.mapNotNullTo(menu) { it.getValue<ToDo>(ToDo::class.java) }
//                var lista: ArrayList<ToDo> = ArrayList<ToDo>()
//                for(i in menu.indices){
//                    lista?.add(i, menu[i])
//                }
//                getViewLista(lista)
//                textView.text = menu[0].Description
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                println("loadPost:onCancelled ${databaseError.toException()}")
//            }
//        }
//        mDatabase.child("TODOs").addValueEventListener(menuListener)
//    }
//
////    fun getViewLista(list: ArrayList<ToDo>){
////        var list_books: ListView = findViewById<ListView>(R.id.listToDOs) as ListView
////        var adapter: ListAdapter? = null
////        adapter = ListAdapter(list, this.)
////        list_books.adapter = adapter
////    }
//
//}




