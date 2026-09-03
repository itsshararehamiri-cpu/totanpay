package com.example.totanpay.data.repository.datasource.transaction

import com.example.totanpay.R

enum class ResponseMessageContainer(
    val code: String, val message1: String, val englishMessage: String, val messageId: Int
) {
    RC_3(
        "-3", "خطا در فرمت اطلاعات", "Error in data format",
        R.string.rc_3
    ),
    RC_6(
        "-6", "خطا در ارسال تراکنش", "error in sending transaction",
        R.string.rc_6
    ),
    RC_5(
        "-5", "خطا در برقراری ارتباط", "Connection error",
        R.string.rc_5
    ),

    RC_1(
        "-1", "خطا در دریافت اطلاعات", "Error receiving data",
        R.string.rc_1
    ),
    RC_2(
        "-2",
        "خطا در ارسال تراکنش تسویه-بازگشت",
        "Error sending settlement-refund transaction",
        R.string.rc_2
    ),

    RC_01(
        "01",
        "به صادر کننده کارت رجوع شود، تراکنش لغو شد",
        "Contact the card issuer, the transaction was canceled.",
        R.string.rc_01
    ),
    RC_02(
        "02",
        "با توجه به شرایط خاص بوجود آمده به صادر کننده کارت رجوع شود.",
        "Please contact the card issuer regarding specific circumstances.",
        R.string.rc_02
    ),
    RC_03(
        "03", "پذیرنده فروشگاهی معتبر نیست.", "Invalid merchant", R.string.rc_03
    ),
    RC_04(
        "04", "کارت نامعتبر است", "Invalid card", R.string.rc_04
    ),

    RC_05(
        "05", "مجوز صادر نشود", "No license will be issued.", R.string.rc_05
    ),
    RC_06(
        "06", "خطا", "Error", R.string.rc_06
    ),
    RC_07(
        "07", "کارت نامعتبر است", "Invalid card", R.string.rc_07
    ),
    RC_08(
        "08",
        "مجوز پس از شناسایی صادر شود، تراکنش انجام شد",
        "Authorization issued after identification,transaction completed.",
        R.string.rc_08
    ),

    RC_09(
        "09", "صادر کننده کارت نامعتبر است", "Invalid card issuer", R.string.rc_09
    ),
    RC_10(
        "10",
        "تراکنش با جزئی از مبلغ انجام شد، تراکنش انجام شد",
        "The transaction was completed with  a partial amount",
        R.string.rc_10
    ),
    RC_11(
        "11",
        "تراکنش جهت اشخاص خاص انجام شد",
        "The transaction was carried out for specific individuals",
        R.string.rc_11
    ),
    RC12(
        "12", "تراکنش معتبر نیست", "Invalid transaction", R.string.rc_12
    ),
    RC13(
        "13", "مبلغ معتبر نیست", "Invalid amount", R.string.rc_13
    ),
    RC14(
        "14", "شماره کارت معتبر نیست", "Invalid card number", R.string.rc_14
    ),
    RC15(
        "15", "صادر کننده کارت نامعتبر است", "Invalid issuer", R.string.rc_15
    ),
    RC16(
        "16",
        "تراکنش پس از بروزرسانی کارت انجام شد، تراکنش انجام شد",
        "The transaction was completed after the card  update.",
        R.string.rc_16
    ),

    RC17(
        "17", "انصراف مشتری", "Customer cancellation", R.string.rc_17
    ),
    RC18(
        "18", "اعتراض مشتری", "Customer dispute", R.string.rc_18
    ),
    RC19(
        "19", "تراکنش تکراری،تراکنش لغو شد", "Duplicate transaction, cancelled", R.string.rc_19
    ),
    RC20("20", "پاسخ معتبر نیست", "Invalid response", R.string.rc_20), RC21(
        "21", "عملیات خاصی انجام نشد", "No specific operation was performed.", R.string.rc_21
    ),
    RC22(
        "22", "احتمال خرابی", "Possibility of failure", R.string.rc_22
    ),
    RC23(
        "23",
        "کارمزد تراکنش غیرقابل اعمال است، تراکنش لغو شد",
        "Transaction fee not applicable, cancelled",
        R.string.rc_23
    ),
    RC24(
        "24",
        "دریافت کننده امکان پشتیبانی از عملیات فایلی را ندارد",
        "File operation are not supported by receiver",
        R.string.rc_24
    ),
    RC25(
        "25", "تراکنش اصلی یافت نشد", "Original transaction not found", R.string.rc_25
    ),
    RC26(
        "26",
        "عملیات فایلی مربوط به رکورد تکراری است. رکورد قبلی جایگزین می شود",
        "Duplicate record, previous replaced",
        R.string.rc_26
    ),
    RC27(
        "27",
        "عملیات فایلی تصحیح فیلد با خطا مواجه شد",
        "File field correction error",
        R.string.rc_27
    ),
    RC28(
        "28", "انجام عملیات فایلی روی یک فایل قفل شده است", "File locked", R.string.rc_28
    ),
    RC29(
        "29",
        "عملیات فایلی موفقیت آمیز نبود، با پذیرنده تماس بگیرید",
        "File action failed, contact acquirer",
        R.string.rc_29
    ),
    RC30(
        "30", "اشکال در فرمت اطلاعات", "Format error", R.string.rc_30
    ),
    RC31(
        "31",
        "بانک توسط سوییچ پشتیبانی نمی شود، تراکنش لغو شد",
        "Bank not supported by switch",
        R.string.rc_31
    ),
    RC32(
        "32", "تراکنش به صورت جزئی انجام شد", "Partial transaction approved", R.string.rc_32
    ),
    RC33("33", "کارت منقضی شده", "Expired card", R.string.rc_33),
    RC34(
        "34", "", "Suspected fraud, card captured", R.string.rc_34
    ),
    RC35(
        "35",
        "دریافت کننده کارت نمی تواند یک پذیرنده باشد،ضبط کارت",
        "Acquirer cannot be cardholder, card captured",
        R.string.rc_35
    ),
    RC36("36", "کارت نامعتبر", "Invalid card", R.string.rc_36), RC37(
        "37", "کارت نامعتبر", "Invalid card", R.string.rc_37
    ),
    R38(
        "38",
        "تعداد دفعات مجاز تکرار رمز مشتری به پایان رسید",
        "PIN retry limit exceeded",
        R.string.rc_38
    ),
    RC39(
        "39",
        "حساب اعتباری وجود ندارد،تراکنش لغو شد",
        "No credit account, transaction cancelled",
        R.string.rc_39
    ),
    RC40(
        "40",
        "عملیات درخواستی پشتیبانی نمی شود،تراکنش لغو شد",
        "Requested function not supported",
        R.string.rc_40
    ),
    RC41("41", "کارت نامعتبر", "Lost card", R.string.rc_41), RC42(
        "42",
        "حساب عمومی وجود ندارد،تراکنش لغو شد",
        "No universal account, transaction cancelled",
        R.string.rc_42
    ),
    RC43("43", "", "", R.string.rc_43), RC44(
        "44",
        "حساب سرمایه گذاری وجود ندارد،تراکنش لغو شد",
        "Investment account not found",
        R.string.rc_44
    ),
    RC51(
        "51", "موجوی حساب کافی نیست،تراکنش لغو شد", "Insufficient funds", R.string.rc_51
    ),
    RC52(
        "52",
        "حساب جاری وجود ندارد،تراکنش لغو شد",
        "Current account not available. Transaction cancelled.",
        R.string.rc_52
    ),
    RC53(
        "53",
        "حساب پس انداز موجود نیست،تراکنش لغو شد",
        "Savings account not available. transaction cancelled",
        R.string.rc_53
    ),
    RC54("54", "کارت منقضی شده است، تراکنش لغو شد", "Expired card", R.string.rc_54), RC55(
        "55", "رمز اشتباه است،تراکنش لغو شد", "Incorrect PIN", R.string.rc_55
    ),
    RC56("56", "کارت نامعتبر", "Invalid card", R.string.rc_56), RC57(
        "57",
        "دارنده کارت مجاز به انجام تراکنش نیست،تراکنش لغو شد",
        "Transaction not permitted to cardholder",
        R.string.rc_57
    ),
    RC58(
        "58",
        "پایانه مجاز به انجام تراکنش نیست،تراکنش لغو شد",
        "Transaction not permitted to terminal",
        R.string.rc_58
    ),
    RC59(
        "59",
        "احتمال سو استفاده،تراکنش لغو شد",
        "Potential fraud detected. Transaction cancelled.",
        R.string.rc_59
    ),
    RC60(
        "60",
        "دریافت کننده کارت نمیتواند یک پذیرنده باشد، تراکنش لغو شد",
        "The card recipient cannot be merchant. Transaction cancelled.",
        R.string.rc_60
    ),
    RC61(
        "61", "مبلغ نامعتبر", "Invalid amount", R.string.rc_61
    ),
    RC62(
        "62", "کارت نامعتبر", "Invalid card", R.string.rc_62
    ),
    RC63(
        "63", "نقض موارد امنیتی", "Security violation", R.string.rc_63
    ),
    RC64(
        "64",
        "مبلغ تراکنش اصلی اشتباه است،تراکنش لغو شد",
        "Original amount incorrect",
        R.string.rc_64
    ),
    RC65(
        "65",
        "مبلغ از سقف دوره ای برداشت نقدی بیشتر است،تراکنش لغو شد",
        "The amount exceeds the periodic cash withdrawal limit. Transaction cancelled.",
        R.string.rc_65
    ),
    RC66(
        "66",
        "تماس دریافت کننده کارت با تشکیلات امنیتی پذیرنده، تراکنش لغو شد",
        "Contact acquirer’s security department",
        R.string.rc_66
    ),
    RC67("67", "ضبط کارت", "Capture card", R.string.rc_67), RC68(
        "68",
        "پاسخ در فرصت مشخص شده دریافت نشد",
        "No response received within the specified time.",
        R.string.rc_68
    ),
    RC75(
        "75",
        "تعداد دفعات مجاز تکرار رمز مشتری به پایان رسید،تراکنش لغو شد",
        "PIN retry limit exceeded",
        R.string.rc_75
    ),
    RC77(
        "77", "تاریخ تراکنش معتبر نیست", "Invalid transaction date", R.string.rc_77
    ),
    RC90(
        "90",
        "عملیات تعویض دوره مالی در حال انجام است",
        "Financial period swtching is in progress.",
        R.string.rc_90
    ),
    RC91(
        "91",
        "صادر کننده یا سوئیچ در حال انجام عملیات هستند-تراکنش لغو شد",
        "Issuer or switch is currently processing.Transaction cancelled",
        R.string.rc_91
    ),
    RC92(
        "92",
        "موسسه مالی یا شبکه ارتباط جهت مسیریابی در دسترس نیست،تراکنش لغو شد",
        "Financial institution or network unavailable",
        R.string.rc_92
    ),
    RC93(
        "93", "امکان تکمیل تراکنش وجود ندارد", "Transaction cannot be completed",
        R.string.rc_93
    ),
    RC94("94", "تراکنش تکراری است", "Duplicate transaction",
        R.string.rc_94),
    RC95(
        "95", "تسویه حساب با خطا مواجه شد", "Settlement encountered an error",
        R.string.rc_95
    ),
    RC96("96", "اشکال در عملکرد سیستم", "System malfunction",
        R.string.rc_96),
    RCUNKNOWN(
        "", "خطا", "Unknown error", R.string.rc_empty
    );

    companion object {
        fun valueOfLabel(code: String): ResponseMessageContainer {
            var temp: ResponseMessageContainer? = null
            for (e in ResponseMessageContainer.values()) {
                if (e.code == code) {
                    temp = e
                }
            }
            return temp ?: RCUNKNOWN
        }
    }
}