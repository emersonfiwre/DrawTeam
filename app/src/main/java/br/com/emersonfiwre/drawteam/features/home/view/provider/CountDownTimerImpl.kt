package br.com.emersonfiwre.drawteam.features.home.view.provider

import android.os.CountDownTimer
import android.widget.TextView
import br.com.emersonfiwre.drawteam.R
import java.util.concurrent.TimeUnit

class CountDownTimerImpl(
    private val textViewTimer: TextView,
    milliseconds: Long
): CountDownTimer(milliseconds, INT_ONE_SECOND) {
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
        textViewTimer.run {
            text = this.context.getString(R.string.draw_team_reset_seconds)
        }
    }

    companion object {

        private const val INT_ONE_SECOND = 1000L
    }
}