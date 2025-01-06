package com.example.rocamora

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton: Button = findViewById(R.id.button)

        rollButton.setOnClickListener { rollDice() }

        rollDice()
    }

    private fun rollDice() {
        val dice = Dice(6)
        val diceImages = listOf<ImageView>(
            findViewById(R.id.imageView1),
            findViewById(R.id.imageView2)
        )

        val resultText: TextView = findViewById(R.id.resultText)

        // Roll both dice and store results
        val rolls = diceImages.map { dice.roll() }

        // Update images and get results
        rolls.forEachIndexed { index, roll ->
            val drawableResource = getDiceDrawable(roll)
            diceImages[index].apply {
                setImageResource(drawableResource)
                contentDescription = roll.toString()
            }
        }

        // Check if dice show same number
        resultText.text = if (rolls[0] == rolls[1]) {
            getString(R.string.win_message)
        } else {
            getString(R.string.lose_message)
        }
    }

    private fun getDiceDrawable(value: Int) = when (value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

}

class Dice(private val numSides: Int) {

    fun roll(): Int {
        return (1..numSides).random()
    }
}