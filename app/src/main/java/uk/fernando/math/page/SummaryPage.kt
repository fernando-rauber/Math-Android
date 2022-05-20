package uk.fernando.math.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.component.snackbar.CustomSnackBar
import uk.fernando.math.viewmodel.SettingsViewModel

@ExperimentalMaterialApi
@Composable
fun SummaryPage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: SettingsViewModel = getViewModel()) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {

        Text(text = "ddddddddddddddd")

    }
}