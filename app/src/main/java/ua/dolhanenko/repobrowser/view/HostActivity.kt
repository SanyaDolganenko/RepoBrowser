package ua.dolhanenko.repobrowser.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ua.dolhanenko.repobrowser.R

class HostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
        val fragment = LoginFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }
}