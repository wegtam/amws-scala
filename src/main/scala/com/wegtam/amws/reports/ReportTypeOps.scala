/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.reports

import java.time.OffsetDateTime

import com.wegtam.amws.common.{ MarketPlace, RequestParameters }

import scala.collection.immutable._

trait ReportTypeOps[T] {
  def buildReportRequest(t: T)(from: Option[OffsetDateTime])(to: Option[OffsetDateTime])(
      ms: Seq[MarketPlace]
  ): RequestParameters
}

object ReportTypeOps {
  implicit object ReportTypeOpsImpl extends ReportTypeOps[ReportType] {
    override def buildReportRequest(t: ReportType)(
        from: Option[OffsetDateTime]
    )(to: Option[OffsetDateTime])(ms: Seq[MarketPlace]): RequestParameters =
      Actions.RequestReport.buildRequestParameters(t, from, to, ms)
  }
}
