package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class TitleViewModel(application: Application): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    val ndueCards: LiveData<Int> = CardsApplication.getNDueCards(context)

    fun getUserLoggedIn(): Boolean {
        return SettingsActivity.getLoggedIn(context)
    }

    fun getUserId(): String? {
        return SettingsActivity.getUserID(context)
    }

    fun saveDecksWithCardsLocally(deckWithCardsList: List<DeckWithCards>) {
        for(deckWithCards in deckWithCardsList) {
            CardsApplication.addDeck(context, deckWithCards.deck)
            for(card in deckWithCards.cards) {
                CardsApplication.addCard(context, card)
            }
        }
    }

    fun saveDecksWithCardsRemotely(): HashMap<Long, Any> {
        val decksWithCards = CardsApplication.getDecksWithCards(context).value

        val uploadData = HashMap<Long, Any>()
        for(deckWithCards in decksWithCards!!) {
            val deckMap = HashMap<String, Any>()
            deckMap["deckId"] = deckWithCards.deck.deckId
            deckMap["name"] = deckWithCards.deck.name

            val cardList = ArrayList<HashMap<String, Any>>()
            for(card in deckWithCards.cards) {
                val cardMap = HashMap<String, Any>()
                cardMap["cardId"] = card.id
                cardMap["question"] = card.question
                cardMap["answer"] = card.answer
                cardMap["answered"] = card.answered
                cardMap["date"] = card.date
                cardMap["easiness"] = card.easiness
                cardMap["quality"] = card.quality
                cardMap["nextPracticeDate"] = card.nextPracticeDate
                cardMap["interval"] = card.interval
                cardMap["repetitions"] = card.repetitions
                cardList.add(cardMap)
            }
            deckMap["cards"] = cardList
            uploadData[deckWithCards.deck.deckId] = deckMap
        }
        return uploadData
    }
}