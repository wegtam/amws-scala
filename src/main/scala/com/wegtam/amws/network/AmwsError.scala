/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.network

/**
  * A wrapper class that must be used for errors returned by an http request.
  *
  * @param code    The http status code.
  * @param details An optional detailed error description.
  */
final case class AmwsError(code: Int, details: Option[String])
