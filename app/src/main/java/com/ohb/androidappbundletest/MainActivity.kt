package com.ohb.androidappbundletest

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
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
    private var mSessionId1 = 0
    private var mSessionId2 = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        splitInstallManager = SplitInstallManagerFactory.create(applicationContext)!!
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        txt_jump_df1.setOnClickListener {
            //TODO:跳转到dynamic feature 1的界面
            val request = SplitInstallRequest.newBuilder().addModule("dynamic_feature_1").build()
            splitInstallManager!!.startInstall(request).addOnSuccessListener {
                mSessionId1 = it
            }.addOnFailureListener {
                when ((it as SplitInstallException).errorCode) {
                    SplitInstallErrorCode.NETWORK_ERROR -> {

                    }
                    SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED -> {

                    }
                }
            }.addOnCompleteListener {

            }
        }

        txt_jump_df2.setOnClickListener {
            //TODO:跳转到dynamic feature 2的界面
            val request = SplitInstallRequest.newBuilder().addModule("some_funny_feature").build()
            splitInstallManager!!.startInstall(request).addOnSuccessListener {
                mSessionId2 = it
            }.addOnFailureListener {
                when ((it as SplitInstallException).errorCode) {
                    SplitInstallErrorCode.NETWORK_ERROR -> {

                    }
                    SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED -> {

                    }
                }
            }.addOnCompleteListener {

            }
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
                    val newContext = createPackageContext(packageName, 0)
                    val assetManager = newContext.assets
                    val intent = Intent()
                    var componentName: ComponentName? = null
                    when (it.sessionId()) {
                        mSessionId1 -> {
                            intent.action = action_to_feature1
                            componentName = ComponentName(newContext, "com.ohb.dynamic_feature_1.Feature1Activity")
                        }
                        mSessionId2 -> {
                            intent.action = action_to_feature_joke
                            componentName = ComponentName(newContext, "com.ohb.some_funny_feature.MakeJokeActivity")
                        }
                    }
                    intent.component = componentName
                    startActivity(intent)
                }
                INSTALLING -> {
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
