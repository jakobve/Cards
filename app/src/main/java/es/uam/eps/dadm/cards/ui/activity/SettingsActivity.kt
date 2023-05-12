package es.uam.eps.dadm.cards.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import es.uam.eps.dadm.cards.R

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
        PreferenceManager.setDefaultValues(
            this,
            R.xml.root_preferences,
            false)
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
        private const val MAX_NUMBER_CARDS_KEY = "max_number_cards"
        private const val MAX_NUMBER_CARDS_DEFAULT = "20"
        private const val REPETITIONS_KEY = "repetitions"
        private const val REPETITIONS_VALUE = "0"
        private const val LAST_STUDY_SESSION_KEY = "last_study_session"
        private const val LAST_STUDY_SESSION_VALUE = "2023-05-09T09:47:08.644"
        private const val BOARD_KEY = "board"
        private const val BOARD_DEFAULT = false
        private const val LOGGED_IN_KEY = "logged_in_key"
        private const val LOGGED_IN_DEFAULT = false
        private const val USER_ID = "user_id"
        private const val USER_ID_DEFAULT = ""

        fun getRepetitions(context: Context): Int {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(REPETITIONS_KEY, REPETITIONS_VALUE)?.toInt() ?: 0
        }

        fun setRepetitions(context: Context, repetitions: Int) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(REPETITIONS_KEY, repetitions.toString())
            editor.apply()
        }

        fun getLastStudySession(context: Context): String? {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(LAST_STUDY_SESSION_KEY, LAST_STUDY_SESSION_VALUE)
        }

        fun setLastStudySession(context: Context, date: String) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(LAST_STUDY_SESSION_KEY, date)
            editor.apply()
        }

        fun getMaximumNumberOfCards(context: Context): String? {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(MAX_NUMBER_CARDS_KEY, MAX_NUMBER_CARDS_DEFAULT)
        }

        fun getBoardPreference(context: Context): Boolean {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(BOARD_KEY, BOARD_DEFAULT)
        }

        fun setLoggedIn(context: Context, loggedIn: Boolean) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putBoolean(LOGGED_IN_KEY, loggedIn)
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