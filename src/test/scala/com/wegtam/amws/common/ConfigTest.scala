package com.wegtam.amws.common

import com.typesafe.config.ConfigFactory
import org.scalatest.{ MustMatchers, WordSpec }

class ConfigTest extends WordSpec with MustMatchers {

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
