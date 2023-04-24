package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.*

class StatisticsViewModel(application: Application): AndroidViewModel(application) {

    private var context = getApplication<Application>().applicationContext

    var decksWithCards: LiveData<List<DeckWithCards>> = CardsApplication.getDecksWithCards(context)
}