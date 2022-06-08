package uk.fernando.math.page

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import uk.fernando.math.R
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

    Column(
        Modifier
            .fillMaxSize()
            .background(green_pastel)
    ) {

        TopNavigationBar(title = stringResource(R.string.settings_title))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
                .background(Color.White, RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
        ) {

            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 33.dp)
            ) {
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
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .clickable {

                        },
                    text = R.string.privacy_policy,
                    isChecked = false,
                    onCheckedChange = {},
                    showArrow = true
                )

                Spacer(Modifier.weight(0.9f))

                Text(
                    text = "Version 1.0",
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

        }

    }
}

@Composable
private fun CustomSettingsResourcesCard(
    modifier: Modifier,
    color: Color = green_pastel,
    @StringRes text: Int,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    showArrow: Boolean = false
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        color = color.copy(0.1f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = stringResource(id = text),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(end = 28.dp)
                    .weight(1f)
            )

            if (showArrow)
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = null,
                    tint = color
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
