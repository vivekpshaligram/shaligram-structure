package com.codestracture.data.manager.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceManagerImpl @Inject constructor(
    @ApplicationContext context: Context
) : PreferenceManager {

    private val masterKeyAlias: String by lazy { MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC) }

    private val sharedPreferences: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            "encrypted_preferences", // fileName
            masterKeyAlias, // masterKeyAlias
            context, // context
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // prefKeyEncryptionScheme
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // prefvalueEncryptionScheme
        )
    }

    val editor: SharedPreferences.Editor = sharedPreferences.edit()
}