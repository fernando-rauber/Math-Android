package uk.fernando.math.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.inject
import uk.fernando.advertising.component.AdBanner
import uk.fernando.math.datastore.PrefsStore

@Composable
fun MyAdBanner(modifier: Modifier, @StringRes unitId: Int) {
    val dataStore: PrefsStore by inject()

    val isPremium = dataStore.isPremium().collectAsState(true)

    if (!isPremium.value)
        AdBanner(
            unitId = unitId,
            modifier = modifier.padding(bottom = 8.dp)
        )
}