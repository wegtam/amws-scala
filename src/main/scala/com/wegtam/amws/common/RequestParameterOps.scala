/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.common

import scala.collection.immutable._

trait RequestParameterOps[T] {
  def toRequestParameters(t: T): RequestParameters
}

object RequestParameterOps {
  implicit object RequestParameterOpsSingle extends RequestParameterOps[RequestParameter] {
    override def toRequestParameters(t: RequestParameter): RequestParameters = Map(
      t.name -> t.value
    )
  }

  object syntax {
    implicit final class WrapRequestParameterOps[T](val t: T) extends AnyVal {
      def toRequestParameters(implicit ev: RequestParameterOps[T]): RequestParameters =
        ev.toRequestParameters(t)
    }
  }
}
