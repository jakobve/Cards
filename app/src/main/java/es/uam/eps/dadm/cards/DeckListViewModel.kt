package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

class DeckListViewModel(application: Application): AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    val decksWithCards: LiveData<List<DeckWithCards>> = CardsApplication.getDecksWithCards(context)

    fun uploadDecksWithCards() {
        Timber.i("Upload decksWithCards")
        var reference = FirebaseDatabase.getInstance().getReference("decksWithCards")
        reference.setValue(decksWithCards.value)
    }
}