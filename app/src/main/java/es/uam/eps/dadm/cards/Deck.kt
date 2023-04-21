package es.uam.eps.dadm.cards

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.lang.Exception

 */
@Entity(tableName = "decks_table")
data class Deck(
    @PrimaryKey var deckId: Long,
    var name: String
    ) {
    /*
    var cards: MutableList<Card> = mutableListOf()
    var repetitionsToday = 0
    var repititionsTotal = cards.sumOf { it.repetitions }
    var averageQuality = cards.sumOf { it.quality ?: 0 } / cards.size

    var numberOfBadQualityCards = cards.filter { it.quality!! < 2.0 }.size
    var numberOfOkQualityCards = cards.filter { it.quality!! in 2 .. 3 }.size
    var numberOfGoodQualityCards = cards.filter { it.quality!! > 3 }.size


    fun addCard() {
        // User prompt
        println("Adding card to $name deck")

        // First ask about the type of card to be added
        var type: Int?
        do {
            print("Type the type (0 -> Card 1 -> Cloze): ")
            type = readln().toIntOrNull()
            if(type == null)
                println("Please enter something")
        } while(type == null && type !in 1..2)

        // Prompt user to enter the question
        var question: String
        do {
            print("Type the question: ")
            question = readln()
            if(question.trim() == "")
                println("Please enter something")
        } while(question.trim() == "")

        // Prompt user to enter the answer
        var answer: String
        do {
            print("Type the answer: ")
            answer = readln()
            if(answer.trim() == "")
                println("Please enter something")
        } while(answer.trim() == "")

        if(type == 0)
            cards += Card(question, answer)
        else if (type == 1)
            cards += Cloze(question, answer)

        println("Card successfully added")
    }

    // TODO: Write comments
    fun deleteCard() {
        println("Please choose a card you would like to delete from the list below.\n" +
                "To delete it type in the question: ")
        var exists = false
        var question = ""
        // Get and check user input
        do {
            // Check if user input is just an empty string
            if(question == "") {
                println("Please enter a question!")
                question = readln()
            } else {
                // Check if the card with the input question exists
                exists =  checkDeck(question)

                // If the respective card exits remove the card
                if (!exists) {
                    println("The specified card does not exist")
                } else if(exists) {
                    cards.removeIf{ it.question == question }
                }
            }
        } while((question == "") || !exists)
    }

    // TODO: Write comments
    private fun checkDeck(question: String): Boolean {
        return cards.any { it.question == question }
    }

    // TODO: Write comments
    fun listCards() {
        if(cards.size == 0) {
            println("No cards in the deck yet.\n")
        } else {
            for (card in cards) {
                println("Question: ${card.question}")
                println("Answer: ${card.answer}\n")
            }
        }
    }

    /**
     * Simulates the practicing of flashcards in a deck
     * @param period The number of days to simulate for
     */
    fun simulate(period: Int, reviewStyle: Int) {
        println("Simulation of the deck $name:")
        val f = DateTimeFormatter.ISO_LOCAL_DATE
        var now = LocalDateTime.now()

        val repsBefore = cards.sumOf { it.repetitions }

        // Loop through the days
        for(day in 0 until period) {
            println("Date: ${f.format(now)}")
            // Loop for every day through the cards
            cards.forEach {
                if(reviewStyle == 0) {
                    if (now.toLocalDate().isEqual(LocalDateTime.parse(it.nextPracticeDate).toLocalDate()) ||
                        now.toLocalDate().isAfter(LocalDateTime.parse(it.nextPracticeDate).toLocalDate())) {
                        it.show()
                        it.update(now)
                        it.details()
                    }
                } else if (reviewStyle == 1) {
                    it.show()
                    it.update(now)
                    it.details()
                }
            }
            // Advance with every day the respective date
            now = now.plusDays(1)
        }

        val repsAfter = cards.sumOf { it.repetitions }
        repetitionsToday += repsAfter - repsBefore
    }

    /**
     * Writes a card deck to a .txt file
     * @param name Filename
     */
    fun writeCards(name: String) {
        println("Writing Cards of deck $name to file ${name.lowercase()}.txt")
        try {
            FileWriter("data/" + name.lowercase() + ".txt").use { writer ->
                for (card in cards) {
                    writer.write(card.toString() + "\n")
                }
            }
            println("Deck successfully written to file ${name.lowercase()}.txt")
        } catch(e: Exception) {
            println(e)
        }
    }

    override fun toString(): String = "$name | $id"

    /**
     * Reads a card deck from a .txt file
     * @param name Filename
     */
    fun readCards(name: String) {
        try {
            val lines: List<String> = File("data/$name.txt").readLines()
            lines.forEach{
                if(it.contains("card")) {
                    cards += Card.fromString(it)
                } else if (it.contains("cloze")) {
                    cards += Cloze.fromString(it)
                }
            }
        } catch(fnf: FileNotFoundException) {
            println("File not found. Try again.")
        } catch(e: Exception) {
            println(e)
        }
    }

    fun actualizeStatistics() {
        repititionsTotal = cards.sumOf { it.repetitions }
        averageQuality = cards.sumOf { it.quality ?: 0 } / cards.size

        numberOfBadQualityCards = cards.filter { it.quality!! > 3 }.size
        numberOfOkQualityCards = cards.filter { it.quality!! > 3 }.size
        numberOfGoodQualityCards = cards.filter { it.quality!! > 3 }.size
    }

    // TODO: Write comments
    companion object {
        fun fromString(cad: String): Deck {
            val properties = cad.split(" | ")

            // Return the deck instance
            return Deck(properties[0], properties[1])
        }
    }
*/
}