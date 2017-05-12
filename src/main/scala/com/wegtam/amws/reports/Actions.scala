/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.reports

import java.time.OffsetDateTime

import com.wegtam.amws.common.{ Action, MarketPlace }
import com.wegtam.amws.common.Request.{ ParameterValue, RequestParameters }

import scala.collection.immutable.Seq

/**
  * API actions for the Reports section.
  *
  * The Reports API section of the Amazon Marketplace Web Service (Amazon
  * MWS) API lets you request various reports that help you manage your
  * Sell on Amazon business. Report types are specified using the
  * `ReportTypes` enumeration.
  */
object Actions {

  /**
    * The `RequestReport` operation creates a report request. Amazon MWS
    * processes the report request and when the report is completed,
    * sets the status of the report request to `_DONE_`.
    * Reports are retained for 90 days.
    */
  case object RequestReport extends Action {

    def buildRequest(baseR: RequestParameters)(
        reportType: ReportType,
        startDate: Option[OffsetDateTime],
        endDate: Option[OffsetDateTime],
        marketplaceIdList: Seq[String]
    ): RequestParameters = {
      val ps = baseR + ("ReportType" -> reportType.toParameterValue) +
      ("Action" -> toParameterValue) ++
      marketplaceIdList.zipWithIndex.map(t => s"MarketplaceIdList.Id.${t._2 + 1}" -> t._1)
      val ps2 = startDate.fold(ps)(sd => ps + ("StartDate" -> sd.toString))
      val ps3 = endDate.fold(ps2)(ed => ps2 + ("EndDate"   -> ed.toString))
      ps3
    }

    override def toParameterValue: ParameterValue = "RequestReport"
  }

  /**
    * The `GetReportRequestList` operation returns a list of report requests
    * that match the query parameters. You can specify query parameters for
    * report status, date range, and report type. The list contains the
    * `ReportRequestId` for each report request. You can obtain `ReportId``
    * values by passing the `ReportRequestId` values to the `GetReportList``
    * operation.
    */
  case object GetReportRequestList extends Action {
    final val DEFAULT_PAGE_SIZE = 50

    def buildRequest(baseR: RequestParameters)(m: MarketPlace,
                                               rids: Seq[String]): RequestParameters = {
      val r: RequestParameters = baseR ++ Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> m.toParameterValue,
        "MaxCount"    -> DEFAULT_PAGE_SIZE.toString
      )
      r ++ rids.zipWithIndex.map(t => s"ReportRequestIdList.Id.${t._2 + 1}" -> t._1)
    }

    override def toParameterValue: ParameterValue = "GetReportRequestList"
  }

  /**
    * Returns a list of report requests using the `NextToken`, which was
    * supplied by a previous request to either
    * `GetReportRequestListByNextToken` or `GetReportRequestList`, where
    * the value of `HasNext` was true in that previous request.
    */
  case object GetReportRequestListByNextToken extends Action {

    def buildRequest(baseR: RequestParameters)(m: MarketPlace, nt: String): RequestParameters =
      baseR ++ Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> m.toParameterValue,
        "NextToken"   -> nt
      )

    override def toParameterValue: ParameterValue =
      "GetReportRequestListByNextToken"
  }

  /**
    * Returns a count of report requests that have been submitted to
    * Amazon MWS for processing.
    */
  case object GetReportRequestCount extends Action {
    override def toParameterValue: ParameterValue = "GetReportRequestCount"
  }

  /**
    * Cancels one or more report requests.
    */
  case object CancelReportRequests extends Action {
    override def toParameterValue: ParameterValue = "CancelReportRequests"
  }

  /**
    * Returns a list of reports that were created in the previous 90 days.
    */
  case object GetReportList extends Action {
    final val DEFAULT_PAGE_SIZE = 50

    def buildRequest(baseR: RequestParameters)(m: MarketPlace): RequestParameters =
      baseR ++ Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> m.toParameterValue,
        "MaxCount"    -> DEFAULT_PAGE_SIZE.toString
      )

    override def toParameterValue: ParameterValue = "GetReportList"
  }

  /**
    * Returns a list of reports using the `NextToken`, which was supplied
    * by a previous request to either `GetReportListByNextToken` or
    * `GetReportList`, where the value of `HasNext` was true in the
    * previous call.
    */
  case object GetReportListByNextToken extends Action {

    def buildRequest(baseR: RequestParameters)(m: MarketPlace, nt: String): RequestParameters =
      baseR ++ Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> m.toParameterValue,
        "NextToken"   -> nt
      )

    override def toParameterValue: ParameterValue = "GetReportListByNextToken"
  }

  /**
    * Returns a count of the reports, created in the previous 90 days,
    * with a status of `_DONE_` and that are available for download.
    */
  case object GetReportCount extends Action {
    override def toParameterValue: ParameterValue = "GetReportCount"
  }

  /**
    * Returns the contents of a report and the Content-MD5 header for the
    * returned report body.
    */
  case object GetReport extends Action {

    def buildRequest(baseR: RequestParameters)(m: MarketPlace, rid: String): RequestParameters =
      baseR ++ Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> m.toParameterValue,
        "ReportId"    -> rid
      )

    override def toParameterValue: ParameterValue = "GetReport"
  }

  /**
    * Creates, updates, or deletes a report request schedule for a
    * specified report type.
    */
  case object ManageReportSchedule extends Action {
    override def toParameterValue: ParameterValue = "ManageReportSchedule"
  }

  /**
    * Returns a list of order report requests that are scheduled to be
    * submitted to Amazon MWS for processing.
    */
  case object GetReportScheduleList extends Action {
    override def toParameterValue: ParameterValue = "GetReportScheduleList"
  }

  /**
    * Currently this operation can never be called because the
    * `GetReportScheduleList` operation cannot return more than 100
    * results. It is included for future compatibility.
    */
  case object GetReportScheduleListByNextToken extends Action {
    override def toParameterValue: ParameterValue =
      "GetReportScheduleListByNextToken"
  }

  /**
    * Returns a count of order report requests that are scheduled to be
    * submitted to Amazon MWS.
    */
  case object GetReportScheduleCount extends Action {
    override def toParameterValue: ParameterValue = "GetReportScheduleCount"
  }

  /**
    * Updates the acknowledged status of one or more reports.
    */
  case object UpdateReportAcknowledgements extends Action {
    override def toParameterValue: ParameterValue =
      "UpdateReportAcknowledgements"
  }

}
