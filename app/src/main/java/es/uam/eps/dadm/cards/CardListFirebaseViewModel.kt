package es.uam.eps.dadm.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CardListFirebaseViewModel : ViewModel() {

    private var _deckWithCards = MutableLiveData<List<DeckWithCards>>()
    val deckWithCards: LiveData<List<DeckWithCards>>
            get() = _deckWithCards
    private var reference = FirebaseDatabase.getInstance().getReference("decksWithCards")

    init {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listOfDeckWithCards = mutableListOf<DeckWithCards>()

                for (child in snapshot.children) {
                    child.getValue(DeckWithCards::class.java)?.let {
                        listOfDeckWithCards.add(it)
                    }
                }
                _deckWithCards.value = listOfDeckWithCards
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}