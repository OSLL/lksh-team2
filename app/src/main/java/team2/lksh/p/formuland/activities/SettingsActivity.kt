package team2.lksh.p.formuland.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import team2.lksh.p.formuland.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(toolbar_settings)

        toolbar_settings.setNavigationIcon(R.drawable.sharp_arrow_back_white_24)
        toolbar_settings.setNavigationOnClickListener {
            onBackPressed()
        }

        supportActionBar!!.title = "Settings"
    }
}
