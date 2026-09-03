package com.example.totanpay.data.repository.datasource.transaction

import com.example.totanpay.R

enum class TransactionType (val tag:Int,val title :Int){
    LOGON(1,-1),
    INIT(2,-2),
    BALANCE(3, R.string.balance_),
    PURCHASE(4,R.string.purchase),
    BILL_INQUERY(5,R.string.bill_inquery),
    SETTLEMENT_REVERSE(6,-3),
    VOUCHER(7,R.string.voucher),
    BILL_PAY(8,R.string.bill_payment),
    TOPUP(9,R.string.topup),
    GETKE(10,R.string.get_key),
    CONFIRMKEY(11,R.string.confirm_key),
    KEYEXCHANGEWORKINGKEY(12,R.string.key_exchange_key),
    PURCHASEWITHID(13,R.string.purchase_with_id),
    CHARGE(14, R.string.charge)
    ;
    companion object {
        fun titleOf(tag: Int): TransactionType {
            var temp: TransactionType? = null
            for (e in TransactionType.values()) {
                if (e.tag == tag) {
                    temp = e
                }
            }
            return temp?:TransactionType.PURCHASE
        }
    }
}