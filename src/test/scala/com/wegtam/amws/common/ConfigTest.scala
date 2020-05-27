/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.common

import java.nio.charset.StandardCharsets

import com.typesafe.config.ConfigFactory

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ConfigTest extends AnyWordSpec with Matchers {
  "getAccessKeyId" when {
    "setting is defined" must {
      "return the proper value" in {
        Config.getAccessKeyId(ConfigFactory.parseString("""amws.access-key-id=FOO""")) must contain(
          "FOO"
        )
      }
    }

    "setting is undefined" must {
      "return an empty option" in {
        Config.getAccessKeyId(ConfigFactory.parseString("")) must be(None)
      }
    }
  }

  "getAuthToken" when {
    "setting is defined" must {
      "return the proper value" in {
        Config.getAuthToken(ConfigFactory.parseString("""amws.auth-token=BAR""")) must contain(
          "BAR"
        )
      }
    }

    "setting is undefined" must {
      "return an empty option" in {
        Config.getAuthToken(ConfigFactory.parseString("")) must be(None)
      }
    }
  }

  "getSecretKey" when {
    "setting is defined" must {
      "return the proper value" in {
        val key = "My voice is my passport. Verify me!"
        Config.getSecretKey(ConfigFactory.parseString(s"""amws.secret-key="$key"""")) mustEqual key
          .getBytes(StandardCharsets.UTF_8)
      }
    }

    "setting is undefined" must {
      "return an empty array" in {
        Config.getSecretKey(ConfigFactory.parseString("")) must be(Array.empty)
      }
    }
  }

  "getSellerId" when {
    "setting is defined" must {
      "return the proper value" in {
        Config.getSellerId(ConfigFactory.parseString("""amws.seller-id=FOOBAR""")) must contain(
          "FOOBAR"
        )
      }
    }

    "setting is undefined" must {
      "return an empty option" in {
        Config.getSellerId(ConfigFactory.parseString("")) must be(None)
      }
    }
  }
}
