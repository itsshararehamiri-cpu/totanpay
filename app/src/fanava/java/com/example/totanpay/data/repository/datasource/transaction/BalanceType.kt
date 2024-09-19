package com.example.totanpay.data.repository.datasource.transaction

enum class BalanceType(val label: String) {
    REAL_BALANCE("01"), AVAILABLE_BALANCE("02");

    companion object {
        fun valueOfLabel(label: String): BalanceType {
            var temp: BalanceType? = null
            for (e in entries) {
                if (e.label == label) {
                    temp = e
                }
            }
            return temp?: REAL_BALANCE
        }
    }
}