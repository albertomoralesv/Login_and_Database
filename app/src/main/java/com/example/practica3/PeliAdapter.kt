package com.example.practica3

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class PeliAdapter(private val context:Activity, private val arrayList: ArrayList<Peliculas>):ArrayAdapter<Peliculas>(context,R.layout.item, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // return super.getView(position, convertView, parent)

        val inflater:LayoutInflater = LayoutInflater.from(context)
        val view:View = inflater.inflate(R.layout.item, null)

        view.findViewById<TextInputEditText>(R.id.nombre).setText(arrayList[position].nombre)
        view.findViewById<TextInputEditText>(R.id.genero).setText(arrayList[position].genero)
        view.findViewById<TextInputEditText>(R.id.año).setText(arrayList[position].ano)

        val contenedor = context.findViewById<FragmentContainerView>(R.id.fragmentContainerView)
        val listaPeliculas = context.findViewById<ListView>(R.id.lista)

        val editar = view.findViewById<FloatingActionButton>(R.id.editar)

        editar.setOnClickListener{
            contenedor.visibility = View.VISIBLE
            listaPeliculas.visibility = View.GONE
            (context as Principal).replaceFragmento(EditarPelicula(position, arrayList))
        }

        val eliminar = view.findViewById<FloatingActionButton>(R.id.eliminar)

        eliminar.setOnClickListener{
            contenedor.visibility = View.VISIBLE
            listaPeliculas.visibility = View.GONE
            (context as Principal).replaceFragmento(EliminarPelicula(position, arrayList))
        }

        return view
    }
}