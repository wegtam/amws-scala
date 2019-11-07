/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.common

import java.net.URI

import scala.collection.immutable.Seq

/**
  * A trait for amazon regions.
  */
sealed trait Region { self =>

  /**
    * The endpoint uri of the region that must be used for api access.
    *
    * @return A uri with the api endpoint.
    */
  def endPoint: URI

  /**
    * A list of all market places associated with the region.
    *
    * @return A list of market places.
    */
  def marketPlaces: Seq[MarketPlace] = MarketPlaces.ALL.filter(_.region == self)
}

object Regions {
  case object NorthAmerica extends Region {
    override def endPoint: URI = new URI("https://mws.amazonservices.com")
  }

  case object Brazil extends Region {
    override def endPoint: URI = new URI("https://mws.amazonservices.com")
  }

  case object Europe extends Region {
    override def endPoint: URI = new URI("https://mws-eu.amazonservices.com")
  }

  case object India extends Region {
    override def endPoint: URI = new URI("https://mws.amazonservices.in")
  }

  case object China extends Region {
    override def endPoint: URI = new URI("https://mws.amazonservices.com.cn")
  }

  case object Japan extends Region {
    override def endPoint: URI = new URI("https://mws.amazonservices.jp")
  }
}
