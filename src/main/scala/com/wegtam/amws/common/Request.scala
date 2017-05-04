package com.wegtam.amws.common

import java.net.{ URI, URLEncoder }
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.util.Locale

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.util.ByteString

import scala.concurrent.Future
import scala.util.Try

object Request {
  type ApiVersion        = String
  type ParameterName     = String
  type ParameterValue    = String
  type QueryString       = String
  type RequestParameters = Map[ParameterName, ParameterValue]

  /**
    * Build a parameters and values that are needed for every request.
    *
    * This parameter map produced by this function will need to be modified
    * to include the desired action and the relevant parameters.
    *
    * Finally the modified parameter map will have to be signed.
    *
    * @see http://docs.developer.amazonservices.com/en_US/dev_guide/DG_RequiredRequestParameters.html
    *
    * @param accessKeyId The AWSAccessKeyId that you received when you registered for Amazon MWS.
    * @param authToken   The optional authorization token that you received when you registered for Amazon MWS.
    * @param sellerId    The seller or merchant identifier that you received when you registered for Amazon MWS.
    * @param version     The version of the API section being called.
    * @return A map of parameter names and values.
    */
  def buildBaseRequest(accessKeyId: ParameterValue,
                       authToken: Option[ParameterValue],
                       sellerId: ParameterValue,
                       version: ApiVersion): RequestParameters = {
    val a: RequestParameters =
      authToken.fold(Map.empty[ParameterName, ParameterValue])(t => Map("MWSAuthToken" -> t))
    a ++ Map(
      "AWSAccessKeyId" -> accessKeyId,
      "SellerId"       -> sellerId,
      "Version"        -> version
    )
  }

  /**
    * Use the given base url and secret api key with the provided request
    * parameters to create a valid query string including signature for the
    * amazon api.
    *
    * The `Timestamp` field will be set (overriding a previously set value).
    *
    * @param baseUrl The base url of the service.
    * @param key     The secret amazon key used to sign the requests.
    * @param ps      The request parameters.
    * @return Either an error or a usable query string for a request to the amazon api.
    */
  def buildAndSignQueryString(baseUrl: URI,
                              key: Array[Byte],
                              ps: RequestParameters): Try[QueryString] =
    for {
      qstr <- Try {
        val encodedP =
          (ps ++ SignRequest.SignatureRequestFields +
          ("Timestamp" -> OffsetDateTime.now().toString)).map(p => p._1 -> urlEncode(p._2)).toList
        encodedP.sortBy(_._1).map(p => s"${p._1}=${p._2}").mkString("&")
      }
      data <- Try {
        s"""|POST
           |${baseUrl.getHost.toLowerCase(Locale.ROOT)}
           |${baseUrl.getPath}
           |$qstr""".stripMargin
      }
      sign <- SignRequest.sign(key, data.getBytes(StandardCharsets.UTF_8))
      encs  = urlEncode(sign)
      query = s"$qstr&${SignRequest.SignatureRequestFieldName}=$encs"
    } yield query

  /**
    * Execute a request using the given base url and query string.
    *
    * An implicit materializer and system have to be in scope.
    *
    * @param baseUrl     The base url of the service to call.
    * @param queryString An encoded and signed query string.
    * @param mat         Implicit actor materializer for akka http.
    * @param sys         Implicit actor system for akka http.
    * @return A future holding the http response of the api.
    */
  def execute(baseUrl: URI, queryString: QueryString)(implicit mat: ActorMaterializer,
                                                      sys: ActorSystem): Future[HttpResponse] =
    Http().singleRequest(
      HttpRequest(
        method = HttpMethods.POST,
        uri = baseUrl.toString,
        entity = HttpEntity(
          contentType =
            ContentType(MediaTypes.`application/x-www-form-urlencoded`, HttpCharsets.`UTF-8`),
          data = ByteString(queryString)
        )
      )
    )

  /**
    * URL encode the given parameter value as expected by the amazon api.
    *
    * @param s A url parameter value.
    * @return A string holding the encoded value.
    */
  @throws[java.io.UnsupportedEncodingException]
  def urlEncode(s: ParameterValue): ParameterValue =
    URLEncoder.encode(s, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~")

}
