package es.uam.eps.dadm.cards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.uam.eps.dadm.cards.databinding.ActivityTitleBinding
import timber.log.Timber

class TitleActivity : AppCompatActivity() {

    lateinit var binding: ActivityTitleBinding

    private val titleViewModel by lazy {
        ViewModelProvider(this)[TitleViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_title)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        binding.navView.setupWithNavController(navHostFragment.navController)

        downloadDecksWithCards()
    }

    private fun setupMenu() {
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_card_list, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.settings -> {
                        startActivity(Intent(this@TitleActivity, SettingsActivity::class.java))
                    }
                }
                return true
            }
        })
    }

    private fun downloadDecksWithCards() {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("decksWithCards")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val decksWithCardsList = mutableListOf<DeckWithCards>()
                for (deckSnapshot in snapshot.children) {
                    val deck = deckSnapshot.child("deck").getValue(Deck::class.java)
                    val cardList = mutableListOf<Card>()
                    for (cardSnapShot in deckSnapshot.child("cards").children) {
                        val card = cardSnapShot.getValue(Card::class.java)
                        card?.let { cardList.add(it) }
                    }
                    val deckWithCards = DeckWithCards(deck!!, cardList)
                    decksWithCardsList.add(deckWithCards)
                }
                titleViewModel.saveDecksWithCardsLocally(decksWithCardsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.i("Failed to read database value")
            }
        })
    }

    override fun onStart() {
        super.onStart()
        // Only build menu when user is authenticated
        if(titleViewModel.getUserLoggedIn()) {
            setupMenu()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uploadDecksWithCards()
    }

    private fun uploadDecksWithCards() {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("decksWithCards")
        reference.setValue(titleViewModel.saveDecksWithCardsRemotely())
    }
}