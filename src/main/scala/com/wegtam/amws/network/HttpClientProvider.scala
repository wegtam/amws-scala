/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.network

import java.net.URI

import scala.language.higherKinds

/**
  * A base for implementing http client providers.
  *
  * @tparam M The return type wrapper.
  */
trait HttpClientProvider[M[_]] {

  /**
    * Execute a GET http request on the given url using the provided query (data).
    *
    * @param url     A url which is used for the request.
    * @param payload The payload for the request.
    * @return The response of the query.
    */
  def get(url: URI)(payload: AmwsRequestPayload): M[AmwsResponse]

  /**
    * Execute a POST http request on the given url using the provided query (data).
    *
    * @param url     A url which is used for the request.
    * @param payload The payload for the request.
    * @return The response of the query.
    */
  def post(url: URI)(payload: AmwsRequestPayload): M[AmwsResponse]
}
