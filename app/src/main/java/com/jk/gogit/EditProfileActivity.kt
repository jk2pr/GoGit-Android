package com.jk.gogit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.google.gson.Gson
import com.jk.gogit.model.UserProfile
import com.jk.gogit.ui.view.BaseActivity

import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : BaseActivity() {
    override fun getLayoutResourceId(): Int {
       return R.layout.activity_edit_profile
    }

    private val userProfile by lazy { Gson().fromJson(intent?.getStringExtra("UserData"), UserProfile::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableHomeInToolBar("Edit Your Profile", true)
       // window.setBackgroundDrawableResource(R.drawable.rounded_button)
      //  setContentView(R.layout.activity_edit_profile)
        // enableHomeInToolBar(getString(R.string.edit_your_profile), true)
        fillData()
        btn_save.setOnClickListener {
            saveData()
        }
        btn_reset.setOnClickListener {
            fillData()
        }
//        model.liveData.observe(this, this)


    }

    private fun fillData() {
        setData(edt_name, userProfile.name)
        setData(edt_bio, userProfile.bio)
        setData(edt_company, userProfile.company)
        setData(edt_location, userProfile.location)
        setData(edt_email, userProfile.email)
        setData(edt_blog, userProfile.blog)
    }

    private fun setData(textView: EditText?, data: String?) {
        textView?.apply {
            visibility = View.VISIBLE
            setText(data)
        }
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        for (i in 0 until menu!!.size())
            menu.getItem(i)?.isVisible = false
        menu.findItem(R.id.action_save).isVisible = true
        menu.findItem(R.id.action_reset).isVisible = true

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) { R.id.action_save -> {
            saveData()
        }
            R.id.action_reset ->
                fillData()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveData() {
        val name = edt_name.text.toString().trim()
        val email = edt_email.text.toString().trim()
        val blog = edt_blog.text.toString().trim()
        val company = edt_company.text.toString().trim()
        val location = edt_location.text.toString().trim()
        val hireable = edt_hireable.text.toString().trim()
        val bio = edt_bio.text.toString().trim()

        val eData = EditData(name, email, blog, company, location, hireable, bio)

        val intent = Intent()
        intent.putExtra("EditData", Gson().toJson(eData))
        setResult(Activity.RESULT_OK, intent)
        finish()


        /*  val eData = EditData(name, email, blog, company, location, hireable, bio)
          val u = model.saveProfile(*//*name, email, blog, company, location, hireable, bio*//* eData).subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread()) .subscribe({
            model.liveData.value = it
        }, { it ->
            onError(it)
        }, {

        })
        subscriptions.add(u)*/
    }

    data class EditData(val name: String, val email: String, val blog: String, val company: String, val location: String, val hireable: String, val bio: String)

}

