/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.reports.adt

import com.wegtam.amws.reports.adt.ReportRequestInfoGenerators._
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ReportRequestInfoTest extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks {
  private def toXmlString(r: ReportRequestInfo): String =
    s"""<ReportRequestInfo><ReportRequestId>${r.id}</ReportRequestId><ReportType>${r.reportType.toParameterValue}</ReportType><StartDate>${r.startDate.toString}</StartDate><EndDate>${r.endDate.toString}</EndDate><Scheduled>${r.scheduled.toString}</Scheduled><SubmittedDate>${r.submittedDate.toString}</SubmittedDate><ReportProcessingStatus>${r.status.toParameterValue}</ReportProcessingStatus>${r.generatedReportId
      .map(d => "<GeneratedReportId>" + d.toString + "</GeneratedReportId>")
      .getOrElse("")}${r.startedProcessingDate
      .map(d => "<StartedProcessingDate>" + d.toString + "</StartedProcessingDate>")
      .getOrElse("")}${r.completedDate
      .map(d => "<CompletedDate>" + d.toString + "</CompletedDate>")
      .getOrElse("")}</ReportRequestInfo>"""

  "fromXmlString" when {
    "given invalid xml" must {
      "return an empty option" in {
        forAll("input") { xml: String =>
          ReportRequestInfo.fromXmlString(xml) must be(empty)
        }
      }
    }

    "given valid xml" must {
      "return the correct ReportRequestInfo" in {
        forAll("input") { r: ReportRequestInfo =>
          val xml = toXmlString(r)
          withClue(s"Test failed while parsing xml:\n$xml\n") {
            ReportRequestInfo.fromXmlString(xml) must contain(r)
          }
        }
      }
    }
  }

  "fromXmlGetReportRequestListResponse" when {
    "given invalid xml" must {
      "return an empty list" in {
        forAll("input") { xml: String =>
          ReportRequestInfo.fromXmlGetReportRequestListResponse(xml) must be(empty)
        }
      }
    }

    "given valid xml" must {
      "return the correct list" in {
        forAll("input") { rs: List[ReportRequestInfo] =>
          val xml =
            s"""<GetReportRequestListResponse xmlns="http://mws.amazonservices.com/doc/2009-01-01/"><GetReportRequestListResult>${rs
              .map(toXmlString)
              .mkString}</GetReportRequestListResult></GetReportRequestListResponse>"""
          withClue(s"Test failed while parsing xml:\n$xml\n") {
            ReportRequestInfo
              .fromXmlGetReportRequestListResponse(xml)
              .toList
              .sorted mustEqual rs.sorted
          }
        }
      }
    }
  }

  "fromXmlRequestReportResponse" when {
    "given invalid xml" must {
      "return an empty option" in {
        forAll("input") { xml: String =>
          ReportRequestInfo.fromXmlRequestReportResponse(xml) must be(empty)
        }
      }
    }

    "given valid xml" must {
      "return the correct ReportRequestInfo" in {
        forAll("input") { r: ReportRequestInfo =>
          val xml =
            s"""<RequestReportResponse xmlns="http://mws.amazonaws.com/doc/2009-01-01/"><RequestReportResult>${toXmlString(
              r
            )}</RequestReportResult></RequestReportResponse>"""
          withClue(s"Test failed while parsing xml:\n$xml\n") {
            ReportRequestInfo.fromXmlRequestReportResponse(xml) must contain(r)
          }
        }
      }
    }
  }
}
