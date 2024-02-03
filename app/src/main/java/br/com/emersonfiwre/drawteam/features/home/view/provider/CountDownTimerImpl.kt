package br.com.emersonfiwre.drawteam.features.home.view.provider

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.CountDownTimer
import android.widget.TextView
import br.com.emersonfiwre.drawteam.R
import java.util.concurrent.TimeUnit

class CountDownTimerImpl(
    private val textViewTimer: TextView,
    milliseconds: Long,
    private val context: Context
): CountDownTimer(milliseconds, INT_ONE_SECOND) {

    private var mediaPlayer: MediaPlayer? = null

    override fun onTick(millisUntilFinished: Long) {
        val hms = String.format(
            "%02d:%02d",
            (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished))),
            (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
            ))
        )

        textViewTimer.text = hms
    }

    override fun onFinish() {
        playSound()

        textViewTimer.run {
            text = this.context.getString(R.string.draw_team_reset_seconds)
        }
    }

    fun startTimer() {
        this.start()
        mediaPlayer?.stop()
    }

    fun stopTimer() {
        this.cancel()
        mediaPlayer?.stop()
    }

    private fun playSound() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        mediaPlayer = mediaPlayer ?: MediaPlayer().apply {
            setDataSource(context, notification)
            setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_ALARM).build()
            )
            prepare()
            isLooping = false
        }
        mediaPlayer?.start()
    }

    companion object {

        private const val INT_ONE_SECOND = 1000L
    }
}