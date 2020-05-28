/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.network

import java.net.URI

import cats.data._
import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.client._
import org.http4s.headers._
import org.http4s.MediaType

import scala.language.higherKinds

/**
  * The implementation of a [[HttpClientProvider]] using http4s.
  *
  * @param client A http4s client.
  */
class HttpClientProviderHttp4s[F[_]: ConcurrentEffect](client: Client[F])
    extends HttpClientProvider[Lambda[A => EitherT[F, AmwsError, A]]] {

  /**
    * Perform the given HTTP request and return either the result or an error.
    *
    * @param req An HTTP request.
    * @return The corresponding AmwsResponse.
    */
  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  protected def performRequest(req: Request[F]): EitherT[F, AmwsError, AmwsResponse] =
    EitherT {
      client.fetch(req) {
        case Status.Successful(r) => r.as[String].map(b => Right(AmwsResponse(body = b)))
        case r                    => r.as[String].map(b => Left(AmwsError(code = r.status.code, details = Option(b))))
      }
    }

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  override def get(url: URI)(payload: AmwsRequestPayload): EitherT[F, AmwsError, AmwsResponse] = {
    val req = Request[F](
      method = Method.GET,
      uri = Uri.unsafeFromString(url.toASCIIString)
    ).withHeaders(`Content-Type`(MediaType.application.`x-www-form-urlencoded`))
      .withEntity(payload.data.getOrElse(""))
    performRequest(req)
  }

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  override def post(url: URI)(payload: AmwsRequestPayload): EitherT[F, AmwsError, AmwsResponse] = {
    val req = Request[F](
      method = Method.POST,
      uri = Uri.unsafeFromString(url.toASCIIString)
    ).withHeaders(`Content-Type`(MediaType.application.`x-www-form-urlencoded`))
      .withEntity(payload.data.getOrElse(""))
    performRequest(req)
  }
}
