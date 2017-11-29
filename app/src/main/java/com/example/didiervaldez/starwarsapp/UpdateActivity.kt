package com.example.didiervaldez.starwarsapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_update.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

class UpdateActivity : AppCompatActivity() {
    var description:String ?=null
    var titulo:String ?= null
    var fecha:String ?= null
    var uuid:String ?= null
    var list: ArrayList<String>?= ArrayList()
    var list2: ArrayList<String>?= ArrayList()
    var user: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        val i = intent
        description = i.getStringExtra("Descripcion")
        titulo = i.getStringExtra("Titulo")
        fecha = i.getStringExtra("Fecha")
        uuid = i.getStringExtra("uuid")
        user = i.getStringExtra("User")
        this.list2?.add(0, "titulo")
        this.list2?.add(1, "description")
        this.list2?.add(2, "fecha")
        toast(""+user.toString())

        setValoresIniciales()

        btnActualizar.setOnClickListener({
            setValores()
            actualizar()
            toast(""+list)
        })
    }

    private fun setValoresIniciales(){
        etDescripcion.setText(description)
        etTitulo.setText(titulo)
        etFecha.setText(fecha)
    }

    private fun setValores(){
        this.list?.add(0, etTitulo.text.toString())
        this.list?.add(1, etDescripcion.text.toString())
        this.list?.add(2, etFecha.text.toString())
        //this.list?.add(3, uuid.toString())


    }

    private fun actualizar(){
        var firebaseData = FirebaseDatabase.getInstance().reference
        alert("¿Actualizar registro?"){
            positiveButton("Actualizar"){
                try {
                    for(j in list2!!.indices){
                        firebaseData.child("starwarsapp-6c83e").child("${user.toString()}").child("TODOs").child(uuid).child(list2!![j]).setValue(list!![j])
                    }
                    //firebaseData.child("starwarsapp-6c83e").child("TODOs").child(uuid).child("titulo").setValue("POE")
                    toast("Registro actualizado")
                }catch (e:Exception){
                    toast(e.message.toString())
                }
            }
            negativeButton("Cancelar"){
                //nothing
            }
        }.show()

    }
}
