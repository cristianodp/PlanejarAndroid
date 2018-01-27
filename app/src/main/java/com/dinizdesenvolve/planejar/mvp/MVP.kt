package com.dinizdesenvolve.planejar.mvp

/**
 * Created by crist on 31/12/2017.
 */
interface MVP {

    interface ModelImpl{

    }

    interface PresenterImpl{
        fun setView(view: MVP.ViewImpl)
    }

    interface ViewImpl{}
}