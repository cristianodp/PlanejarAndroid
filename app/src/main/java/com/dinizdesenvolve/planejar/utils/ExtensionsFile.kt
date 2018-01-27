package com.dinizdesenvolve.planejar.utils

import android.annotation.SuppressLint
import android.os.Build
import android.support.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*



/**
 * Created by crist on 07/01/2018.
 */



fun Float.toCurrencyFormat():String{
    var test = this
    return test.toString()
}

fun Double.toCurrencyFormat():String{
    var test = this
    return test.toString()
}


//LocalDate
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toDeviceFormat():String{
    return this.format(DateTimeFormatter.ISO_LOCAL_DATE)
}



@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toDefaultFormat(): String? {
    val FORMATTER:DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var date = this.format(FORMATTER)
    return date
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.getYearMonth(): String? {
    val FORMATTER:DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
    var date = this.format(FORMATTER)
    return date+"-01"
}


//String
@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDate(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(this, formatter)
    return date
}