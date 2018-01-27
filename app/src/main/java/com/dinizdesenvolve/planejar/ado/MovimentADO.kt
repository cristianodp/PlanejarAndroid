package com.dinizdesenvolve.planejar.ado

import com.dinizdesenvolve.planejar.domain.Moviment
import com.dinizdesenvolve.planejar.domain.MovtoFirebase
import com.dinizdesenvolve.planejar.domain.Summary
import com.google.firebase.database.DataSnapshot
import java.util.ArrayList

/**
 * Created by crist on 25/01/2018.
 */
class MovimentADO(override var path: String, override var dataChange: IFirebaseDatadaseADO.IDataChange) :IFirebaseDatadaseADO<Moviment> {

    var list: ArrayList<Moviment>
    init {
        list = ArrayList<Moviment>()
        initDatabaseListener()
    }

    override fun removeList(item: Moviment?) {
        if (item != null) {
            val oldMov = list.filter { it-> it.keyId == item.keyId}[0]
            list.remove(oldMov)
        }
    }

    override fun updateList(item: Moviment?) {
        if (item != null) {
            try {
                val oldMov = list.filter { it-> it.keyId == item.keyId}[0]
                val idx = list.indexOf(oldMov)
                list[idx] = item
            }catch (e:Exception){
                list.add(item)
            }
        }
    }

    override fun processSnapshot(snapshot: DataSnapshot?): Moviment? {
        if (snapshot != null) {
            snapshot.getValue(MovtoFirebase::class.java)?.let{movtoFirebase ->
                return Moviment(movtoFirebase)
            }
        }
        return null

    }


}