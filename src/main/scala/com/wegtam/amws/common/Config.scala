package com.wegtam.amws.common

import scala.util.Try

object Config {

  /**
    * Return the AWSAccessKeyId from the configuration.
    *
    * @param cfg The configuration.
    * @return An option to the AWSAccessKeyId if it is configured.
    */
  def getAccessKeyId(cfg: com.typesafe.config.Config): Option[String] =
    Try(cfg.getString("amws.access-key-id")).toOption

  /**
    * Return the authorization token from the configuration.
    *
    * @param cfg The configuration.
    * @return An option to the authorization token if it is configured.
    */
  def getAuthToken(cfg: com.typesafe.config.Config): Option[String] =
    Try(cfg.getString("amws.auth-token")).toOption

  /**
    * Return the seller id from the configuration.
    *
    * @param cfg The configuration.
    * @return An option to the seller id if it is configured.
    */
  def getSellerId(cfg: com.typesafe.config.Config): Option[String] =
    Try(cfg.getString("amws.seller-id")).toOption

}
