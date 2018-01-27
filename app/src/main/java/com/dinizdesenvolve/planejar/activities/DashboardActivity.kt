package com.dinizdesenvolve.planejar.activities

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.dinizdesenvolve.planejar.R
import com.dinizdesenvolve.planejar.adapters.RecyclerMovtAdapter
import com.dinizdesenvolve.planejar.ado.IFirebaseDatadaseADO
import com.dinizdesenvolve.planejar.ado.MovimentADO
import com.dinizdesenvolve.planejar.ado.SummaryADO
import com.dinizdesenvolve.planejar.domain.Moviment


import com.dinizdesenvolve.planejar.domain.Summary
import com.dinizdesenvolve.planejar.global.getPathMoviment
import com.dinizdesenvolve.planejar.global.getPathSummaryMonth
import com.dinizdesenvolve.planejar.utils.toCurrencyFormat
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_dashboard_content.*
import java.util.*
import android.widget.ArrayAdapter
import com.dinizdesenvolve.planejar.adapters.RecyclerSummaryAdapter
import com.dinizdesenvolve.planejar.global.getPathMovimentMonth


class DashboardActivity : AppCompatActivity() {

    //firabase
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    private lateinit var mMovimentADO:MovimentADO
    private lateinit var mSummaryADO:SummaryADO

    private val RC_SIGN_IN = 1
    private var mRecyclerMovtAdapter: RecyclerMovtAdapter? = null


    private var userId:String = ""
    private var monthSelected:String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)

        supportActionBar?.let { support ->
            support.setDisplayShowTitleEnabled(false)
            //support.setHomeAsUpIndicator(iconDrawable)
        }

        loadSammary(Summary())

        floatingActionButton.setOnClickListener { /*view ->*/

            CreateNewMoviment()
        }

        initFirebaseAuth()

    }
    override fun onResume() {
        super.onResume()
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)
    }

    override fun onPause() {
        super.onPause()
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener)
    }




    private fun loadSammary(summary:Summary?){
        if (summary != null){
            dash_saved.setValue(summary.saved.toCurrencyFormat())
            dash_receive.setValue(summary.received.toCurrencyFormat())
            dash_pay.setValue(summary.expense.toCurrencyFormat() )
            dash_balence.setValue(summary.balance.toCurrencyFormat() )
        }else{
            dash_saved.setValue(0.0f.toCurrencyFormat())
            dash_receive.setValue(0.0f.toCurrencyFormat())
            dash_pay.setValue(0.0f.toCurrencyFormat() )
            dash_balence.setValue(0.0f.toCurrencyFormat() )
        }
    }
    private fun initFirebaseAuth() {

        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->

            val user = firebaseAuth.currentUser

            if (user != null){
                onSignedInInitialize(user.uid)

            }
            else{
                onSignedOutCleanup()
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(
                                        Arrays.asList<AuthUI.IdpConfig>(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                /* new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),*/
                                                AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()/*,
                                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()*/))
                                .build(),
                        RC_SIGN_IN)

            }

        }
    }

    fun CreateNewMoviment(){
        val i = Intent(this, MovtActivity::class.java)
        i.putExtra("userId", userId)
        startActivity(i)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this@DashboardActivity, "Sing in!", Toast.LENGTH_SHORT).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this@DashboardActivity, "Sing in canceled", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out_menu -> {
                AuthUI.getInstance().signOut(this)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun onSignedOutCleanup() {

    }



    private fun onSignedInInitialize(authUI:String) {
        this.userId = authUI
        atchDatabaseListener()
    }

    private fun atchDatabaseListener() {

        mSummaryADO = SummaryADO(getPathSummaryMonth(userId),object:IFirebaseDatadaseADO.IDataChange{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun notifyDataChanged() {

                val list = ArrayList<String>()

                for (item in mSummaryADO.list){
                    list.add(item.yearMonth)
                }

                val dataAdapter = ArrayAdapter<String>(this@DashboardActivity,
                        android.R.layout.simple_spinner_item, list)
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spinnerYearMonth.adapter = dataAdapter
                mSummaryADO.getCurrent()?.let { current ->
                    loadSammary(current)
                }

                spinnerYearMonth.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        parent?.let{
                            loadSammary(mSummaryADO.list[position])
                            monthSelected = mSummaryADO.list[position].yearMonth
                            //recyclerView.getRecycledViewPool().clear()
                            //mRecyclerMovtAdapter!!.
                            //recyclerView.recycledViewPool.clear()
                            //mRecyclerMovtAdapter!!.notifyDataSetChanged()
                            //mMovimentADO.clear()
                            mMovimentADO = MovimentADO(getPathMovimentMonth(userId,monthSelected),object:IFirebaseDatadaseADO.IDataChange {
                               @TargetApi(Build.VERSION_CODES.O)
                               @RequiresApi(Build.VERSION_CODES.O)
                               override fun notifyDataChanged() {
/*
                                   if (mRecyclerMovtAdapter != null) {
                                       mRecyclerMovtAdapter!!.notifyDataSetChanged()
                                   } else {*/
                                       mRecyclerMovtAdapter = RecyclerMovtAdapter(mMovimentADO.list)
                                       recyclerView.setLayoutManager(LinearLayoutManager(this@DashboardActivity))
                                       recyclerView.adapter = mRecyclerMovtAdapter
                                       recyclerView
                                 //  }
                               }

                           })


                        }

                    }
                }


            }

        })
    }




}
