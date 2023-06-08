package com.lofly.voiceme

import android.annotation.SuppressLint
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lofly.voiceme.databinding.ActivityMainBinding
import com.lofly.voiceme.ui.theme.VoicemeTheme
import java.io.IOException

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaRecorder: MediaRecorder
    private var fileName: String = ""
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.micButton.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startRecording()
                }
                MotionEvent.ACTION_UP -> {
                    stopRecording()
                }
            }
            false
        }
        fileName = Environment.getExternalStorageDirectory().absolutePath+"/recorded_audio.3gp"
        mediaRecorder = MediaRecorder().apply() {
            setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        }

    }
    private fun startRecording(){
        try {
            mediaRecorder.prepare()
            mediaRecorder.start()
        } catch (e: IOException){
            Log.e("AUDIO_RECORDING", "prepare() failed")
        }
    }
    private fun stopRecording(){
        mediaRecorder.stop()
        mediaRecorder.release()
        mediaRecorder = MediaRecorder()
        Toast.makeText(this, "Voice recording completed", Toast.LENGTH_SHORT).show()
    }

}
