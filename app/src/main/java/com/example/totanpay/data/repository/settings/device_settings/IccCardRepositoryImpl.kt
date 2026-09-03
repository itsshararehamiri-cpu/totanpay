package com.example.totanpay.data.repository.settings.device_settings

import android.util.Log
import com.example.totanpay.data.repository.device.IDevice
import com.example.totanpay.data.util.ApduUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jpos.iso.ISOException
import org.jpos.iso.ISOUtil
import java.math.BigInteger
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.RSAPrivateKeySpec
import java.security.spec.RSAPublicKeySpec
import javax.inject.Inject

class IccCardRepositoryImpl @Inject constructor(
    private val device: IDevice,
    private val ioDispatcher: CoroutineDispatcher
) : IccCardRepository {
    val tag = "IccCardRepositoryImpl"
    override suspend fun detectCard(): Boolean {
        return withContext(ioDispatcher) {
            var detected: Boolean
            var timeOut = 6000
            while (true) {
                device.powerOnIcCard()
                detected = device.isIcCardDetect()
                if (detected)
                    break
                delay(500)
                timeOut -= 100
                if (timeOut == 0) {
                    break
                }
            }
            detected
        }
    }

    private suspend fun selectFirstApplet(): String? {
        return withContext(ioDispatcher) {
            try {
                Log.d("TAG", "selecftFirstApplet: hjjhjhj${ApduUtil.getSelectFirstAppletCommand()}")

                val responseBuffer: ByteArray? =
                    device.sendApdu(ISOUtil.hex2byte(ApduUtil.getSelectFirstAppletCommand()))
                Log.d("TAG", "selecftFirstApplet: hjjhjhj${ISOUtil.hexString(responseBuffer)}")

                return@withContext if (responseBuffer == null) {
                    null
                } else if (!ApduUtil.apduResponseIsSuccess(responseBuffer)) {
                    Log.d("TAG", "selecftFirstAppledt: hjjhjhj${ApduUtil.apduResponseIsSuccess(responseBuffer)}")

                    null
                } else {
                    ISOUtil.hexString(responseBuffer)
                }
            } catch (e: Exception) {
                Log.d(tag, "selectFirstApplet exception cause->${e.cause}")
                Log.d(tag, "selectFirstApplet exception message->${e.message}")
                e.printStackTrace()
                return@withContext null
            }
            return@withContext null
        }
    }

    private suspend fun selectSecondApplet(): String? {
        try {
            val responseBuffer: ByteArray? =
                device.sendApdu(ISOUtil.hex2byte(ApduUtil.getSelectSecondAppletCommand()))
            return if (responseBuffer == null) {
                null
            } else if (!ApduUtil.apduResponseIsSuccess(responseBuffer)) {
                null
            } else {
                ISOUtil.hexString(responseBuffer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(tag, "exception cause->${e.cause}")
            Log.d(tag, "exception message->${e.message}")
        }
        return null
    }

    private suspend fun verifyFirstPin(pin: String): String? {
        try {
            val responseBuffer: ByteArray =
                device.sendApdu(ISOUtil.hex2byte(ApduUtil.getVerifyFirstPinCommand(pin))) ?: return null
            return if (!ApduUtil.apduResponseIsSuccess(responseBuffer))
                null
            else ISOUtil.hexString(responseBuffer)
        } catch (e: ISOException) {
            Log.d(tag, "verifyFirstPin cause: " + e.cause)
            Log.d(tag, "verifyFirstPin message: " + e.message)
            e.printStackTrace()
            return null
        }
    }

    override suspend fun verifySecondPinAndInjectKeys(pin: String, privateKey: PrivateKey?): Boolean {
        return withContext(ioDispatcher) {
            try {
                device.powerOffIcCard()
                device.powerOnIcCard()
                val selectSecondAppletResult = selectSecondApplet()
                if (selectSecondAppletResult != null) {
                    val verifySecondPinResult: String? = verifySecondPin(pin)
                    val masterKey: String?
                    if (verifySecondPinResult == null) {
                        return@withContext false
                    } else {
                        masterKey = readMasterKey()
                        if (masterKey == null) return@withContext false
                        val factory = readFactoryKey(pin) ?: return@withContext false
                        val dM = ApduUtil.decrypt(privateKey, ISOUtil.hex2byte(masterKey))
                        val dF = ApduUtil.decrypt(privateKey, ISOUtil.hex2byte(factory))
                        val keyInjectResult = device.writeMasterKey(dF)
                        val m1 = device.decrypt(dM)
                        device.writeMasterKey(m1!!)//
                        device.powerOffIcCard()
                        return@withContext true
                    }
                } else {
                    return@withContext false
                }
            } catch (e: Exception) {
                Log.e(tag, "verifySecondPinAndInjectKeys: ${e.cause}")
                e.printStackTrace()
                return@withContext false
            }
            return@withContext false
        }
    }

    private suspend fun readFactoryKey(pin: String): String? {
        try {
            val responseBuffer: ByteArray? =
                device.sendApdu(ISOUtil.hex2byte(ApduUtil.getReadFactoryKeyCommand(pin)))
            if (responseBuffer == null) return null
            else if (!ApduUtil.apduResponseIsSuccess(responseBuffer))
                return null
            val retLen = responseBuffer!!.size
            val factoryKey = ISOUtil.hexString(responseBuffer, 0, retLen - 2)
            return factoryKey
        } catch (e: Exception) {
            Log.d(tag, "readFactoryKey cause: ${e.cause}")
            Log.d(tag, "readFactoryKey message: ${e.message}")
            e.printStackTrace()
            return null
        }

    }

    private suspend fun readMasterKey(): String? {
        try {
            val responseBuffer: ByteArray? =
                device.sendApdu(ISOUtil.hex2byte(ApduUtil.getReadMasterKeyCommand()))
            return if (responseBuffer == null) null
            else if (!ApduUtil.apduResponseIsSuccess(responseBuffer)) null
            else ISOUtil.hexString(responseBuffer, 0, 128)
        } catch (e: Exception) {
            Log.d(tag, "readMasterKey cause: ${e.cause}")
            Log.d(tag, "readMasterKey:message ${e.message}")
            return null
        }

    }

    private suspend fun verifySecondPin(pin: String): String? {
        try {
            val responseBuffer: ByteArray? =
                device.sendApdu(ISOUtil.hex2byte(ApduUtil.getVerifySecondPinCommand(pin))!!)
            return if (responseBuffer == null) {
                null
            } else if (!ApduUtil.apduResponseIsSuccess(responseBuffer)) {
                null
            } else {
                ISOUtil.hexString(responseBuffer)
            }
        } catch (e: Exception) {
            Log.d(tag, "verifySecondPin cause: " + e.cause)
            Log.d(tag, "verifySecondPin message: " + e.message)
            return null
        }
    }

    override suspend fun readPublicKey(): String? {
        return withContext(ioDispatcher) {
            try {
                var responseBuffer: ByteArray? =
                    device.sendApdu(ISOUtil.hex2byte(ApduUtil.getReadPublicKeyCommand()))
                var responseLength = responseBuffer!!.size
                val publicKeyExponent = ISOUtil.hexString(responseBuffer, 0, responseLength - 2)
                responseBuffer = device.sendApdu(ISOUtil.hex2byte(ApduUtil.getExponentCommand()))
                responseLength = responseBuffer!!.size
                val publicKeyData = ISOUtil.hexString(responseBuffer, 0, responseLength - 2)
                val spec =
                    RSAPublicKeySpec(
                        BigInteger(publicKeyData, 16),
                        BigInteger(publicKeyExponent, 16)
                    )
                var factory: KeyFactory? = null
                try {
                    factory = KeyFactory.getInstance("RSA")
                } catch (e: NoSuchAlgorithmException) {
                    Log.d(tag, "readPublicKey NoSuchAlgorithmException cause: " + e.cause)
                    Log.d(tag, "NoSuchAlgorithmException message: " + e.message)
                    return@withContext null
                }
                var pub: PublicKey? = null
                try {
                    pub = factory.generatePublic(spec)
                } catch (e: InvalidKeySpecException) {
                    Log.d(tag, "readPublicKey InvalidKeySpecException cause: " + e.cause)
                    Log.d(tag, "InvalidKeySpecException message: " + e.message)
                    return@withContext null
                }
                val publicKey = ISOUtil.hexString(pub.encoded)
                return@withContext publicKey
            } catch (e: Exception) {
                Log.d(tag, "readPublicKey Exception cause: " + e.cause)
                Log.d(tag, "Exception message: " + e.message)
                return@withContext null
            }
            return@withContext null
        }
    }

    override suspend fun readPrivateKey(): PrivateKey? {
      return withContext(ioDispatcher) {
          try {
              var responseBuffer: ByteArray? =
                  device.sendApdu(ISOUtil.hex2byte(ApduUtil.getReadPrivateKeyCommand()))
              var responseLength = responseBuffer!!.size
              val privateKeyExponent = ISOUtil.hexString(responseBuffer, 0, responseLength - 2)
              responseBuffer = device.sendApdu(ISOUtil.hex2byte("ACBD010180"))
              responseLength = responseBuffer!!.size
              val privateKeyData = ISOUtil.hexString(responseBuffer, 0, responseLength - 2)
              val privateSpec = RSAPrivateKeySpec(
                  BigInteger(privateKeyData, 16),
                  BigInteger(privateKeyExponent, 16)
              )
              var factory: KeyFactory? = null
              try {
                  factory = KeyFactory.getInstance("RSA")
                  return@withContext factory.generatePrivate(privateSpec)
              } catch (e: NoSuchAlgorithmException) {
                  Log.e(tag, "readPrivateKey NoSuchAlgorithmException: ", e.cause)
                  Log.e(tag, "NoSuchAlgorithmException: ${e.message}")
              } catch (e: InvalidKeySpecException) {
                  Log.e(tag, "readPrivateKey InvalidKeySpecException: ", e.cause)
                  Log.e(tag, "InvalidKeySpecException: ${e.message}")
              }
          } catch (e: Exception) {
              Log.e(tag, "readPrivateKey Exception: ", e.cause)
              Log.e(tag, "readPrivateKey Exception: ${e.message}")
          }
          return@withContext null
      }
    }

    override suspend fun confirmFirstPin(pin: String): Boolean {
        return withContext(ioDispatcher) {
            try {
                val selectAppletResult: String? = selectFirstApplet()
                Log.d("TAG", "confirmFirstPddin: dddddddddddd$selectAppletResult")
                if (selectAppletResult == null) false
                else {
                    val verifyPinResult: String? = verifyFirstPin(pin)
                    Log.d("TAG", "confirmFirstPddin: ddddddddddddd$verifyPinResult")
                    verifyPinResult != null
                }
            } catch (e: Exception) {
                Log.e(tag, "confirmFirstPin exception cause: ${e.cause}")
                Log.e(tag, "confirmFirstPin exception message: ${e.message}")
                e.printStackTrace()
                false
            }
        }

    }
}