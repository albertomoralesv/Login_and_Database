package com.example.practica3

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AgregarPelicula.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditarPelicula(pos: Int, listaPel: ArrayList<Peliculas>) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database
    private val position = pos
    private var datos = listaPel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        auth = FirebaseAuth.getInstance()

        val myRef = database.getReference("peliculas")

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_agregar_pelicula, container, false)

        val pantalla = (activity as Principal).findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.pantallaPrincipal)
        val toolbar = (activity as Principal).findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        val contenedor = (activity as Principal).findViewById<FragmentContainerView>(R.id.fragmentContainerView)
        val listaPeliculas = (activity as Principal).findViewById<ListView>(R.id.lista)

        val nombrePelicula = view.findViewById<TextInputEditText>(R.id.nombreM)
        val generoPelicula = view.findViewById<TextInputEditText>(R.id.generoM)
        val anoPelicula = view.findViewById<TextInputEditText>(R.id.a??oM)

        val pantallaFragmento = view.findViewById<FrameLayout>(R.id.pantallaFragmento)

        pantallaFragmento.setBackgroundColor(Color.parseColor("#fad643"))

        val botonAceptar = view.findViewById<Button>(R.id.aceptar)
        val botonCancelar = view.findViewById<Button>(R.id.cancelar)

        val botonAgregar = (activity as Principal).findViewById<FloatingActionButton>(R.id.agregar)

        if (contenedor.visibility == View.VISIBLE){
            toolbar.title = "Editar Pelicula"
            toolbar.setBackgroundColor(Color.parseColor("#a47e1b"))
            toolbar.setTitleTextColor(Color.parseColor("#f2e8cf"))
            botonAgregar.visibility = View.GONE
        }
        botonAceptar.text = resources.getString(R.string.confirmar)
        botonCancelar.text = resources.getString(R.string.cancelar)

        nombrePelicula.setText(datos[position].nombre)
        generoPelicula.setText(datos[position].genero)
        anoPelicula.setText(datos[position].ano)

        botonAceptar.setOnClickListener{
            val nombre = nombrePelicula.text.toString()
            val genero = generoPelicula.text.toString()
            val ano = anoPelicula.text.toString()
            nombrePelicula.clearFocus()
            generoPelicula.clearFocus()
            anoPelicula.clearFocus()
            (activity as Principal).hideSoftKeyboard(view)
            if (nombre != "" && genero != "" && ano != ""){
                contenedor.visibility = View.GONE
                toolbar.setBackgroundColor(Color.parseColor("#c8c8c8"))
                toolbar.setTitleTextColor(Color.parseColor("#0d0d0d"))
                toolbar.title = "Lista de Peliculas"
                val peliculaNueva = Pelicula(nombre, ano, genero)
                myRef.child(datos[position].id.toString()).setValue(peliculaNueva)
                Toast.makeText(view.context, "Pelicula Editada", Toast.LENGTH_SHORT).show()
                listaPeliculas.visibility = View.VISIBLE
                botonAgregar.visibility = View.VISIBLE
            }else{
                Toast.makeText(view.context, "Datos no Validos", Toast.LENGTH_SHORT).show()
            }

        }
        botonCancelar.setOnClickListener {
            (activity as Principal).hideSoftKeyboard(view)
            contenedor.visibility = View.GONE
            toolbar.setBackgroundColor(Color.parseColor("#c8c8c8"))
            toolbar.setTitleTextColor(Color.parseColor("#0d0d0d"))
            toolbar.title = "Lista de Peliculas"
            listaPeliculas.visibility = View.VISIBLE
            botonAgregar.visibility = View.VISIBLE
            Toast.makeText(view.context, "Operacion Cancelada", Toast.LENGTH_SHORT).show()
        }

        pantalla.setOnClickListener{
            nombrePelicula.clearFocus()
            generoPelicula.clearFocus()
            anoPelicula.clearFocus()
            (activity as Principal).hideSoftKeyboard(view)
        }
        toolbar.setOnClickListener{
            nombrePelicula.clearFocus()
            generoPelicula.clearFocus()
            anoPelicula.clearFocus()
            (activity as Principal).hideSoftKeyboard(view)
        }

        return view
    }

}