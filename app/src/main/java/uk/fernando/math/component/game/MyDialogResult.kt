package uk.fernando.math.component.game

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import org.koin.androidx.compose.inject
import uk.fernando.advertising.AdInterstitial
import uk.fernando.math.R
import uk.fernando.math.component.MyAnimation
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.ext.playAudio
import uk.fernando.math.viewmodel.BaseGameViewModel

@Composable
fun MyDialogResult(
    viewModel: BaseGameViewModel,
    fullScreenAd: AdInterstitial,
    onFinish: () -> Unit
) {
    val prefs: PrefsStore by inject()
    val isSoundEnable = prefs.soundEnable().collectAsState(initial = false)
    val isPremium = prefs.isPremium().collectAsState(initial = false)
    val soundFinish = MediaPlayer.create(LocalContext.current, R.raw.sound_finish)

    MyAnimation(viewModel.isGameFinished.value) {
        LaunchedEffect(Unit) { soundFinish.playAudio(isSoundEnable.value) }

        if (!isPremium.value)
            fullScreenAd.showAdvert()

        MyGameDialog(
            image = R.drawable.ic_fireworks,
            message = R.string.result_message,
            buttonText = R.string.result_action,
            onClick = onFinish
        )
    }
}