package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.*

class StatisticsViewModel(application: Application): AndroidViewModel(application) {

    private var context = getApplication<Application>().applicationContext

    private val userId = SettingsActivity.getUserID(context)

    var decksWithCards: LiveData<List<DeckWithCards>> = CardsApplication.getDecksWithCardsForUser(context, userId!!)

}