package com.example.practica3

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
class AgregarPelicula : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database

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
        val anoPelicula = view.findViewById<TextInputEditText>(R.id.añoM)

        val pantallaFragmento = view.findViewById<FrameLayout>(R.id.pantallaFragmento)

        pantallaFragmento.setBackgroundColor(Color.parseColor("#6ede8a"))

        val botonAceptar = view.findViewById<Button>(R.id.aceptar)
        val botonCancelar = view.findViewById<Button>(R.id.cancelar)

        val botonAgregar = (activity as Principal).findViewById<FloatingActionButton>(R.id.agregar)

        if (contenedor.visibility == View.VISIBLE){
            toolbar.title = "Agregar Pelicula"
            toolbar.setBackgroundColor(Color.parseColor("#25a244"))
            toolbar.setTitleTextColor(Color.parseColor("#f2e8cf"))
            botonAgregar.visibility = View.GONE
        }
        botonAceptar.text = resources.getString(R.string.agregarPeli)
        botonCancelar.text = resources.getString(R.string.cancelar)

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
                myRef.push().setValue(peliculaNueva)
                Toast.makeText(view.context, "Pelicula Agregada", Toast.LENGTH_SHORT).show()
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AgregarPelicula.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AgregarPelicula().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}