package com.example.practica3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Principal : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database
    lateinit var datos:ArrayList<Peliculas>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        auth = FirebaseAuth.getInstance()

        val myRef = database.getReference("peliculas")

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                datos = ArrayList()
                dataSnapshot.children.forEach{
                    hijo ->

                    val pelicula = Peliculas(hijo.child("nombre").value.toString(), hijo.child("ano").value.toString(), hijo.child("genero").value.toString(), hijo.key.toString())
                    datos.add(pelicula)
                }
                llenaLista(datos)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        //

        //


        val agrega = findViewById<FloatingActionButton>(R.id.agregar)
        val contenedor = findViewById<FragmentContainerView>(R.id.fragmentContainerView)
        val listaPeliculas = findViewById<ListView>(R.id.lista)
        agrega.setOnClickListener{
            contenedor.visibility = View.VISIBLE
            listaPeliculas.visibility = View.GONE
            replaceFragmento(AgregarPelicula())
        }
    }

    fun llenaLista(datos: ArrayList<Peliculas>){
        val adaptador = PeliAdapter(this, datos)
        val lista = findViewById<ListView>(R.id.lista)
        lista.adapter = adaptador
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val usuario: FirebaseUser? = auth.currentUser
        if (item.itemId == R.id.salir){
            Toast.makeText(this, "Adios " + usuario!!.email.toString(), Toast.LENGTH_SHORT).show()
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else if (item.itemId == R.id.perfil){
            Toast.makeText(this, usuario!!.email.toString(), Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    fun replaceFragmento(fragmento: Fragment){
        val manager = supportFragmentManager
        val transaccion = manager.beginTransaction()
        transaccion.replace(R.id.fragmentContainerView, fragmento)
        transaccion.commit()
    }
    fun hideSoftKeyboard(view: View) {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view.windowToken, 0)
    }



}