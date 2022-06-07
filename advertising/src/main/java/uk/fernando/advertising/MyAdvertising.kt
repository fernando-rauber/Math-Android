package uk.fernando.advertising

import android.content.Context
import com.google.android.gms.ads.MobileAds

object MyAdvertising {

//        val configs = RequestConfiguration.Builder().setTestDeviceIds(listOf("1B8A325EEFF8BEF2134994B7A47F8F19")).build()
//        MobileAds.setRequestConfiguration(configs)

    fun initialize(context: Context) {
        MobileAds.initialize(context) {}
    }
}