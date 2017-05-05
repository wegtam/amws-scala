package com.wegtam.amws.common

import com.wegtam.amws.common.Request.ParameterValue

import scala.collection.immutable.Seq

/**
  * Amazon marketplaces.
  */
sealed trait MarketPlace {

  /**
    * Return the region that the market place belongs to.
    *
    * @return A region.
    */
  def region: Region

  /**
    * The parameter value of the marketplace that must be used in the
    * query string.
    *
    * @return A string representation of the marketplace, usable in a query.
    */
  def toParameterValue: ParameterValue

}

object MarketPlaces {
  @SuppressWarnings(
    Array("org.wartremover.warts.Any",
          "org.wartremover.warts.Product",
          "org.wartremover.warts.Serializable")
  )
  final val ALL: Seq[MarketPlace] = Seq(BR, CA, CN, DE, ES, FR, IN, IT, JP, MX, UK, US)

  case object CA extends MarketPlace {
    override def region: Region = Regions.NorthAmerica

    override def toParameterValue: ParameterValue = "A2EUQ1WTGCTBG2"
  }

  case object MX extends MarketPlace {
    override def region: Region = Regions.NorthAmerica

    override def toParameterValue: ParameterValue = "A1AM78C64UM0Y8"
  }

  case object US extends MarketPlace {
    override def region: Region = Regions.NorthAmerica

    override def toParameterValue: ParameterValue = "ATVPDKIKX0DER"
  }

  case object BR extends MarketPlace {
    override def region: Region = Regions.Brazil

    override def toParameterValue: ParameterValue = "A2Q3Y263D00KWC"
  }

  case object DE extends MarketPlace {
    override def region: Region = Regions.Europe

    override def toParameterValue: ParameterValue = "A1PA6795UKMFR9"
  }

  case object ES extends MarketPlace {
    override def region: Region = Regions.Europe

    override def toParameterValue: ParameterValue = "A1RKKUPIHCS9HS"
  }

  case object FR extends MarketPlace {
    override def region: Region = Regions.Europe

    override def toParameterValue: ParameterValue = "A13V1IB3VIYZZH"
  }

  case object IT extends MarketPlace {
    override def region: Region = Regions.Europe

    override def toParameterValue: ParameterValue = "APJ6JRA9NG5V4"
  }

  case object UK extends MarketPlace {
    override def region: Region = Regions.Europe

    override def toParameterValue: ParameterValue = "A1F83G8C2ARO7P"
  }

  case object IN extends MarketPlace {
    override def region: Region = Regions.India

    override def toParameterValue: ParameterValue = "A21TJRUUN4KGV"
  }

  case object JP extends MarketPlace {
    override def region: Region = Regions.Japan

    override def toParameterValue: ParameterValue = "A1VC38T7YXB528"
  }

  case object CN extends MarketPlace {
    override def region: Region = Regions.China

    override def toParameterValue: ParameterValue = "AAHKV2X7AFYLW"
  }

}
