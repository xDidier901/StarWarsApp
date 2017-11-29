package com.example.didiervaldez.starwarsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.*


class Home : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null

    var data: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var mDatabase: DatabaseReference = data.getReference("starwarsapp-6c83e")
    var name: String? = null

    override fun onResume() {
        super.onResume()
        listaToDo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mAuth = FirebaseAuth.getInstance()
        user = mAuth!!.currentUser

        val displayName: String = user!!.displayName.toString()
        userName.text = "Correo: ${user!!.email.toString()}"
        toast(user!!.displayName.toString())

        Picasso.with(this)
                .load(user!!.photoUrl)
                .into(profileImage)


////        println(obj.displayName)
//        button2.setOnClickListener({
//            val intent = Intent(this, TODOActivity::class.java)
//            startActivity(intent)
//
//        })

        listaToDo()
        fabAdd.setOnClickListener{ view ->
            alert {
                customView {
                    verticalLayout {
                        var header = textView{
                            text = "Agregar nuevo TODo"
                        }
                        val titulo = editText {
                            hint = "Titulo"
                        }
                        val descripcion = editText {
                            hint = "Descripcion"
                        }
                        val fecha = editText {
                            hint = "Fecha"
                        }
                        positiveButton("Agregar") { insertToDo(mDatabase, titulo.text.toString(), descripcion.text.toString(), fecha.text.toString()) }
                    }
                }
            }.show()
        }
        listToDOs.setOnItemClickListener { adapterView, view, position, id ->
            name = "" + listToDOs.selectedItem
            //borrarUsuario(adapterView.getItemAtPosition(position).toString())
        }
        registerForContextMenu(listToDOs);

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.log_out -> {
                mAuth!!.signOut()
                toast(R.string.log_out_text)
                startActivity<MainActivity>()
            }
            R.id.about_us -> startActivity<AboutUs>()
            else -> { // Note the block
                print("x is neither 1 nor 2")
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //toast("seleccionado: " + name)
        if (v.id === R.id.listToDOs) {
            val inflater = menuInflater
            menu.setHeaderTitle("Selecciona una opcion")
            inflater.inflate(R.menu.menu_opciones, menu)
        }

    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val info = item?.menuInfo as AdapterView.AdapterContextMenuInfo
        var itemOnList = listToDOs.getItemAtPosition(info.position) as ToDo //*o*
        //toast(""+itemOnList.Description)
        if (item.toString() == "Borrar") {
            //toast(name.toString())
            //this.closeContextMenu()
            borrar(itemOnList.uuid)
            //listaToDo()
        } else if (item.toString() == "Editar") {
            //cambiar a nuevo activity
            val intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra("Descripcion", "${itemOnList.Description.toString()}")
            intent.putExtra("Titulo", "${itemOnList.Titulo.toString()}")
            intent.putExtra("Fecha", "${itemOnList.Fecha.toString()}")
            intent.putExtra("uuid", "${itemOnList.uuid.toString()}")
            intent.putExtra("User", user!!.uid.toString())
            startActivity(intent)
        }
        return super.onContextItemSelected(item)
    }

    private fun borrar(uuid: String) {

        var firebaseData = FirebaseDatabase.getInstance().reference
        val item = firebaseData.child("starwarsapp-6c83e").child(user!!.uid.toString()).child("TODOs").child(uuid)
        item.removeValue()
        toast("Item borrado")
        listaToDo()
    }

    //Insertar evento a firebase
    private fun insertToDo(firebaseData: DatabaseReference, titulo: String, descripcion: String, fecha: String) {
        //Recibir valores y agregarlos a la lista
        try{
            val availableTODOs: List<ToDo> = mutableListOf(
                    ToDo("${titulo.toString()}", "${descripcion.toString()}", "${fecha.toString()}")
            )
            //insertar lista en firebase
            availableTODOs.forEach {
                val key = firebaseData.child(user!!.uid.toString()).child("TODOs").push().key
                it.uuid = key
                firebaseData.child(user!!.uid.toString()).child("TODOs").child(key).setValue(it)
                toast("Evento añadido con exito")
                listaToDo()
            }
        }
        catch(e:Exception){
            toast(e.message.toString())
        }
    }

    //obtiene todos los elementos de firebase
    fun listaToDo(){
        val menuListener = object: ValueEventListener {
            val menu: MutableList<ToDo> = mutableListOf()
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                var children = dataSnapshot!!.children
                dataSnapshot?.children.mapNotNullTo(menu) { it.getValue<ToDo>(ToDo::class.java) }
                var lista: ArrayList<ToDo> = ArrayList<ToDo>()
                for(i in menu.indices){
                    lista?.add(i, menu[i])
                }
                getViewLista(lista)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        mDatabase.child(user!!.uid).child("TODOs").addValueEventListener(menuListener)
    }

    fun getViewLista(list: ArrayList<ToDo>){
        var listItems: ListView = findViewById<ListView>(R.id.listToDOs) as ListView
        var adapter: ListAdapter? = null
        adapter = ListAdapter(list, this)
        listItems.adapter = adapter
    }

}
