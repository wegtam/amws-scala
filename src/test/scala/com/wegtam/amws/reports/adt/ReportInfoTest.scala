/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.reports.adt

import java.time.OffsetDateTime

import com.wegtam.amws.reports.ListingReports
import org.scalatest.{ MustMatchers, WordSpec }

import scala.collection.immutable.Seq

class ReportInfoTest extends WordSpec with MustMatchers {

  "fromXmlString" when {
    "given valid xml" must {
      "return the correct ReportInfo" in {
        val expected = ReportInfo(
          id = "898899473",
          reportType = ListingReports.ActiveListings,
          requestId = "2278662938",
          availableDate = OffsetDateTime.parse("2009-02-10T09:22:33+00:00"),
          acknowledged = false,
          acknowledgedDate = None
        )
        val xml =
          s"""
            |<ReportInfo>
            |  <ReportId>898899473</ReportId>
            |  <ReportType>_GET_MERCHANT_LISTINGS_DATA_</ReportType>
            |  <ReportRequestId>2278662938</ReportRequestId>
            |  <AvailableDate>2009-02-10T09:22:33+00:00</AvailableDate>
            |  <Acknowledged>false</Acknowledged>
            |</ReportInfo>
          """.stripMargin
        ReportInfo.fromXmlString(xml) must contain(expected)
      }
    }
  }

  "fromXmlGetReportListResponse" when {
    "given valid xml" must {
      "return the correct list" in {
        val xml =
          s"""
             |<GetReportListResponse
             |    xmlns="http://mws.amazonservices.com/doc/2009-01-01/">
             |    <GetReportListResult>
             |        <NextToken>2YgYW55IPQhvu5hbCBwbGVhc3VyZS4=</NextToken>
             |        <HasNext>true</HasNext>
             |        <ReportInfo>
             |            <ReportId>898899473</ReportId>
             |            <ReportType>_GET_MERCHANT_LISTINGS_DATA_</ReportType>
             |            <ReportRequestId>2278662938</ReportRequestId>
             |            <AvailableDate>2009-02-10T09:22:33+00:00</AvailableDate>
             |            <Acknowledged>true</Acknowledged>
             |            <AcknowledgedDate>2009-02-11T09:12:00+00:00</AcknowledgedDate>
             |        </ReportInfo>
             |        <ReportInfo>
             |            <ReportId>898899474</ReportId>
             |            <ReportType>_GET_MERCHANT_LISTINGS_DATA_</ReportType>
             |            <ReportRequestId>2278662939</ReportRequestId>
             |            <AvailableDate>2009-02-10T09:22:35+00:00</AvailableDate>
             |            <Acknowledged>false</Acknowledged>
             |        </ReportInfo>
             |        <ReportInfo>
             |            <ReportId>898899475</ReportId>
             |            <ReportType>_GET_MERCHANT_LISTINGS_DATA_</ReportType>
             |            <ReportRequestId>2278662940</ReportRequestId>
             |            <AvailableDate>2009-02-10T09:22:37+00:00</AvailableDate>
             |            <Acknowledged>false</Acknowledged>
             |        </ReportInfo>
             |    </GetReportListResult>
             |    <ResponseMetadata>
             |        <RequestId>fbf677c1-dcee-4110-bc88-2ba3702e331b</RequestId>
             |    </ResponseMetadata>
             |</GetReportListResponse>
           """.stripMargin
        val expected: Seq[ReportInfo] = Seq(
          ReportInfo(
            id = "898899473",
            reportType = ListingReports.ActiveListings,
            requestId = "2278662938",
            availableDate = OffsetDateTime.parse("2009-02-10T09:22:33+00:00"),
            acknowledged = true,
            acknowledgedDate = Option(OffsetDateTime.parse("2009-02-11T09:12:00+00:00"))
          ),
          ReportInfo(
            id = "898899474",
            reportType = ListingReports.ActiveListings,
            requestId = "2278662939",
            availableDate = OffsetDateTime.parse("2009-02-10T09:22:35+00:00"),
            acknowledged = false,
            acknowledgedDate = None
          ),
          ReportInfo(
            id = "898899475",
            reportType = ListingReports.ActiveListings,
            requestId = "2278662940",
            availableDate = OffsetDateTime.parse("2009-02-10T09:22:37+00:00"),
            acknowledged = false,
            acknowledgedDate = None
          )
        )

        ReportInfo.fromXmlGetReportListResponse(xml) mustEqual expected
      }
    }
  }

}
