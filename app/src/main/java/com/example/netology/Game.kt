package com.example.netology

import android.content.ContentValues
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.netology.databinding.FragmentGameBinding
import java.util.Calendar
import java.util.UUID
import kotlin.math.atan2
import kotlin.math.min
import kotlin.random.Random


class Game : Fragment(), View.OnClickListener {

    private val gameArgs: GameArgs by navArgs()
    private lateinit var gameBinding: FragmentGameBinding
    private var successCount: Int = 0
    private var failCount: Int = 0
    private var mice: MutableList<ImageView> = mutableListOf()
    private var baseMoveMs = 5000

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gameBinding = FragmentGameBinding.inflate(inflater, container, false)
        gameBinding.root.setOnClickListener(this)



        repeat(gameArgs.mouseCount) {
            createMouse()
        }


        startTimer(10)
        return gameBinding.root
    }

    private fun startTimer(timeSeconds: Long) {
        object : CountDownTimer(timeSeconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                gameBinding.timer.text = (millisUntilFinished / 1000 + 1).toString()
            }

            override fun onFinish() {
                val helper = DbHelper(requireContext())
                val db = helper.writableDatabase
                val cv = ContentValues()
                cv.put("id", UUID.randomUUID().toString())
                cv.put("total_count", successCount + failCount)
                cv.put("success_count", successCount)
                cv.put("created_at", Calendar.getInstance().time.toString())
                db.insert("games", null, cv)
                val action = GameDirections.actionGameToEnd(successCount + failCount, successCount)
                findNavController().navigate(action)
            }
        }.start()
    }


    private fun createMouse() {
        val displayMetrics = resources.displayMetrics

        val mouseSize = (min(displayMetrics.widthPixels, displayMetrics.heightPixels) / 5) * gameArgs.size
        val size = Point(displayMetrics.widthPixels, displayMetrics.heightPixels)

        val mouse = ImageView(this.context)
        mouse.setImageResource(R.drawable.mouse_game)
        mouse.layoutParams = ConstraintLayout.LayoutParams(mouseSize.toInt(), mouseSize.toInt())

        mouse.setOnClickListener {
            successCount++
            vibrate()
            it.animate().scaleX(1.2f).scaleY(1.2f).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).duration = 100
            }.duration = 100
        }
        gameBinding.root.addView(mouse)
        mice.add(mouse)

        startMouseMovement(mouse, size)
    }

    private fun startMouseMovement(mouse: ImageView, size: Point) {

        val screenWidth = size.x
        val screenHeight = size.y

        val startX = if (Random.nextBoolean()) {
            if (Random.nextBoolean()) -mouse.width.toFloat() else (screenWidth + mouse.width).toFloat()
        } else {
            Random.nextInt(0, screenWidth - mouse.width).toFloat()
        }

        val startY = if (startX == -mouse.width.toFloat() || startX == (screenWidth + mouse.width).toFloat()) {
            Random.nextInt(0, screenHeight - mouse.height).toFloat()
        } else {
            if (Random.nextBoolean()) -mouse.height.toFloat() else (screenHeight + mouse.height).toFloat()
        }

        mouse.x = startX
        mouse.y = startY

        moveMouseToRandomPoint(mouse, size)
    }

    private fun moveMouseToRandomPoint(mouse: ImageView, size: Point) {

        val screenWidth = size.x
        val screenHeight = size.y

        val endX = Random.nextInt(0, screenWidth - mouse.width).toFloat()
        val endY = Random.nextInt(0, screenHeight - mouse.height).toFloat()
        val startX = mouse.x
        val startY = mouse.y
        val angle = atan2((startY - endY).toDouble(), (startX - endX).toDouble()).toFloat()

        val targetRotation = angle * (180 / Math.PI.toFloat())
        mouse.animate()
            .rotation(targetRotation)
            .setDuration(100)
            .start()

        val duration = (baseMoveMs / gameArgs.speed).toLong()

        mouse.animate()
            .x(endX)
            .y(endY)
            .setDuration(duration)
            .withEndAction {
                moveMouseToRandomPoint(mouse, size)
            }
            .start()
    }

    override fun onClick(v: View?) {
        failCount++
    }

    private fun vibrate() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(50)
        }
    }

}