/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.common

import java.nio.charset.StandardCharsets

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
    * Return the secret key for the amazon mws api from the configuration.
    *
    * @param cfg The configuration.
    * @return An array of bytes holding the key which may be empty.
    */
  def getSecretKey(cfg: com.typesafe.config.Config): Array[Byte] =
    Try(cfg.getString("amws.secret-key")).toOption
      .map(_.getBytes(StandardCharsets.UTF_8))
      .getOrElse(Array.empty)

  /**
    * Return the seller id from the configuration.
    *
    * @param cfg The configuration.
    * @return An option to the seller id if it is configured.
    */
  def getSellerId(cfg: com.typesafe.config.Config): Option[String] =
    Try(cfg.getString("amws.seller-id")).toOption

}
