package com.dinizdesenvolve.planejar.ado

import android.os.Build
import android.support.annotation.RequiresApi
import com.dinizdesenvolve.planejar.domain.Moviment
import com.dinizdesenvolve.planejar.domain.Summary
import com.dinizdesenvolve.planejar.utils.getYearMonth
import com.google.firebase.database.DataSnapshot
import java.time.LocalDate
import java.util.ArrayList

/**
 * Created by crist on 25/01/2018.
 */
class SummaryADO(override var path: String, override var dataChange: IFirebaseDatadaseADO.IDataChange) :IFirebaseDatadaseADO<Summary> {

    var list: ArrayList<Summary>
    init {
        list = ArrayList<Summary>()
        initDatabaseListener()
    }

    override fun removeList(item: Summary?) {
        if (item != null) {
            val oldMov = list.filter{ it-> it.yearMonth == item.yearMonth }[0]
            list.remove(oldMov)
        }
    }

    override fun updateList(item: Summary?) {

        if (item != null) {
            try {
                val oldMov = list.filter { it-> it.yearMonth == item.yearMonth }[0]
                val idx = list.indexOf(oldMov)
                list[idx] = item
            }catch (e:Exception){
                list.add(item)
            }
        }

    }

    override fun processSnapshot(snapshot: DataSnapshot?): Summary? {
        if (snapshot != null) {
            val summary = snapshot.getValue(Summary::class.java)
            return summary
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrent():Summary?{
        try {
            val current = list.filter { it -> it.yearMonth == LocalDate.now().getYearMonth()}[0]
            return current
        }catch (e:Exception){
            return null
        }

    }


}