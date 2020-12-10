package com.example.relatome.utils

sealed class LoadingStatus {
    class Loading : LoadingStatus()
    class Noop : LoadingStatus()
    class Error(msg: String) : LoadingStatus()
    class Timeout: LoadingStatus()
    class NoConnection: LoadingStatus()
}