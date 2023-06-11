package  com.lofly.voiceme

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lofly.voiceme.databinding.ActivityMainBinding

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity: AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private var fileName: String = ""
    private var recorder: MediaRecorder? = null

    private var permissionToRecordAccepted = false
    private var permission: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION){
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else{
            false
        }
        if (!permissionToRecordAccepted) finish()
    }

    private fun startRecording(){
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile("${externalMediaDirs}/audiorecord.3gp")
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
                start()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun stopRecording(){
        recorder?.apply{
            try {
                stop()
                release()
            } catch (e: Exception){
                Log.d("STOP ERROR", e.message.toString())
            }
        }
        recorder = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fileName = "${externalMediaDirs}/audiorecord.3gp"

        requestPermissions(permission, REQUEST_RECORD_AUDIO_PERMISSION)

        binding.micButton.setOnClickListener{
            if (recorder == null) {
                startRecording()
                binding.micButton.text = "STOP"
            } else{
                stopRecording()
                binding.micButton.text = "START"
            }
        }
    }
    override fun onStop(){
        super.onStop()
        recorder?.release()
        recorder = null
    }
}