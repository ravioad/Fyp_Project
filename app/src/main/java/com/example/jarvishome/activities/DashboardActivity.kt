package com.example.jarvishome.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.jarvishome.*
import com.example.jarvishome.adaptors.DashboardBottomNavAdapter
import com.example.jarvishome.databinding.ActivityDashboardBinding
import com.example.jarvishome.fragments.HomeFragment
import com.example.jarvishome.fragments.SettingsFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_dashboard.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import android.speech.tts.Voice
import android.util.Log


private const val REQUEST_CODE_PERMISSIONS = 10

class DashboardActivity : AppCompatActivity(), View.OnClickListener {

    private var tts: TextToSpeech? = null
    lateinit var bottomNavAdapter: DashboardBottomNavAdapter
    private lateinit var binding: ActivityDashboardBinding
    private var textReceived = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor(false)
        setViewPager()
        speakUp("")
        binding.floatingActionButton.setOnClickListener(this)
    }

    private fun speakUp(text: String) {
        tts = TextToSpeech(applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
//                showToastShort("Not Error: $status")
                tts?.language = Locale.getDefault()
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                showToastShort("Error: $status")
            }
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSIONS)
    private fun startRecognizerWithPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.RECORD_AUDIO)) {
            startRecognizer()
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.speech_recognition),
                REQUEST_CODE_PERMISSIONS,
                Manifest.permission.RECORD_AUDIO
            )
        }
    }

    private var recognizerIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val resultData =
                        result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val text = resultData?.get(0).toString()
                    this.textReceived = text
                    showToastShort(text)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    @SuppressLint("InflateParams")
    private fun setViewPager() {
        bottomNavAdapter = DashboardBottomNavAdapter(supportFragmentManager)
        bottomNavAdapter.addFragment(HomeFragment.newInstance())
        bottomNavAdapter.addFragment(SettingsFragment.newInstance())
        main_viewpager.adapter = bottomNavAdapter
        bottomTabLayout.setupWithViewPager(main_viewpager)
        main_viewpager.offscreenPageLimit = 0
        for (i in 0 until bottomNavAdapter.count) {
            val customView = layoutInflater.inflate(R.layout.custom_tab_layout, null)
            val image = customView.findViewById<ImageView>(R.id.icon)
            val params = image.layoutParams
            params.width = resources.getDimension(R.dimen._22sdp).toInt()
            params.height = resources.getDimension(R.dimen._22sdp).toInt()
            image.layoutParams = params
            when (i) {
                0 -> {
                    Glide.with(this).load(R.drawable.home).into(image)
                    image.select(this)
                }
                1 -> {
                    Glide.with(this).load(R.drawable.settings).into(image)
                    image.unSelect(this)
                }
            }
            bottomTabLayout.getTabAt(i)?.customView = customView
        }
        val tabStrip = bottomTabLayout.getChildAt(0)
        if (tabStrip is ViewGroup) {
            for (i in 0 until tabStrip.childCount) {
                val tabView = tabStrip.getChildAt(i)
                val margin = resources.getDimension(R.dimen._40sdp).toInt()
                tabView.setPadding(margin, 0, margin, 0)
            }
            bottomTabLayout.requestLayout()
        }
        addBottomNavListener()
    }

    private fun addBottomNavListener() {
        bottomTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                for (i in 0 until bottomNavAdapter.count) {
                    val img =
                        bottomTabLayout.getTabAt(i)?.customView!!.findViewById<ImageView>(R.id.icon)
                    when (i) {
                        0 -> {
                            if (i == tab?.position) {
                                img.select(this@DashboardActivity)
                                changeStatusBarColor(false)
                            } else {
                                img.unSelect(this@DashboardActivity)
                            }
                        }
                        1 -> {
                            if (i == tab?.position) {
                                changeStatusBarColor()
                                img.select(this@DashboardActivity)
                            } else {
                                img.unSelect(this@DashboardActivity)
                            }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        bottomTabLayout.getTabAt(0)?.select()
//        selectedFragment = bottomNavAdapter.getItem(0) as MyBaseFragment
        main_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
//                selectedFragment = bottomNavAdapter.getItem(position) as MyBaseFragment

            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.floatingActionButton -> {
                if (textReceived != "") {
                    speakUp("Ok Sir!")
                    return
                }
                startRecognizerWithPermission()
            }
        }
    }

    private fun startRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            showToastShort("Device does not support speech recognition!")
        } else {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!")
            recognizerIntentLauncher.launch(intent)
        }
    }

    override fun onPause() {
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        super.onPause()
    }
}