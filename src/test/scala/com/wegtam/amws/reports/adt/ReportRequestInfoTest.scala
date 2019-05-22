/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.reports.adt

import java.time.OffsetDateTime

import com.wegtam.amws.reports._
import org.scalatest.{ MustMatchers, WordSpec }

class ReportRequestInfoTest extends WordSpec with MustMatchers {

  "fromXmlString" when {
    "given valid xml" must {
      "return the correct ReportRequestInfo" in {
        val xml =
          s"""
             |<ReportRequestInfo>
             |    <ReportRequestId>2291326454</ReportRequestId>
             |    <ReportType>_GET_MERCHANT_LISTINGS_DATA_</ReportType>
             |    <StartDate>2011-01-21T02:10:39+00:00</StartDate>
             |    <EndDate>2011-02-13T02:10:39+00:00</EndDate>
             |    <Scheduled>false</Scheduled>
             |    <SubmittedDate>2011-02-17T23:44:09+00:00</SubmittedDate>
             |    <ReportProcessingStatus>_DONE_</ReportProcessingStatus>
             |    <GeneratedReportId>3538561173</GeneratedReportId>
             |    <StartedProcessingDate>
             |        2011-02-17T23:44:43+00:00
             |    </StartedProcessingDate>
             |    <CompletedDate>2011-02-17T23:44:48+00:00</CompletedDate>
             |</ReportRequestInfo>
           """.stripMargin
        val expected = ReportRequestInfo(
          id = "2291326454",
          reportType = ListingReports.ActiveListings,
          startDate = OffsetDateTime.parse("2011-01-21T02:10:39+00:00"),
          endDate = OffsetDateTime.parse("2011-02-13T02:10:39+00:00"),
          scheduled = false,
          submittedDate = OffsetDateTime.parse("2011-02-17T23:44:09+00:00"),
          status = ReportProcessingStatus.Done,
          generatedReportId = Option("3538561173"),
          startedProcessingDate = Option(OffsetDateTime.parse("2011-02-17T23:44:43+00:00")),
          completedDate = Option(OffsetDateTime.parse("2011-02-17T23:44:48+00:00"))
        )
        ReportRequestInfo.fromXmlString(xml) must contain(expected)
      }
    }
  }

