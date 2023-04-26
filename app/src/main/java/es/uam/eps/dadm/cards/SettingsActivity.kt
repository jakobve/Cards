package es.uam.eps.dadm.cards

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }


    companion object {
        const val MAX_NUMBER_CARDS_KEY = "max_number_cards"
        const val MAX_NUMBER_CARDS_DEFAULT = "20"
        const val LOGGED_IN_KEY = "logged_in_key"
        const val LOGGED_IN_DEFAULT = false
        const val USER_ID = "user_id"
        const val USER_ID_DEFAULT = ""

        fun getMaximumNumberOfCards(context: Context): String? {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(MAX_NUMBER_CARDS_KEY, MAX_NUMBER_CARDS_DEFAULT)
        }

        fun setLoggedIn(context: Context, loggedin: Boolean) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putBoolean(LOGGED_IN_KEY, loggedin)
            editor.apply()
        }

        fun getLoggedIn(context: Context): Boolean {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(LOGGED_IN_KEY, LOGGED_IN_DEFAULT)
        }

        fun setUserID(context: Context, userId: String) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(USER_ID, userId)
            editor.apply()
        }

        fun getUserID(context: Context): String? {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(USER_ID, USER_ID_DEFAULT)
        }

    }
}