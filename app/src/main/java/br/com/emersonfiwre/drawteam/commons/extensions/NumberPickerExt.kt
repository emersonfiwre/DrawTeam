package br.com.emersonfiwre.drawteam.commons.extensions

import android.widget.NumberPicker

private const val MIN_TO_MILLISECONDS = 60000

fun NumberPicker.minutesToMilliseconds(): Long {
    return (this.value * MIN_TO_MILLISECONDS).toLong()
}

fun NumberPicker.millisecondsToInt(milliseconds: Long) = this.run {
    this.value = (milliseconds / MIN_TO_MILLISECONDS).toInt()
}