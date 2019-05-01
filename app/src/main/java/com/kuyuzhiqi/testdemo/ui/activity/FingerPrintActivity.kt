package com.kuyuzhiqi.acrossdemo.ui

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import com.kuyuzhiqi.testdemo.R
import kotlinx.android.synthetic.main.activity_fingerprint.iv_indicator
import kotlinx.android.synthetic.main.activity_fingerprint.tv_status
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

class FingerPrintActivity : AppCompatActivity() {


    val fingerprintManagerCompat: FingerprintManagerCompat by lazy {
        FingerprintManagerCompat.from(this@FingerPrintActivity)
    }
    private var mCancellationSignal: CancellationSignal? = null
    private var inAuthentication: Boolean = false
    /**
     * 标识是否是用户主动取消的认证。
     */
    private var isSelfCancelled: Boolean = false
    private lateinit var mCipher: Cipher


    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fingerprint)

        initData()
        initView()
    }

    override fun onPause() {
        super.onPause()
        stopListening()
    }


    private fun initView() {
        iv_indicator.setOnClickListener {
            if (!inAuthentication) {
                startListening()
                tv_status.setText("正在认证")
            }else{
                stopListening()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @TargetApi(Build.VERSION_CODES.M)
    private fun initData() {
        if (!fingerprintManagerCompat.isHardwareDetected) {
            Toast.makeText(this, "您的设备不支持指纹识别", Toast.LENGTH_SHORT).show()
            return
        }
        if (!fingerprintManagerCompat.hasEnrolledFingerprints()) {
            Toast.makeText(this, "请录入指纹后重试", Toast.LENGTH_SHORT).show()
            return
        }
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val builder = KeyGenParameterSpec.Builder(
            DEFAULT_KEY_NAME,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setUserAuthenticationRequired(true)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
        keyGenerator.init(builder.build())
        keyGenerator.generateKey()
        val key = keyStore.getKey(DEFAULT_KEY_NAME, null)
        mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        mCipher.init(Cipher.ENCRYPT_MODE, key)
    }

    private fun startListening() {
        inAuthentication = true
        isSelfCancelled = false
        mCancellationSignal = CancellationSignal()
        fingerprintManagerCompat.authenticate(
            FingerprintManagerCompat.CryptoObject(mCipher), 0, mCancellationSignal,
            object : FingerprintManagerCompat.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(this@FingerPrintActivity, "指纹认证成功", Toast.LENGTH_SHORT).show()
                    tv_status.setText("认证成功")
                    inAuthentication = false
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    tv_status.setText("认证失败")
                    inAuthentication = false
                }

                override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errMsgId, errString)
                    tv_status.setText("认证错误")
                    inAuthentication = false
                }

                override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
                    super.onAuthenticationHelp(helpMsgId, helpString)
                    inAuthentication = false
                }
            }, null
        )

    }

    private fun stopListening() {
        if (mCancellationSignal != null) {
            mCancellationSignal?.cancel()
            mCancellationSignal = null
            isSelfCancelled = true
        }
        inAuthentication = false
    }

    companion object {
        val DEFAULT_KEY_NAME = "default_key"
    }

}