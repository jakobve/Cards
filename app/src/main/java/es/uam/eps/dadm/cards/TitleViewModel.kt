package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import es.uam.eps.dadm.cards.database.CardDatabase

class TitleViewModel(application: Application): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    val ndueCards: LiveData<Int> = CardsApplication.getNDueCards(context)

    fun getUserLoggedIn(): Boolean {
        return SettingsActivity.getLoggedIn(context)
    }

    fun getUserId(): String? {
        return SettingsActivity.getUserID(context)
    }
}