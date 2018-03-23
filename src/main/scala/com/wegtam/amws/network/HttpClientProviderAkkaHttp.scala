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

  override def post(url: URI)(query: String): Future[Response] = {
    val request = Http().singleRequest(
      HttpRequest(
        method = HttpMethods.POST,
        uri = url.toString,
        entity = HttpEntity(
          contentType =
            ContentType(MediaTypes.`application/x-www-form-urlencoded`, HttpCharsets.`UTF-8`),
          data = ByteString(query)
        )
      )
    )

    for {
      r <- request
      b <- r.entity.dataBytes.runFold(ByteString(""))(_ ++ _)
    } yield
      r.status match {
        case StatusCodes.OK => Right(HttpResponse(body = b.utf8String))
        case _              => Left(HttpError(code = r.status.intValue(), details = Option(b.utf8String)))
      }
  }
}
