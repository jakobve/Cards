package es.uam.eps.dadm.cards.viewModel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.*
import es.uam.eps.dadm.cards.ui.fragment.CardsApplication
import es.uam.eps.dadm.cards.model.DeckWithCards
import es.uam.eps.dadm.cards.ui.activity.SettingsActivity

@SuppressLint("StaticFieldLeak")
class StatisticsViewModel(application: Application): AndroidViewModel(application) {

    private var context = getApplication<Application>().applicationContext

    private val userId = SettingsActivity.getUserID(context)

    var decksWithCards: LiveData<List<DeckWithCards>> =
        CardsApplication.getDecksWithCardsForUser(context, userId!!)

    fun getRepetitionsToday(): Int {
        return SettingsActivity.getRepetitions(context)
    }
}