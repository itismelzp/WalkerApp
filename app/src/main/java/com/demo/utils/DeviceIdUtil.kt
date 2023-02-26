package com.demo.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import java.security.MessageDigest
import java.util.*


/**
 * 获取手机的唯一标识ID
 */
object DeviceIdUtil {

    fun getDeviceId(context: Context): String? {
        val sbDeviceId = StringBuilder()
        val imei = getIMEI(context)
        val androidId = getAndroidId(context)
        val serial = serial
        val uuid = deviceUUID

        //附加imei
        if (imei.isNotEmpty()) {
            sbDeviceId.append(imei)
            sbDeviceId.append("|")
        }
        //附加androidId
        if (androidId.isNotEmpty()) {
            sbDeviceId.append(androidId)
            sbDeviceId.append("|")
        }
        //附加serial
        if (serial != null && serial.isNotEmpty()) {
            sbDeviceId.append(serial)
            sbDeviceId.append("|")
        }
        //附加uuid
        if (uuid.isNotEmpty()) {
            sbDeviceId.append(uuid)
        }
        if (sbDeviceId.isNotEmpty()) {
            try {
                val hash = getHashByString(sbDeviceId.toString())
                val sha1 = bytesToHex(hash)
                if (sha1.isNotEmpty()) {
                    //返回最终的DeviceId
                    return sha1
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 转16进制字符串
     *
     * @param data 数据
     * @return 16进制字符串
     */
    private fun bytesToHex(data: ByteArray): String {
        val sb = StringBuilder()
        var string: String
        for (i in data.indices) {
            string = Integer.toHexString(data[i].toInt() and 0xFF)
            if (string.length == 1) {
                sb.append("0")
            }
            sb.append(string)
        }
        return sb.toString().uppercase(Locale.CHINA)
    }

    /**
     * 取 SHA1
     *
     * @param data 数据
     * @return 对应的Hash值
     */
    private fun getHashByString(data: String): ByteArray {
        return try {
            val messageDigest = MessageDigest.getInstance("SHA1")
            messageDigest.reset()
            messageDigest.update(data.toByteArray(charset("UTF-8")))
            messageDigest.digest()
        } catch (e: Exception) {
            "".toByteArray()
        }
    }

    /**
     * 获取硬件的UUID
     *
     * @return
     */
    private val deviceUUID: String
        get() {
            val deviceId = "9527" + Build.ID +
                    Build.DEVICE +
                    Build.BOARD +
                    Build.BRAND +
                    Build.HARDWARE +
                    Build.PRODUCT +
                    Build.MODEL +
                    Build.SERIAL
            return UUID(deviceId.hashCode().toLong(), Build.SERIAL.hashCode().toLong()).toString()
                .replace("-", "")
        }
    private val serial: String?
        get() {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return Build.getSerial()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

    /**
     * 获取AndroidId
     *
     * @param context 上下文
     * @return AndroidId
     */
    private fun getAndroidId(context: Context): String {
        try {
            return Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取IMEI
     *
     * @param context 上下文
     * @return IMEI
     */
    private fun getIMEI(context: Context): String {
        try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return telephonyManager.deviceId
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}