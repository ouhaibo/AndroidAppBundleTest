package com.ohb.androidappbundletest

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private var splitInstallManager: SplitInstallManager? = null
    private val action_to_feature1 = "action_launch_feature_one"
    private val action_to_feature_joke = "action_launch_feature_joke"
    private val feature_name_1 = "dynamic_feature_1"
    private val feature_name_2 = "some_funny_feature"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        splitInstallManager = SplitInstallManagerFactory.create(application)!!
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        txt_jump_df1.setOnClickListener {
            //TODO:跳转到dynamic feature 1的界面
            if (splitInstallManager!!.installedModules.contains(feature_name_1)) {
                Intent().setClassName(packageName, "com.ohb.dynamic_feature_1.Feature1Activity")
                    .also {
                        startActivity(it)
                    }
                return@setOnClickListener
            }
            val request = SplitInstallRequest.newBuilder().addModule(feature_name_1).build()
            splitInstallManager!!.startInstall(request)
        }

        txt_jump_df2.setOnClickListener {
            //TODO:跳转到dynamic feature 2的界面
            if (splitInstallManager!!.installedModules.contains(feature_name_2)) {
                Intent().setClassName(packageName, "com.ohb.some_funny_feature.MakeJokeActivity")
                    .also {
                        startActivity(it)
                    }
                return@setOnClickListener
            }
            val request = SplitInstallRequest.newBuilder().addModule(feature_name_2).build()
            splitInstallManager!!.startInstall(request)
        }
        splitInstallManager!!.registerListener {
            when (it.status()) {
                PENDING -> {
                }
                DOWNLOADING -> {

                    when (it.sessionId()) {

                    }
                }
                DOWNLOADED -> {
                }
                INSTALLED -> {
                    Toast.makeText(
                        this@MainActivity,
                        "dynamic feature installed",
                        Toast.LENGTH_SHORT
                    ).show()
//                    val newContext = createPackageContext(packageName, 0)
//                    val assetManager = newContext.assets
//                    val intent = Intent()
//                    var componentName: ComponentName? = null
                    if (it.moduleNames().contains(feature_name_1)) {
//                        intent.action = action_to_feature1
//                        componentName = ComponentName(newContext, "com.ohb.dynamic_feature_1.Feature1Activity")
                        Intent().setClassName(packageName, "com.ohb.dynamic_feature_1.Feature1Activity")
                            .also { startActivity(it) }
                    } else if (it.moduleNames().contains(feature_name_2)) {
//                        intent.action = action_to_feature_joke
//                        componentName = ComponentName(newContext, "com.ohb.some_funny_feature.MakeJokeActivity")
                        Intent().setClassName(packageName, "com.ohb.some_funny_feature.MakeJokeActivity")
                            .also { startActivity(it) }
                    }
//                    intent.component = componentName
//                    startActivity(intent)
                }
                INSTALLING -> {
                    Toast.makeText(this@MainActivity, "installing module", Toast.LENGTH_SHORT).show()
                }
                REQUIRES_USER_CONFIRMATION -> {
                    startIntentSender(it.resolutionIntent().intentSender, null, 0, 0, 0)
                }
                FAILED -> {
                    if (it.errorCode() == SplitInstallErrorCode.SERVICE_DIED) {
                        return@registerListener
                    }
                }
                CANCELING -> {
                }
                CANCELED -> {
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
