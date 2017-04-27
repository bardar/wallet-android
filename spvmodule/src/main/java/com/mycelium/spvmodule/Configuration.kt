package com.mycelium.spvmodule

import android.content.SharedPreferences
import com.google.common.base.Strings

class Configuration(private val prefs: SharedPreferences) {
    val connectivityNotificationEnabled: Boolean
        get() = prefs.getBoolean(PREFS_KEY_CONNECTIVITY_NOTIFICATION, true)

    val trustedPeerHost: String?
        get() {
            if (!prefs.contains(PREFS_KEY_TRUSTED_PEER)) {
                val nodeList = if (BuildConfig.APPLICATION_ID.contains(".test")) TRUSTED_FULL_NODES_TEST else TRUSTED_FULL_NODES_MAIN
                prefs.edit().putString(PREFS_KEY_TRUSTED_PEER, nodeList[(Math.random() * nodeList.size).toInt()]).apply()
            }
            return Strings.emptyToNull(prefs.getString(PREFS_KEY_TRUSTED_PEER, "")!!.trim { it <= ' ' })
        }

    val trustedPeerOnly: Boolean
        get() = prefs.getBoolean(PREFS_KEY_TRUSTED_PEER_ONLY, true)

    val lastUsedAgo: Long
        get() = System.currentTimeMillis() - prefs.getLong(PREFS_KEY_LAST_USED, 0)

    val bestChainHeightEver: Int
        get() = prefs.getInt(PREFS_KEY_BEST_CHAIN_HEIGHT_EVER, 0)

    fun maybeIncrementBestChainHeightEver(bestChainHeightEver: Int) {
        if (bestChainHeightEver > bestChainHeightEver) {
            prefs.edit().putInt(PREFS_KEY_BEST_CHAIN_HEIGHT_EVER, bestChainHeightEver).apply()
        }
    }

    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        private val TRUSTED_FULL_NODES_MAIN = arrayOf(
                "mws2.mycelium.com",
                "mws6.mycelium.com",
                "mws7.mycelium.com",
                "mws8.mycelium.com")
        private val TRUSTED_FULL_NODES_TEST = arrayOf(
                "node3.mycelium.com")
        val PREFS_KEY_CONNECTIVITY_NOTIFICATION = "connectivity_notification"
        val PREFS_KEY_TRUSTED_PEER = "trusted_peer"
        val PREFS_KEY_TRUSTED_PEER_ONLY = "trusted_peer_only"
        val PREFS_KEY_DATA_USAGE = "data_usage"

        private val PREFS_KEY_LAST_USED = "last_used"
        private val PREFS_KEY_BEST_CHAIN_HEIGHT_EVER = "best_chain_height_ever"
    }
}