  "fromXmlGetReportRequestListResponse" when {
    "given valid xml" must {
      "return the correct list" in {
        val xml =
          s"""
             |<GetReportRequestListResponse
             |    xmlns="http://mws.amazonservices.com/doc/2009-01-01/">
             |    <GetReportRequestListResult>
             |        <NextToken>2YgYW55IPQhcm5hbCBwbGVhc3VyZS4=</NextToken>
             |        <HasNext>true</HasNext>
             |        <ReportRequestInfo>
             |            <ReportRequestId>2291326454</ReportRequestId>
             |            <ReportType>_GET_MERCHANT_LISTINGS_DATA_</ReportType>
             |            <StartDate>2011-01-21T02:10:39+00:00</StartDate>
             |            <EndDate>2011-02-13T02:10:39+00:00</EndDate>
             |            <Scheduled>false</Scheduled>
             |            <SubmittedDate>2011-02-17T23:44:09+00:00</SubmittedDate>
             |            <ReportProcessingStatus>_DONE_</ReportProcessingStatus>
             |            <GeneratedReportId>3538561173</GeneratedReportId>
             |            <StartedProcessingDate>
             |                2011-02-17T23:44:43+00:00
             |            </StartedProcessingDate>
             |            <CompletedDate>2011-02-17T23:44:48+00:00</CompletedDate>
             |        </ReportRequestInfo>
             |        <ReportRequestInfo>
             |            <ReportRequestId>2291326455</ReportRequestId>
             |            <ReportType>_GET_FLAT_FILE_ALL_ORDERS_DATA_BY_LAST_UPDATE_</ReportType>
             |            <StartDate>2011-01-21T02:10:39+00:00</StartDate>
             |            <EndDate>2011-02-13T02:10:39+00:00</EndDate>
             |            <Scheduled>true</Scheduled>
             |            <SubmittedDate>2011-02-17T23:44:09+00:00</SubmittedDate>
             |            <ReportProcessingStatus>_SUBMITTED_</ReportProcessingStatus>
             |            <GeneratedReportId>3538561174</GeneratedReportId>
             |        </ReportRequestInfo>
             |        <ReportRequestInfo>
             |            <ReportRequestId>2291326456</ReportRequestId>
             |            <ReportType>_GET_FLAT_FILE_OPEN_LISTINGS_DATA_</ReportType>
             |            <StartDate>2011-01-21T02:10:39+00:00</StartDate>
             |            <EndDate>2011-02-13T02:10:39+00:00</EndDate>
             |            <Scheduled>false</Scheduled>
             |            <SubmittedDate>2011-02-17T23:44:11+00:00</SubmittedDate>
             |            <ReportProcessingStatus>_IN_PROGRESS_</ReportProcessingStatus>
             |            <GeneratedReportId>3538561175</GeneratedReportId>
             |            <StartedProcessingDate>2011-02-17T23:44:45+00:00</StartedProcessingDate>
             |        </ReportRequestInfo>
             |    </GetReportRequestListResult>
             |    <ResponseMetadata>
             |        <RequestId>732480cb-84a8-4c15-9084-a46bd9a0889b</RequestId>
             |    </ResponseMetadata>
             |</GetReportRequestListResponse>
           """.stripMargin
        val expected: Seq[ReportRequestInfo] = Seq(
          ReportRequestInfo(
            id = "2291326454",
            reportType = ListingReports.ActiveListings,
            startDate = OffsetDateTime.parse("2011-01-21T02:10:39+00:00"),
            endDate = OffsetDateTime.parse("2011-02-13T02:10:39+00:00"),
            scheduled = false,
            submittedDate = OffsetDateTime.parse("2011-02-17T23:44:09+00:00"),
            status = ReportProcessingStatus.Done,
            generatedReportId = Option("3538561173"),
            startedProcessingDate = Option(OffsetDateTime.parse("2011-02-17T23:44:43+00:00")),
            completedDate = Option(OffsetDateTime.parse("2011-02-17T23:44:48+00:00"))
          ),
          ReportRequestInfo(
            id = "2291326455",
            reportType = FBASales.FlatFileAllOrdersByLastUpdate,
            startDate = OffsetDateTime.parse("2011-01-21T02:10:39+00:00"),
            endDate = OffsetDateTime.parse("2011-02-13T02:10:39+00:00"),
            scheduled = true,
            submittedDate = OffsetDateTime.parse("2011-02-17T23:44:09+00:00"),
            status = ReportProcessingStatus.Submitted,
            generatedReportId = Option("3538561174"),
            startedProcessingDate = None,
            completedDate = None
          ),
          ReportRequestInfo(
            id = "2291326456",
            reportType = ListingReports.Inventory,
            startDate = OffsetDateTime.parse("2011-01-21T02:10:39+00:00"),
            endDate = OffsetDateTime.parse("2011-02-13T02:10:39+00:00"),
            scheduled = false,
            submittedDate = OffsetDateTime.parse("2011-02-17T23:44:11+00:00"),
            status = ReportProcessingStatus.InProgress,
            generatedReportId = Option("3538561175"),
            startedProcessingDate = Option(OffsetDateTime.parse("2011-02-17T23:44:45+00:00")),
            completedDate = None
          )
        )
        ReportRequestInfo
          .fromXmlGetReportRequestListResponse(xml)
          .toList
          .sorted mustEqual expected.sorted
      }
    }
  }

  "fromXmlRequestReportResponse" when {
    "given valid xml" must {
      "return the correct ReportRequestInfo" in {
        val xml =
          """
            |<RequestReportResponse
            |    xmlns="http://mws.amazonaws.com/doc/2009-01-01/">
            |    <RequestReportResult>
            |        <ReportRequestInfo>
            |            <ReportRequestId>2291326454</ReportRequestId>
            |            <ReportType>_GET_MERCHANT_LISTINGS_DATA_</ReportType>
            |            <StartDate>2009-01-21T02:10:39+00:00</StartDate>
            |            <EndDate>2009-02-13T02:10:39+00:00</EndDate>
            |            <Scheduled>false</Scheduled>
            |            <SubmittedDate>2009-02-20T02:10:39+00:00</SubmittedDate>
            |            <ReportProcessingStatus>_SUBMITTED_</ReportProcessingStatus>
            |        </ReportRequestInfo>
            |    </RequestReportResult>
            |    <ResponseMetadata>
            |        <RequestId>88faca76-b600-46d2-b53c-0c8c4533e43a</RequestId>
            |    </ResponseMetadata>
            |</RequestReportResponse>
          """.stripMargin
        val expected = ReportRequestInfo(
          id = "2291326454",
          reportType = ListingReports.ActiveListings,
          startDate = OffsetDateTime.parse("2009-01-21T02:10:39+00:00"),
          endDate = OffsetDateTime.parse("2009-02-13T02:10:39+00:00"),
          scheduled = false,
          submittedDate = OffsetDateTime.parse("2009-02-20T02:10:39+00:00"),
          status = ReportProcessingStatus.Submitted,
          generatedReportId = None,
          startedProcessingDate = None,
          completedDate = None
        )
        ReportRequestInfo.fromXmlRequestReportResponse(xml) must contain(expected)
      }
    }
  }

}
