package com.example.practica3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val correo = findViewById<EditText>(R.id.Correo)
        val contrasena = findViewById<EditText>(R.id.Contraseña)
        val login = findViewById<Button>(R.id.Login)
        val logout = findViewById<Button>(R.id.Logout)
        val pantalla = findViewById<ConstraintLayout>(R.id.pantalla)

        val insetsController = ViewCompat.getWindowInsetsController(pantalla)

        login.setOnClickListener{
            correo.clearFocus()
            contrasena.clearFocus()
            insetsController?.hide(WindowInsetsCompat.Type.ime())
            try {
                auth.signInWithEmailAndPassword(correo.text.toString(), contrasena.text.toString())
                    .addOnCompleteListener(this
                    ) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Usuario Autentificado", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, Principal::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this, "Falló la Autentificación",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }catch(e: Exception){ }
        }

        logout.setOnClickListener {
            correo.clearFocus()
            contrasena.clearFocus()
            insetsController?.hide(WindowInsetsCompat.Type.ime())
            val usuario:FirebaseUser? = auth.currentUser
            if (usuario != null){
                auth.signOut()
                Toast.makeText(this, "Usuario " + usuario.email.toString() + " deslogueado", Toast.LENGTH_SHORT).show()
            }
        }

        pantalla.setOnClickListener {
            correo.clearFocus()
            contrasena.clearFocus()
            insetsController?.hide(WindowInsetsCompat.Type.ime())
        }

    }

    override fun onStart() {
        super.onStart()
        val usuarioActual = auth.currentUser
        usuarioLogueado(usuarioActual)
    }

    private fun usuarioLogueado(usuario:FirebaseUser?){
        if (usuario != null)
        {
            Toast.makeText(this, "El usuario " + usuario.email.toString() + " esta autentificado", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Principal::class.java))
        }
    }


}