package ua.dolhanenko.repobrowser.view.host

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ua.dolhanenko.repobrowser.R
import ua.dolhanenko.repobrowser.view.login.LoginFragment

class HostActivity : AppCompatActivity(), LoginFragment.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
        val fragment = LoginFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }

    override fun onLoginSuccess() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            UserPagesHostFragment()
        ).commit()
    }
}