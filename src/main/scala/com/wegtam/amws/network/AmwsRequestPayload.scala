/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.network

/**
  * A wrapper class for the data which is used as the payload for an
  * http request.
  *
  * @param data An optional string containing the data.
  */
final case class AmwsRequestPayload(data: Option[String])
