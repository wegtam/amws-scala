package com.wegtam.amws.reports.adt

import java.time.OffsetDateTime

import com.wegtam.amws.reports.ListingReports
import org.scalatest.{ MustMatchers, WordSpec }

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

}
