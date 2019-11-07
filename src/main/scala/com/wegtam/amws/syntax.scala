/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws

import java.time.OffsetDateTime

import com.wegtam.amws.common.{ MarketPlace, RequestParameters }
import com.wegtam.amws.reports.{ Actions, ReportType }

import scala.collection.immutable.Seq

object syntax {
  def buildReportRequest(reportType: ReportType)(
      startDate: Option[OffsetDateTime]
  )(endDate: Option[OffsetDateTime])(marketplaces: Seq[MarketPlace]): RequestParameters =
    Actions.RequestReport.buildRequestParameters(reportType, startDate, endDate, marketplaces)
}
