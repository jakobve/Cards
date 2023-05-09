package es.uam.eps.dadm.cards.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import es.uam.eps.dadm.cards.ui.fragment.CardsApplication
import es.uam.eps.dadm.cards.model.Card
import timber.log.Timber

class CardEditViewModel(application: Application): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val cardId = MutableLiveData<String>()

    val card: LiveData<Card> = cardId.switchMap {
        CardsApplication.getCard(context, it)
    }

    fun loadCardId(id: String) {
        cardId.value = id
        Timber.i("Load cardId")
    }

    fun updateCard(card: Card) {
        CardsApplication.updateCard(context, card)
    }

    fun removeCard(card: Card) {
        CardsApplication.removeCard(context, card)
    }

    fun addCard(card: Card) {
        CardsApplication.addCard(context, card)
    }

}