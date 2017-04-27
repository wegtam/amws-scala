package com.wegtam.amws.common

import java.net.{ URI, URLEncoder }
import java.nio.charset.StandardCharsets

import org.scalacheck.Gen
import org.scalatest.prop.PropertyChecks
import org.scalatest.{ MustMatchers, WordSpec }

import scala.util.{ Failure, Success }

class RequestTest extends WordSpec with MustMatchers with PropertyChecks {

  "buildBaseRequest" must {
    "include AWSAccessKeyId" in {
      val m = Request.buildBaseRequest(
        accessKeyId = "KEY",
        authToken = "TOKEN",
        sellerId = "SELLERID",
        version = "VERSION"
      )
      m.get("AWSAccessKeyId") must contain("KEY")
    }

    "include MWSAuthToken" in {
      val m = Request.buildBaseRequest(
        accessKeyId = "KEY",
        authToken = "TOKEN",
        sellerId = "SELLERID",
        version = "VERSION"
      )
      m.get("MWSAuthToken") must contain("TOKEN")
    }

    "include SellerId" in {
      val m = Request.buildBaseRequest(
        accessKeyId = "KEY",
        authToken = "TOKEN",
        sellerId = "SELLERID",
        version = "VERSION"
      )
      m.get("SellerId") must contain("SELLERID")
    }

    "include Version" in {
      val m = Request.buildBaseRequest(
        accessKeyId = "KEY",
        authToken = "TOKEN",
        sellerId = "SELLERID",
        version = "VERSION"
      )
      m.get("Version") must contain("VERSION")
    }
  }

  "buildAndSignQueryString" must {
    "create a signed query string" in {
      val base = Request.buildBaseRequest(
        accessKeyId = "0PExampleR2",
        authToken = "amzn.mws.4ea38b7b-f563-7709-4bae-87aeaEXAMPLE",
        sellerId = "A1ExampleE6",
        version = "2009-01-01"
      )
      val params = Map(
        "Marketplace" -> "ATExampleER",
        "Action"      -> "SubmitFeed",
        "FeedType"    -> "_POST_INVENTORY_AVAILABILITY_DATA_"
      )
      Request.buildAndSignQueryString(
        baseUrl = new URI("https://mws.amazonservices.com/Feeds/2009-01-01"),
        key = "Whatever key it may be...".getBytes(StandardCharsets.UTF_8),
        ps = base ++ params
      ) match {
        case Failure(t) => fail(t.getMessage)
        case Success(q) =>
          q must include("Timestamp=")
          q must include("Signature=")
      }
    }
  }

  "urlEncode" must {

    val urlParamValues = for { u <- Gen.alphaNumStr } yield u

    "work correctly" in {
      forAll(urlParamValues) { v =>
        Request.urlEncode(v) mustEqual URLEncoder
          .encode(v, "UTF-8")
          .replace("+", "%20")
          .replace("*", "%2A")
          .replace("%7E", "~")
      }
    }
  }

}
