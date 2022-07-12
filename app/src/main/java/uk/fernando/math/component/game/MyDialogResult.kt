package uk.fernando.math.component.game

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import uk.fernando.advertising.AdInterstitial
import uk.fernando.math.R
import uk.fernando.math.component.MyAnimation
import uk.fernando.math.ext.playAudio
import uk.fernando.math.viewmodel.BaseGameViewModel

@Composable
fun MyDialogResult(
    isPremium: Boolean,
    disableSound: Boolean,
    viewModel: BaseGameViewModel,
    fullScreenAd: AdInterstitial,
    onFinish: () -> Unit
) {
    val soundFinish = MediaPlayer.create(LocalContext.current, R.raw.sound_finish)

    MyAnimation(viewModel.isGameFinished.value) {
        LaunchedEffect(Unit) { soundFinish.playAudio(disableSound) }

        if (!isPremium)
            fullScreenAd.showAdvert()

        MyGameDialog(
            image = R.drawable.ic_fireworks,
            message = R.string.result_message,
            buttonText = R.string.result_action,
            onClick = onFinish
        )
    }
}