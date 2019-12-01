/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.common

import java.net.URI

import com.wegtam.amws.common.MarketPlaces._
import com.wegtam.amws.common.Regions._
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.collection.immutable.Seq
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RegionsTest extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks {
  private final val expectedEndpoints = Table(
    ("Region", "Endpoint"),
    (NorthAmerica, new URI("https://mws.amazonservices.com")),
    (Brazil, new URI("https://mws.amazonservices.com")),
    (Europe, new URI("https://mws-eu.amazonservices.com")),
    (India, new URI("https://mws.amazonservices.in")),
    (China, new URI("https://mws.amazonservices.com.cn")),
    (Japan, new URI("https://mws.amazonservices.jp"))
  )
  private final val expectedMarketplaces = Table(
    ("Region", "Marketplaces"),
    (NorthAmerica, Seq(CA, MX, US)),
    (Brazil, Seq(BR)),
    (Europe, Seq(DE, ES, FR, IT, UK)),
    (India, Seq(IN)),
    (China, Seq(CN)),
    (Japan, Seq(JP))
  )

  "endpoint" must {
    "return the correct region endpoint" in {
      forAll(expectedEndpoints) { (r: Region, u: URI) =>
        r.endPoint mustEqual u
      }
    }
  }

  "marketPlaces" must {
    "return the correct marketplaces for the region" in {
      forAll(expectedMarketplaces) { (r: Region, mps: Seq[MarketPlace]) =>
        r.marketPlaces mustEqual mps
      }
    }
  }
}
