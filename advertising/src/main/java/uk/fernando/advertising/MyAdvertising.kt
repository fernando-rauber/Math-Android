package uk.fernando.advertising

import android.content.Context
import com.google.android.gms.ads.MobileAds

object MyAdvertising {

//    private val configs = RequestConfiguration.Builder().setTestDeviceIds(listOf("1B8A325EEFF8BEF2134994B7A47F8F19"))
//        .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
//        .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
//        .build()


    fun initialize(context: Context) {
//        MobileAds.setRequestConfiguration(configs) //DELETE
        MobileAds.initialize(context) { }
    }
}