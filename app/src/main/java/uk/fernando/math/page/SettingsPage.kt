package uk.fernando.math.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.component.snackbar.CustomSnackBar
import uk.fernando.math.viewmodel.SettingsViewModel

@ExperimentalMaterialApi
@Composable
fun SettingsPage(viewModel: SettingsViewModel = getViewModel()) {


        CustomSnackBar(snackBarSealed = viewModel.snackBar.value) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {

        }
    }
}