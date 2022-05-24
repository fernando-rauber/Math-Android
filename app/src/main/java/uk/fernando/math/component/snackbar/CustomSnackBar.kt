package uk.fernando.math.component.snackbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CustomSnackBar(snackBarSealed: SnackBarSealed?, content: @Composable () -> Unit) {
    val lifecycleScope = rememberCoroutineScope()
    val snackBarController = SnackBarController(lifecycleScope)
    val scaffoldState = remember { SnackbarHostState() }

    when (snackBarSealed) {
        is SnackBarSealed.Success -> snackBarController.showSnackBar(scaffoldState = scaffoldState, longDuration = snackBarSealed.isLongDuration)
        is SnackBarSealed.Error -> snackBarController.showSnackBar(scaffoldState = scaffoldState, longDuration = snackBarSealed.isLongDuration)
    }

//    LargeTopAppBar
//    MediumTopAppBar
//    SmallTopAppBar
//    CenterAlignedTopAppBar() {
//
//    }
//    IconToggleButton(checked = , onCheckedChange = ) {
//
//    }

    Box(modifier = Modifier.fillMaxSize()) {

        content()

        DefaultSnackBar(
            snackBarHostState = scaffoldState,
            snackBarSealed = snackBarSealed,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}