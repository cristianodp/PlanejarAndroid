package com.dinizdesenvolve.planejar.global

/**
 * Created by crist on 25/01/2018.
 */

fun getPathMoviment(userId:String):String{
    return "profiles/"+userId+"/movtos"
}

fun getPathMovimentMonth(userId:String,month:String):String{
    return "profiles/"+userId+"/summary/balanceMonth/"+month+"/movtos"
}


fun getPathSummaryMonth(userId:String):String{
    return "profiles/"+userId+"/summary/balanceMonth"
}

fun getPathSummaryDay(userId:String):String{
    return "profiles/"+userId+"/summary/balanceDay"
}

