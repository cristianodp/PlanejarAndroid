package com.dinizdesenvolve.planejar.domain

import android.os.Build
import android.support.annotation.RequiresApi
import com.dinizdesenvolve.planejar.utils.toDefaultFormat
import com.dinizdesenvolve.planejar.utils.toLocalDate
import java.io.Serializable
import java.lang.reflect.Type
import java.time.LocalDate



/**
 * Created by crist on 06/01/2018.
 */

enum class TypeMovto {
    EXPENCIE, RECEIVER
}

class Moviment(var keyId:String, var type:TypeMovto, var description:String, var date: LocalDate, var value:Double):Serializable {

    constructor(movto:MovtoFirebase) :this(movto.keyId,movto.type,movto.description,movto.date.toLocalDate(),movto.value)

    fun IsExpencie():Boolean{
        return type == TypeMovto.EXPENCIE
    }

    fun IsReceiver():Boolean{
        return type == TypeMovto.RECEIVER
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toMovto():MovtoFirebase{
        return MovtoFirebase(this.keyId,this.type,this.description, this.date.toDefaultFormat()!!,this.value)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isValid():Boolean {
        if (this.date < LocalDate.of(2018,1,1)){
            return false
        }

        if (this.description.length < 5 ){
            return false
        }

        if (this.value <= 0 ){
            return false
        }

        return true
    }
}

class MovtoFirebase(var keyId:String,val type:TypeMovto, val description:String, val date: String, val value:Double){

    constructor() : this("",TypeMovto.RECEIVER,"","",0.0)
}

