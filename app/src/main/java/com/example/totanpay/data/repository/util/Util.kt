package com.example.totanpay.data.repository.util

 fun extractPanFromTrack2(track2: String): String {
    val string: List<String> = track2.split("=")
    return string[0]
}