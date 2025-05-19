package com.example.cocktailapp.api

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SmsManager
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun sendSms(context: Context, phoneNumber: String, message: String) {
    val smsManager = context.getSystemService(SmsManager::class.java)

    smsManager.sendTextMessage(phoneNumber, null, message, null, null)
}