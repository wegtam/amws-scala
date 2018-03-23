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
    * Execute a POST http request on the given url using the provided query (data).
    *
    * @param url   A url which is used for the request.
    * @param query A string containing the query data which may be empty. <em>The string must be correctly built and signed as documented in the amazon api docs!</em>
    * @return The response of the query.
    */
  def post(url: URI)(query: String): M[Response]

}
