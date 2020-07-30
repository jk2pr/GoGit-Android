package com.jk.gogit

import android.os.Bundle
import com.jk.gogit.ui.view.BaseActivity
import com.jk.gogit.utils.NavUtils
import kotlinx.android.synthetic.main.activity_no_login_main.*

class NoLoginMainActivity : BaseActivity() {
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_no_login_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableHomeInToolBar(resources.getString(R.string.app_name), false)
        btn_login.setOnClickListener {
            NavUtils.redirectToLogin(this)
        }
    }

}
