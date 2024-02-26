package com.example.devicecontrols.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import android.webkit.URLUtil
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.URLEncoder
import java.util.Collections
import java.util.Locale


fun openDefaultBrowser(activity: Activity, url: String) {
    try {
        val uri: Uri = if (URLUtil.isValidUrl(url)) {
            Uri.parse(url)
        } else {
            val encodedSearchText = URLEncoder.encode(url, "UTF-8")
            val newUrl = "https://www.google.com/search?q=$encodedSearchText"
            Uri.parse(newUrl)
        }
        val intent = Intent(Intent.ACTION_VIEW, uri)
        activity.startActivity(intent)
    } catch (e: Exception) {
        //other exception
        Log.e("Open Default Browser With", e.message!!)
    }
}

fun getMACAddress(interfaceName: String?): String {
    try {
        val interfaces: List<NetworkInterface> =
            Collections.list(NetworkInterface.getNetworkInterfaces())
        for (intf in interfaces) {
            if (interfaceName != null) {
                if (!intf.name.equals(interfaceName, ignoreCase = true)) continue
            }
            val mac = intf.hardwareAddress ?: return ""
            val buf = StringBuilder()
            for (aMac in mac) buf.append(String.format("%02X:", aMac))
            if (buf.isNotEmpty()) buf.deleteCharAt(buf.length - 1)
            return buf.toString()
        }
    } catch (ignored: Exception) {
    } // for now eat exceptions
    return ""
}

fun requestAddress(useIPv4: Boolean): String? {
    try {
        val interfaces: List<NetworkInterface> =
            Collections.list(NetworkInterface.getNetworkInterfaces())
        for (intf in interfaces) {
            val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
            for (addr in addrs) {
                if (!addr.isLoopbackAddress) {
                    val sAddr = addr.hostAddress
                    //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                    val isIPv4 = sAddr.indexOf(':') < 0
                    if (useIPv4) {
                        if (isIPv4) return sAddr
                    } else {
                        if (!isIPv4) {
                            val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                            return if (delim < 0) sAddr.uppercase(Locale.getDefault()) else sAddr.substring(
                                0,
                                delim
                            ).uppercase(
                                Locale.getDefault()
                            )
                        }
                    }
                }
            }
        }
    } catch (ignored: Exception) {
        Log.e("Get IP Address", ignored.message!!)
    } // for now eat exceptions
    return ""
}

@SuppressLint("HardwareIds")
fun requestIMEI(activity: Activity, context: Context, telephonyManager: TelephonyManager): String {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_PHONE_STATE),
            REQUEST_PERMISSION_CODE
        )
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Nếu API level >= 26 (Android 8.0), sử dụng getImei()
        telephonyManager.imei
    } else {
        // Nếu API level < 26, sử dụng getDeviceId()
        telephonyManager.deviceId
    }
}


fun getAppNameFromPackageName(applicationContext: Context, packageName: String): String {
    val packageManager = applicationContext.packageManager
    return try {
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        packageManager.getApplicationLabel(applicationInfo).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        packageName
    }
}

fun isBanking(name: String): Boolean {
    return backingPackageName().contains(name)
}

fun backingPackageName(): List<String> {
    return mutableListOf(
        "com.mbmobile",//MB
        "com.tpb.mb.gprsandroid", //
        "com.vnpay.bidv",
        "com.VCB",
        "com.Agribank3g",
        "com.vietinbank.ipay",
        "vn.com.techcombank.bb.app",
    )
}

fun containsNumberAndVnd(text: String): Boolean {
    return Regex(
        "\\d{1,3}(?:,\\d{3})*(?:\\.\\d+)?\\s*vnd",
        RegexOption.IGNORE_CASE
    ).containsMatchIn(changeVietnameseWithoutAccents(text.lowercase()))
}

fun changeVietnameseWithoutAccents(str: String): String {
    var originalText = str
    var modifiedText = ""

    for (character in originalText) {
        modifiedText += diacriticMap[character.toString()] ?: character.toString()
    }

    return modifiedText
}

