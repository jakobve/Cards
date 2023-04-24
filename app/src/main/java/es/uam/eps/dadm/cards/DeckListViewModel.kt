package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class DeckListViewModel(application: Application): AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    val decksWithCards: LiveData<List<DeckWithCards>> = CardsApplication.getDecksWithCards(context)

}