package com.example.totanpay.data.repository.util

import com.example.totanpay.R

fun getBillType(billid: String): BillType {
    val org:CharSequence = billid.subSequence(billid.length - 2, billid.length - 1)
    return BillType.valueOf(org.toString())
}

enum class BillType(val billId: String,val titleId:Int) {
    AB("1", R.string.water), BARGH("2",R.string.electricity),
    QAZ("3",R.string.gas),
    MOBILE("4",R.string.mobile), MOBILE2("5",R.string.empty_message), NO("-1",R.string.empty_message);

    companion object {
        fun valueOf(billId: String): BillType {
            var temp: BillType? = null
            for (e in BillType.values()) {
                if (e.billId == billId) {
                    temp = e
                }
            }
            return temp ?: NO
        }
    }
}