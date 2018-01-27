package com.dinizdesenvolve.planejar.activities

import android.app.DatePickerDialog
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import com.dinizdesenvolve.planejar.R
import com.dinizdesenvolve.planejar.domain.Moviment
import com.dinizdesenvolve.planejar.domain.TypeMovto
import kotlinx.android.synthetic.main.activity_movt.*
import java.time.LocalDate
import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.Toast
import com.dinizdesenvolve.planejar.utils.toDeviceFormat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MovtActivity : AppCompatActivity()  {


    private lateinit var dialog:DatePickerDialog
    private lateinit var mMovto:Moviment

    //firabse
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mMovtoDatabaseReference: DatabaseReference
    private lateinit var userId:String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movt)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userId = intent.getStringExtra("userId")

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mMovtoDatabaseReference = mFirebaseDatabase.getReference("profiles/"+this.userId+"/movtos")



        initFields()

        initDefaultValues()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initFields() {
        editTextVecto.setOnTouchListener( object : View.OnTouchListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                hideKeyboard(this@MovtActivity)
                try {
                    if (dialog.isShowing){
                        return true
                    }
                }catch (e:Exception){ }
                dialog = DatePickerDialog(this@MovtActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    mMovto.date = LocalDate.of(year, month+1, dayOfMonth)
                    editTextVecto.setText(mMovto.date.toDeviceFormat())
                }, mMovto.date.year, mMovto.date.monthValue, mMovto.date.dayOfMonth)
                dialog.show()

                return true
            }
        })

        butomOk.setOnClickListener {
            if (mMovto.isValid()){
                mMovto.keyId = mMovtoDatabaseReference.push().key
                mMovtoDatabaseReference.child(mMovto.keyId).setValue(mMovto.toMovto())
                finish()
            }else{
                Toast.makeText(this,"Preencha todos os campos",Toast.LENGTH_LONG).show()
            }
        }

        radioButtonExpence.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked){
                    mMovto.type = TypeMovto.EXPENCIE
                }
            }
        })

        radioButtonReceiver.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked){
                    mMovto.type = TypeMovto.RECEIVER
                }
            }
        })

        editTextDescription.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                mMovto.description = editTextDescription.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        editTextValue.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                try {
                    mMovto.value = editTextValue.text.toString().toDouble()
                }catch (e:Exception){

                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initDefaultValues(){
        mMovto = Moviment("",TypeMovto.EXPENCIE,"", LocalDate.now(),0.0)

        radioButtonExpence.isChecked = mMovto.IsExpencie()
        radioButtonReceiver.isChecked = mMovto.IsReceiver()
        editTextVecto.setText(mMovto.date.toDeviceFormat())
        editTextDescription.setText("")
        editTextDescription.isFocusable = true
        editTextValue.setText("")

    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
}
