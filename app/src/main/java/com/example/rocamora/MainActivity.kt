package com.example.rocamora

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var targetValue = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val slider: Slider = findViewById(R.id.numberSlider)
        val targetValueText: TextView = findViewById(R.id.targetValue)

        targetValueText.text = getString(R.string.target_value, targetValue)

        slider.addOnChangeListener { _, value, _ ->
            targetValue = value.toInt()
            targetValueText.text = getString(R.string.target_value, targetValue)
            rollDice()
        }
    }

    private fun rollDice() {
        val dice = Dice(6)
        val diceImages = listOf<ImageView>(
            findViewById(R.id.imageView1),
            findViewById(R.id.imageView2)
        )

        val resultText: TextView = findViewById(R.id.resultText)
        val konfettiView: KonfettiView = findViewById(R.id.konfettiView)

        // Animation de secouement des dés
        diceImages.forEach { diceImage ->
            val shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_animation)
            diceImage.startAnimation(shakeAnimation)
        }

        val rolls = diceImages.map { dice.roll() }

        rolls.forEachIndexed { index, roll ->
            val drawableResource = getDiceDrawable(roll)
            diceImages[index].apply {
                setImageResource(drawableResource)
                contentDescription = roll.toString()
            }
        }

        val isWin = rolls.sum() == targetValue
        resultText.text = if (isWin) {
            // Lance les confettis si c'est gagné
            val party = Party(
                speed = 0f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
                position = Position.Relative(0.5, 0.9)
            )
            konfettiView.start(party)
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