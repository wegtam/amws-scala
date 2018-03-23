/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.network

import java.net.URI

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.Materializer
import akka.util.ByteString

import scala.concurrent.{ ExecutionContext, Future }

/**
  * The implementation of a [[HttpClientProvider]] using akka http.
  *
  * @param actorSystem  An implicit actor system.
  * @param materializer An implicit actor materializer.
  */
class HttpClientProviderAkkaHttp(implicit actorSystem: ActorSystem, materializer: Materializer)
    extends HttpClientProvider[Future] {
  implicit private val executionContext: ExecutionContext = actorSystem.dispatcher

  /**
    * Build the http request entity from the given parameters.
    *
    * @param method  The actual http method to use (GET, POST).
    * @param url     A url which is used for the request.
    * @param payload The payload for the request.
    * @return An HttpRequest entity.
    */
  protected def buildRequest(
      method: HttpMethod
  )(url: URI)(payload: AmwsRequestPayload): HttpRequest =
    HttpRequest(
      method = method,
      uri = url.toString,
      entity = HttpEntity(
        contentType =
          ContentType(MediaTypes.`application/x-www-form-urlencoded`, HttpCharsets.`UTF-8`),
        data = ByteString(payload.data.getOrElse(""))
      )
    )

  /**
    * Perform the given HttpRequest and convert the result into a [[AmwsRequestResult]].
    *
    * @param httpRequest An http request.
    * @return The corresponding AmwsRequestResult.
    */
  protected def performRequest(httpRequest: HttpRequest): Future[AmwsRequestResult] =
    for {
      r <- Http().singleRequest(httpRequest)
      b <- r.entity.dataBytes.runFold(ByteString(""))(_ ++ _)
    } yield
      r.status match {
        case StatusCodes.OK => Right(AmwsResponse(body = b.utf8String))
        case _              => Left(AmwsError(code = r.status.intValue(), details = Option(b.utf8String)))
      }

  override def get(url: URI)(payload: AmwsRequestPayload): Future[AmwsRequestResult] = {
    val request = buildRequest(HttpMethods.GET)(url)(payload)
    performRequest(request)
  }

  override def post(url: URI)(payload: AmwsRequestPayload): Future[AmwsRequestResult] = {
    val request = buildRequest(HttpMethods.POST)(url)(payload)
    performRequest(request)
  }
}