val diacriticMap = mapOf(
    "á" to "a",
    "à" to "a",
    "ả" to "a",
    "ã" to "a",
    "ạ" to "a",
    "ă" to "a",
    "ắ" to "a",
    "ằ" to "a",
    "ẳ" to "a",
    "ẵ" to "a",
    "ặ" to "a",
    "â" to "a",
    "ấ" to "a",
    "ầ" to "a",
    "ẩ" to "a",
    "ẫ" to "a",
    "ậ" to "a",
    "é" to "e",
    "è" to "e",
    "ẻ" to "e",
    "ẽ" to "e",
    "ẹ" to "e",
    "ê" to "e",
    "ế" to "e",
    "ề" to "e",
    "ể" to "e",
    "ễ" to "e",
    "ệ" to "e",
    "í" to "i",
    "ì" to "i",
    "ỉ" to "i",
    "ĩ" to "i",
    "ị" to "i",
    "ó" to "o",
    "ò" to "o",
    "ỏ" to "o",
    "õ" to "o",
    "ọ" to "o",
    "ô" to "o",
    "ố" to "o",
    "ồ" to "o",
    "ổ" to "o",
    "ỗ" to "o",
    "ộ" to "o",
    "ơ" to "o",
    "ớ" to "o",
    "ờ" to "o",
    "ở" to "o",
    "ỡ" to "o",
    "ợ" to "o",
    "ú" to "u",
    "ù" to "u",
    "ủ" to "u",
    "ũ" to "u",
    "ụ" to "u",
    "ư" to "u",
    "ứ" to "u",
    "ừ" to "u",
    "ử" to "u",
    "ữ" to "u",
    "ự" to "u",
    "ý" to "y",
    "ỳ" to "y",
    "ỷ" to "y",
    "ỹ" to "y",
    "ỵ" to "y",
    "đ" to "d",
    "Á" to "A",
    "À" to "A",
    "Ả" to "A",
    "Ã" to "A",
    "Ạ" to "A",
    "Ă" to "A",
    "Ắ" to "A",
    "Ằ" to "A",
    "Ẳ" to "A",
    "Ẵ" to "A",
    "Ặ" to "A",
    "Â" to "A",
    "Ấ" to "A",
    "Ầ" to "A",
    "Ẩ" to "A",
    "Ẫ" to "A",
    "Ậ" to "A",
    "É" to "E",
    "È" to "E",
    "Ẻ" to "E",
    "Ẽ" to "E",
    "Ẹ" to "E",
    "Ê" to "E",
    "Ế" to "E",
    "Ề" to "E",
    "Ể" to "E",
    "Ễ" to "E",
    "Ệ" to "E",
    "Í" to "I",
    "Ì" to "I",
    "Ỉ" to "I",
    "Ĩ" to "I",
    "Ị" to "I",
    "Ó" to "O",
    "Ò" to "O",
    "Ỏ" to "O",
    "Õ" to "O",
    "Ọ" to "O",
    "Ô" to "O",
    "Ố" to "O",
    "Ồ" to "O",
    "Ổ" to "O",
    "Ỗ" to "O",
    "Ộ" to "O",
    "Ơ" to "O",
    "Ớ" to "O",
    "Ờ" to "O",
    "Ở" to "O",
    "Ỡ" to "O",
    "Ợ" to "O",
    "Ú" to "U",
    "Ù" to "U",
    "Ủ" to "U",
    "Ũ" to "U",
    "Ụ" to "U",
    "Ư" to "U",
    "Ứ" to "U",
    "Ừ" to "U",
    "Ử" to "U",
    "Ữ" to "U",
    "Ự" to "U",
    "Ý" to "Y",
    "Ỳ" to "Y",
    "Ỷ" to "Y",
    "Ỹ" to "Y",
    "Ỵ" to "Y",
    "Đ" to "D"
)