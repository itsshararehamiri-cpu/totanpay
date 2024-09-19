package com.example.totanpay.data.repository.datasource.transaction

enum class TransactionType (val tag:Int,val title:String){
    LOGON(1,""),
    INIT(2,""),
    BALANCE(3,"موجودی"),
    PURCHASE(4,"خرید"),
    BILL_INQUERY(5,"استعلام قبض"),
    SETTLEMENT_REVERSE(6,""),
    VOUCHER(7,"خرید کد شارژ"),
    BILL_PAY(8,"پرداخت قبض"),
    TOPUP(9,"شارژ مستقیم"),
    GETKE(10,"گرفتن کلید"),
    CONFIRMKEY(11,"خرید مستقیم شارژ"),
    KEYEXCHANGEWORKINGKEY(12,"خرید مستقیم شارژ"),
    PURCHASEWITHID(13,"خرید شناسه دار"),
    CHARGE(14,"شارژ")
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