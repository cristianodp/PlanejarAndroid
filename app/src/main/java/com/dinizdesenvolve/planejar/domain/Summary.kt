package com.dinizdesenvolve.planejar.domain

import java.util.*

/**
 * Created by crist on 31/12/2017.
 */
class Summary(var yearMonth:String, var received: Float, var expense:Float, var balance:Float, var saved:Float){
    constructor():this("",0.0f,0.0f,0.0f,0.0f)
}
