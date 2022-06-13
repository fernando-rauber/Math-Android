package uk.fernando.math.page

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.BuildConfig
import uk.fernando.math.R
import uk.fernando.math.component.MyBackground
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.ui.theme.green_pastel
import uk.fernando.math.ui.theme.greySuperLight
import uk.fernando.math.viewmodel.SettingsViewModel

@ExperimentalMaterialApi
@Composable
fun SettingsPage(viewModel: SettingsViewModel = getViewModel()) {

    val isDarkMode = viewModel.prefs.isDarkMode().collectAsState(initial = false)
    val allowDecimals = viewModel.prefs.allowDecimals().collectAsState(initial = false)
    val isPremium = viewModel.prefs.isPremium().collectAsState(initial = false)
    val notificationEnable = viewModel.prefs.notificationEnable().collectAsState(initial = true)

    MyBackground {

        Column(Modifier.fillMaxSize()) {

            TopNavigationBar(title = R.string.settings_title)

            Column(Modifier.padding(horizontal = 20.dp, vertical = 30.dp)) {
                CustomSettingsResourcesCard(
                    modifier = Modifier,
                    text = R.string.dark_mode,
                    isChecked = isDarkMode.value,
                    onCheckedChange = viewModel::updateDarkMode
                )

                CustomSettingsResourcesCard(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = R.string.allow_decimals,
                    isChecked = allowDecimals.value,
                    onCheckedChange = viewModel::updateAllowDecimals
                )

                CustomSettingsResourcesCard(
                    modifier = Modifier,
                    text = R.string.premium,
                    isChecked = isPremium.value,
                    onCheckedChange = viewModel::updatePremium
                )

                CustomSettingsResourcesCard(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = R.string.notification,
                    isChecked = notificationEnable.value,
                    onCheckedChange = viewModel::updateNotification
                )

                CustomSettingsResourcesCard(
                    modifier = Modifier.padding(bottom = 10.dp),
                    modifierRow = Modifier
                        .padding(vertical = 3.dp)
                        .clickable {

                        },
                    text = R.string.privacy_policy,
                    isChecked = false,
                    onCheckedChange = {},
                    showArrow = true
                )

                Spacer(Modifier.weight(0.9f))

                Text(
                    text = stringResource(id = R.string.version, BuildConfig.VERSION_NAME),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun CustomSettingsResourcesCard(
    modifier: Modifier = Modifier,
    modifierRow: Modifier = Modifier,
    @StringRes text: Int,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    showArrow: Boolean = false
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        shadowElevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifierRow.padding(16.dp)
        ) {

            Text(
                text = stringResource(id = text),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(end = 28.dp)
                    .weight(1f)
            )

            if (showArrow)
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = null,
                    tint = green_pastel
                )
            else
                Surface(
                    modifier = Modifier.height(30.dp),
                    color = if (isChecked) green_pastel else greySuperLight,
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Switch(
                        modifier = Modifier
                            .padding(6.dp)
                            .scale(1.2f),
                        checked = isChecked,
                        onCheckedChange = onCheckedChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = green_pastel,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = greySuperLight
                        )
                    )
                }
        }
    }
}
