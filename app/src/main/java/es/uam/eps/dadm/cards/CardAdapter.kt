package es.uam.eps.dadm.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import es.uam.eps.dadm.cards.Model.Card
import es.uam.eps.dadm.cards.databinding.ListItemCardBinding

class CardAdapter : RecyclerView.Adapter<CardAdapter.CardHolder>() {

    var data = listOf<Card>()
    private lateinit var binding: ListItemCardBinding

    inner class CardHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var local = binding
        private val imageButton: ImageButton = binding.infoCardButton
        private val details: LinearLayout = binding.cardDetails

        init {
            imageButton.setOnClickListener {
                details.isVisible = !details.isVisible
            }
        }

        fun bind(card: Card) {
            local.card = card

            itemView.setOnClickListener {
                it.findNavController()
                    .navigate(
                        CardListFragmentDirections
                        .actionCardListFragmentToCardEditFragment(card.id, card.deckId)
                    )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ListItemCardBinding.inflate(layoutInflater, parent, false)

        return CardHolder(binding.root)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(data[position])
        //(holder.itemView as TextView).text = "${item.question} \n ${item.answer}"
    }
}