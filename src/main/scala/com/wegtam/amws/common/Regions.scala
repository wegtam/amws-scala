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
