/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.common

import com.wegtam.amws.common.MarketPlaces._
import org.scalatest.prop.PropertyChecks
import org.scalatest.{ MustMatchers, WordSpec }

import scala.collection.immutable.Seq

class MarketPlacesTest extends WordSpec with MustMatchers with PropertyChecks {
  private final val expectedIds = Table(
    ("Marketplace", "ID"),
    (BR, "A2Q3Y263D00KWC"),
    (CA, "A2EUQ1WTGCTBG2"),
    (CN, "AAHKV2X7AFYLW"),
    (DE, "A1PA6795UKMFR9"),
    (ES, "A1RKKUPIHCS9HS"),
    (FR, "A13V1IB3VIYZZH"),
    (IN, "A21TJRUUN4KGV"),
    (IT, "APJ6JRA9NG5V4"),
    (JP, "A1VC38T7YXB528"),
    (MX, "A1AM78C64UM0Y8"),
    (UK, "A1F83G8C2ARO7P"),
    (US, "ATVPDKIKX0DER")
  )
  private final val expectedRegions = Table(
    ("Marketplace", "Region"),
    (BR, Regions.Brazil),
    (CA, Regions.NorthAmerica),
    (CN, Regions.China),
    (DE, Regions.Europe),
    (ES, Regions.Europe),
    (FR, Regions.Europe),
    (IN, Regions.India),
    (IT, Regions.Europe),
    (JP, Regions.Japan),
    (MX, Regions.NorthAmerica),
    (UK, Regions.Europe),
    (US, Regions.NorthAmerica)
  )

  "ALL" must {
    "contain all entries" in {
      val expected = Seq(BR, CA, CN, DE, ES, FR, IN, IT, JP, MX, UK, US)
      MarketPlaces.ALL.size must be(expected.size)
      MarketPlaces.ALL mustEqual expected
    }
  }

  "toParameterValue" must {
    "return the correct marketplace id" in {
      forAll(expectedIds) { (mp: MarketPlace, id: ParameterValue) =>
        mp.toParameterValue mustEqual id
      }
    }
  }

  "region" must {
    "return the correct region of the marketplace" in {
      forAll(expectedRegions) { (mp: MarketPlace, r: Region) =>
        mp.region must be(r)
      }
    }
  }

}
