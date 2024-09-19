package com.example.totanpay.data.repository.util

fun getBillType(billid: String): BillType {
    val org:CharSequence = billid.subSequence(billid.length - 2, billid.length - 1)
    return BillType.valueOf(org.toString())
}

enum class BillType(val billId: String,val title:String) {
    AB("1","آب"), BARGH("2","برق"), QAZ("3","گاز"),
    MOBILE("4","موبایل"), MOBILE2("5",""), NO("-1","");

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