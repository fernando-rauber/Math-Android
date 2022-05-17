package uk.fernando.math.viewmodel

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.MutableStateFlow


class CreateGameViewModel() : BaseViewModel() {

    val isBluetoothOn = MutableStateFlow(false)
    val isScanning = MutableStateFlow(false)
    val devicesNotFound = MutableStateFlow(false)

    val myDevices = MutableStateFlow<List<BluetoothDevice>>(listOf())
    val otherDevices = MutableStateFlow<List<BluetoothDevice>>(listOf())

    val navChat = MutableStateFlow("")


}



