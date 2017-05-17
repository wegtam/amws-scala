/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.reports

import java.time.OffsetDateTime

import com.wegtam.amws.common.Request.{ ParameterValue, RequestParameters }
import com.wegtam.amws.common.{ Action, MarketPlace }

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

    /**
      * Create the request parameters needed for the API action.
      *
      * @param reportType   The type of the report that shall be created.
      * @param startDate    The start of a date range used for selecting the data to report. Must be prior to or equal to the current time.
      * @param endDate      The end of a date range used for selecting the data to report. Must be prior to or equal to the current time.
      * @param marketplaces A list of one or more marketplace IDs for the marketplaces you are registered to sell in. The resulting report will include information for all marketplaces you specify.
      * @return The request parameters needed to call the API action.
      */
    def buildRequestParameters(
        reportType: ReportType,
        startDate: Option[OffsetDateTime],
        endDate: Option[OffsetDateTime],
        marketplaces: Seq[MarketPlace]
    ): RequestParameters = {
      val ps = Map(
        "Action"     -> toParameterValue,
        "ReportType" -> reportType.toParameterValue
      ) ++ marketplaces.zipWithIndex.map(
        t => s"MarketplaceIdList.Id.${t._2 + 1}" -> t._1.toParameterValue
      )
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

    /**
      * Create the request parameters needed for the API action.
      *
      * @param marketPlace The marketplace of the requested report.
      * @param requestIds  A structured list of ReportRequestId values. '''If you pass in ReportRequestId values, other query conditions are ignored.'''
      * @param reportTypes A structured list of ReportType enumeration values.
      * @param processing  A structured list of report processing statuses by which to filter report requests.
      * @param max         A non-negative integer that represents the maximum number of report requests to return. If you specify a number greater than 100, the request is rejected.
      * @param from        The start of the date range used for selecting the data to report.
      * @param to          The end of the date range used for selecting the data to report.
      * @return The request parameters needed for the API action.
      */
    @SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
    def buildRequestParameters(
        marketPlace: MarketPlace,
        requestIds: Seq[String],
        reportTypes: Seq[ReportType] = Seq.empty,
        processing: Seq[ReportProcessingStatus] = Seq.empty,
        max: Int = DEFAULT_PAGE_SIZE,
        from: Option[OffsetDateTime] = None,
        to: Option[OffsetDateTime] = None
    ): RequestParameters = {
      val r: RequestParameters = Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> marketPlace.toParameterValue
      )
      if (requestIds.isEmpty) {
        val r1: RequestParameters = r ++ reportTypes.zipWithIndex.map(
          t => s"ReportTypeList.Type.${t._2 + 1}" -> t._1.toParameterValue
        ) ++
        processing.zipWithIndex.map(
          t => s"ReportProcessingStatusList.Status.${t._2 + 1}" -> t._1.toParameterValue
        ) ++
        Map("MaxCount" -> max.toString)
        val r2: RequestParameters = from.fold(r1)(d => r1 + ("RequestedFromDate" -> d.toString))
        to.fold(r2)(d => r2 + ("RequestedToDate" -> d.toString))
      } else
        r ++ requestIds.zipWithIndex.map(t => s"ReportRequestIdList.Id.${t._2 + 1}" -> t._1)
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

    /**
      * Create the request parameters needed for the API action.
      *
      * @param marketPlace The marketplace of the requested report.
      * @param nextToken   A string token returned in a previous call.
      * @return The request parameters needed for the API action.
      */
    def buildRequestParameters(marketPlace: MarketPlace, nextToken: String): RequestParameters =
      Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> marketPlace.toParameterValue,
        "NextToken"   -> nextToken
      )

    override def toParameterValue: ParameterValue =
      "GetReportRequestListByNextToken"
  }

  /**
    * Returns a count of report requests that have been submitted to
    * Amazon MWS for processing.
    */
  case object GetReportRequestCount extends Action {

    /**
      * Create the request parameters needed for the API action.
      *
      * @param marketPlace The marketplace of the requested report.
      * @param reportTypes A structured list of ReportType enumeration values.
      * @param processing  A structured list of report processing statuses by which to filter report requests.
      * @param from        The start of the date range used for selecting the data to report.
      * @param to          The end of the date range used for selecting the data to report.
      * @return The request parameters needed for the API action.
      */
    @SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
    def buildRequestParameters(
        marketPlace: MarketPlace,
        reportTypes: Seq[ReportType] = Seq.empty,
        processing: Seq[ReportProcessingStatus] = Seq.empty,
        from: Option[OffsetDateTime] = None,
        to: Option[OffsetDateTime] = None
    ): RequestParameters = {
      val r: RequestParameters = Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> marketPlace.toParameterValue
      )
      val r1: RequestParameters = r ++ reportTypes.zipWithIndex.map(
        t => s"ReportTypeList.Type.${t._2 + 1}" -> t._1.toParameterValue
      ) ++
      processing.zipWithIndex.map(
        t => s"ReportProcessingStatusList.Status.${t._2 + 1}" -> t._1.toParameterValue
      )
      val r2: RequestParameters = from.fold(r1)(d => r1 + ("RequestedFromDate" -> d.toString))
      to.fold(r2)(d => r2 + ("RequestedToDate" -> d.toString))
    }

    override def toParameterValue: ParameterValue = "GetReportRequestCount"
  }

  /**
    * Cancels one or more report requests.
    */
  case object CancelReportRequests extends Action {

    /**
      * Create the request parameters needed for the API action.
      *
      * @param marketPlace The marketplace of the requested report.
      * @param requestIds  A structured list of ReportRequestId values. '''If you pass in ReportRequestId values, other query conditions are ignored.'''
      * @param reportTypes A structured list of ReportType enumeration values.
      * @param processing  A structured list of report processing statuses by which to filter report requests.
      * @param from        The start of the date range used for selecting the data to report.
      * @param to          The end of the date range used for selecting the data to report.
      * @return The request parameters needed for the API action.
      */
    @SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
    def buildRequestParameters(
        marketPlace: MarketPlace,
        requestIds: Seq[String],
        reportTypes: Seq[ReportType] = Seq.empty,
        processing: Seq[ReportProcessingStatus] = Seq.empty,
        from: Option[OffsetDateTime] = None,
        to: Option[OffsetDateTime] = None
    ): RequestParameters = {
      val r: RequestParameters = Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> marketPlace.toParameterValue
      )
      if (requestIds.isEmpty) {
        val r1: RequestParameters = r ++ reportTypes.zipWithIndex.map(
          t => s"ReportTypeList.Type.${t._2 + 1}" -> t._1.toParameterValue
        ) ++
        processing.zipWithIndex.map(
          t => s"ReportProcessingStatusList.Status.${t._2 + 1}" -> t._1.toParameterValue
        )
        val r2: RequestParameters = from.fold(r1)(d => r1 + ("RequestedFromDate" -> d.toString))
        to.fold(r2)(d => r2 + ("RequestedToDate" -> d.toString))
      } else
        r ++ requestIds.zipWithIndex.map(t => s"ReportRequestIdList.Id.${t._2 + 1}" -> t._1)
    }

    override def toParameterValue: ParameterValue = "CancelReportRequests"
  }

  /**
    * Returns a list of reports that were created in the previous 90 days.
    */
  case object GetReportList extends Action {
    final val DEFAULT_PAGE_SIZE = 50

    /**
      * Create the request parameters needed for the API action.
      *
      * @todo Add support for `Acknowledged` parameter.
      *
      * @param marketPlace The marketplace of the requested report.
      * @param requestIds  A structured list of ReportRequestId values. '''If you pass in ReportRequestId values, other query conditions are ignored.'''
      * @param reportTypes A structured list of ReportType enumeration values.
      * @param processing  A structured list of report processing statuses by which to filter report requests.
      * @param max         A non-negative integer that represents the maximum number of report requests to return. If you specify a number greater than 100, the request is rejected.
      * @param from        The earliest date you are looking for.
      * @param to          The most recent date you are looking for.
      * @return The request parameters needed for the API action.
      */
    @SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
    def buildRequestParameters(
        marketPlace: MarketPlace,
        requestIds: Seq[String],
        reportTypes: Seq[ReportType] = Seq.empty,
        processing: Seq[ReportProcessingStatus] = Seq.empty,
        max: Int = DEFAULT_PAGE_SIZE,
        from: Option[OffsetDateTime] = None,
        to: Option[OffsetDateTime] = None
    ): RequestParameters = {
      val r: RequestParameters = Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> marketPlace.toParameterValue
      )
      if (requestIds.isEmpty) {
        val r1: RequestParameters = r ++ reportTypes.zipWithIndex.map(
          t => s"ReportTypeList.Type.${t._2 + 1}" -> t._1.toParameterValue
        ) ++
        processing.zipWithIndex.map(
          t => s"ReportProcessingStatusList.Status.${t._2 + 1}" -> t._1.toParameterValue
        ) ++
        Map("MaxCount" -> max.toString)
        val r2: RequestParameters = from.fold(r1)(d => r1 + ("AvailableFromDate" -> d.toString))
        to.fold(r2)(d => r2 + ("AvailableToDate" -> d.toString))
      } else
        r ++ requestIds.zipWithIndex.map(t => s"ReportRequestIdList.Id.${t._2 + 1}" -> t._1)
    }

    override def toParameterValue: ParameterValue = "GetReportList"
  }

  /**
    * Returns a list of reports using the `NextToken`, which was supplied
    * by a previous request to either `GetReportListByNextToken` or
    * `GetReportList`, where the value of `HasNext` was true in the
    * previous call.
    */
  case object GetReportListByNextToken extends Action {

    /**
      * Create the request parameters needed for the API action.
      *
      * @param marketPlace The marketplace of the requested report.
      * @param nextToken   A string token returned in a previous call.
      * @return The request parameters needed for the API action.
      */
    def buildRequestParameters(marketPlace: MarketPlace, nextToken: String): RequestParameters =
      Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> marketPlace.toParameterValue,
        "NextToken"   -> nextToken
      )

    override def toParameterValue: ParameterValue = "GetReportListByNextToken"
  }

  /**
    * Returns a count of the reports, created in the previous 90 days,
    * with a status of `_DONE_` and that are available for download.
    */
  case object GetReportCount extends Action {

    /**
      * Create the request parameters needed for the API action.
      *
      * @param marketPlace The marketplace of the requested report.
      * @param reportTypes A structured list of ReportType enumeration values.
      * @param from        The earliest date you are looking for.
      * @param to          The most recent date you are looking for.
      * @return The request parameters needed for the API action.
      */
    @SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
    def buildRequestParameters(
        marketPlace: MarketPlace,
        reportTypes: Seq[ReportType] = Seq.empty,
        from: Option[OffsetDateTime] = None,
        to: Option[OffsetDateTime] = None
    ): RequestParameters = {
      val r: RequestParameters = Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> marketPlace.toParameterValue
      )
      val r1: RequestParameters = r ++ reportTypes.zipWithIndex.map(
        t => s"ReportTypeList.Type.${t._2 + 1}" -> t._1.toParameterValue
      )
      val r2: RequestParameters = from.fold(r1)(d => r1 + ("AvailableFromDate" -> d.toString))
      to.fold(r2)(d => r2 + ("AvailableToDate" -> d.toString))
    }

    override def toParameterValue: ParameterValue = "GetReportCount"
  }

  /**
    * Returns the contents of a report and the Content-MD5 header for the
    * returned report body.
    */
  case object GetReport extends Action {

    /**
      * Create the request parameters needed for the API action.
      *
      * @param marketPlace The marketplace of the requested report.
      * @param reportId    A unique identifier of the report to download, obtained from the GetReportList operation or the GeneratedReportId of a ReportRequest.
      * @return The request parameters needed for the API action.
      */
    def buildRequestParameters(
        marketPlace: MarketPlace,
        reportId: String
    ): RequestParameters =
      Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> marketPlace.toParameterValue,
        "ReportId"    -> reportId
      )

    override def toParameterValue: ParameterValue = "GetReport"
  }

  /**
    * Creates, updates, or deletes a report request schedule for a
    * specified report type.
    */
  case object ManageReportSchedule extends Action {

    /**
      * Create the request parameters needed for the API action.
      *
      * @param marketPlace  The marketplace of the report.
      * @param reportType   A value of the ReportType that indicates the type of report to request.
      * @param scheduleType A value of the ScheduleType that indicates how often a report request should be created.
      * @param scheduleDate The date when the next report request is scheduled to be submitted.
      * @return The request parameters needed for the API action.
      */
    @SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
    def buildRequestParameters(
        marketPlace: MarketPlace,
        reportType: ReportType,
        scheduleType: ScheduleType,
        scheduleDate: Option[OffsetDateTime] = None
    ): RequestParameters = {
      val r: RequestParameters = Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> marketPlace.toParameterValue,
        "ReportType"  -> reportType.toParameterValue,
        "Schedule"    -> scheduleType.toParameterValue
      )
      scheduleDate.fold(r)(d => r + ("ScheduleDate" -> d.toString))
    }

    override def toParameterValue: ParameterValue = "ManageReportSchedule"
  }

  /**
    * Returns a list of order report requests that are scheduled to be
    * submitted to Amazon MWS for processing.
    */
  case object GetReportScheduleList extends Action {

    /**
      * Create the request parameters needed for the API action.
      *
      * @param marketPlace The marketplace of the requested report.
      * @param reportTypes A structured list of ReportType enumeration values.
      * @return The request parameters needed for the API action.
      */
    @SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
    def buildRequestParameters(
        marketPlace: MarketPlace,
        reportTypes: Seq[ReportType] = Seq.empty
    ): RequestParameters = {
      val r: RequestParameters = Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> marketPlace.toParameterValue
      )
      r ++ reportTypes.zipWithIndex.map(
        t => s"ReportTypeList.Type.${t._2 + 1}" -> t._1.toParameterValue
      )
    }

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

    /**
      * Create the request parameters needed for the API action.
      *
      * @param marketPlace The marketplace of the requested report.
      * @param reportTypes A structured list of ReportType enumeration values.
      * @return The request parameters needed for the API action.
      */
    @SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
    def buildRequestParameters(
        marketPlace: MarketPlace,
        reportTypes: Seq[ReportType] = Seq.empty
    ): RequestParameters = {
      val r: RequestParameters = Map(
        "Action"      -> toParameterValue,
        "Marketplace" -> marketPlace.toParameterValue
      )
      r ++ reportTypes.zipWithIndex.map(
        t => s"ReportTypeList.Type.${t._2 + 1}" -> t._1.toParameterValue
      )
    }

    override def toParameterValue: ParameterValue = "GetReportScheduleCount"
  }

  /**
    * Updates the acknowledged status of one or more reports.
    */
  case object UpdateReportAcknowledgements extends Action {

    /**
      * Create the request parameters needed for the API action.
      * @param marketPlace  The marketplace of the reports.
      * @param reportIds    A structured list of Report Ids. The maximum number of reports that can be specified is 100.
      * @param acknowledged A Boolean value that indicates that you have received and stored a report. Specify true to set the acknowledged status of a report to true. Specify false to set the acknowledged status of a report to false.
      * @return The request parameters needed for the API action.
      */
    @throws[IllegalArgumentException](
      cause = "The maximum number of reports that can be specified is 100."
    )
    def buildRequestParameters(
        marketPlace: MarketPlace,
        reportIds: Seq[String],
        acknowledged: Boolean
    ): RequestParameters = {
      require(reportIds.length <= 100,
              "The maximum number of reports that can be specified is 100.")
      Map(
        "Action"       -> toParameterValue,
        "Marketplace"  -> marketPlace.toParameterValue,
        "Acknowledged" -> acknowledged.toString
      ) ++ reportIds.zipWithIndex.map(t => s"ReportIdList.Id.${t._2 + 1}" -> t._1)
    }

    override def toParameterValue: ParameterValue =
      "UpdateReportAcknowledgements"
  }

}
