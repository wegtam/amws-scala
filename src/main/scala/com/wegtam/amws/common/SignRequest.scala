package com.wegtam.amws.common

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import com.wegtam.amws.common.Request.{ ParameterName, RequestParameters }

import scala.util.Try

object SignRequest {
  // The field name for the query parameter that will hold the signature.
  final val SignatureRequestFieldName: ParameterName = "Signature"
  // These fields are needed for signed requests and must be added accordingly.
  final val SignatureRequestFields: RequestParameters = Map(
    "SignatureMethod"  -> "HmacSHA256",
    "SignatureVersion" -> "2"
  )

  /**
    * Sign the given date using the provided key and the HMAC-SHA256 algorithm.
    * A string containing the base64 encoded signature is returned.
    *
    * @param key  The AWS access key.
    * @param data The data that shall be signed.
    * @return Either an error or a string (utf-8) holding the base64 encoded signature.
    */
  def sign(key: Array[Byte], data: Array[Byte]): Try[String] = Try {
    val algo = "HmacSHA256"
    val base = Base64.getEncoder
    val hmac = Mac.getInstance(algo)
    val skey = new SecretKeySpec(key, algo)
    hmac.init(skey)
    val sig = hmac.doFinal(data)
    new String(base.encode(sig), StandardCharsets.UTF_8)
  }

}
