package com.example.smkesport

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.smkesport.databinding.ActivitySignUpBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUp.setOnClickListener {

            val fullName = binding.fullName.text.toString()
            val username = binding.username.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

//            fullName.isEmpty()
//            if (fullName.isEmpty() or username.isEmpty() or email.isEmpty() or password.isEmpty() or confirmPassword.isEmpty()){
//                Toast.makeText(this@SignUp, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//
//            }
//
//            if((username.length < 6) or (password.length < 6)){
//                Toast.makeText(this@SignUp, "Data tidak boleh kurang dari 6 karakter", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            if(password == confirmPassword){
//                Toast.makeText(this@SignUp, "Password harus sama!", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

            val jsonobject = JSONObject()
            jsonobject.put("fullName", fullName)
            jsonobject.put("username", username)
            jsonobject.put("email", email)
            jsonobject.put("phoneNumber", "08")
            jsonobject.put("password", password)

            lifecycleScope.launch(Dispatchers.IO) {
                val con = URL(Variabel.url + "/api/sign-up").openConnection() as HttpURLConnection
                con.doOutput = true
                con.doInput = true
                con.requestMethod = "POST"
                con.setRequestProperty("Content-Type", "application/json")
                con.setRequestProperty("accept", "application/json")
                val outputStreamWriter = OutputStreamWriter(con.outputStream)
                outputStreamWriter.write(jsonobject.toString())
                outputStreamWriter.flush()
                if (con.responseCode == 409) {
                    runOnUiThread {
                        Toast.makeText(this@SignUp, "Error", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    runOnUiThread{
                        Toast.makeText(this@SignUp, "Berhasil membuat akun!", Toast.LENGTH_SHORT).show()
                    }
                }
            }



        }

    }
}