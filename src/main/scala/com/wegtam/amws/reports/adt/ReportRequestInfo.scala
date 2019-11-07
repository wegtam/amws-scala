/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.reports.adt

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime

import com.wegtam.amws.reports.{ ReportProcessingStatus, ReportType }
import eu.cdevreeze.yaidom.parse.DocumentParserUsingSax
import eu.cdevreeze.yaidom.queryapi.ClarkElemApi._
import eu.cdevreeze.yaidom.simple.Elem

import scala.util.Try

/**
  * A wrapper for the ReportRequestInfo response.
  *
  * @param id                     A unique identifier for the report request.
  * @param reportType             The ReportType value requested.
  * @param startDate              The start of a date range used for selecting the data to report.
  * @param endDate                The end of a date range used for selecting the data to report.
  * @param scheduled              A Boolean value that indicates if a report is scheduled. The value is `true` if the report was scheduled; otherwise `false`.
  * @param submittedDate          The date when the report was submitted.
  * @param status                 The processing status of the report.
  * @param generatedReportId      The report identifier used to retrieve the report.
  * @param startedProcessingDate  The date when the report processing started.
  * @param completedDate          The date when the report processing completed.
  */
final case class ReportRequestInfo(
    id: String,
    reportType: ReportType,
    startDate: OffsetDateTime,
    endDate: OffsetDateTime,
    scheduled: Boolean,
    submittedDate: OffsetDateTime,
    status: ReportProcessingStatus,
    generatedReportId: Option[String],
    startedProcessingDate: Option[OffsetDateTime],
    completedDate: Option[OffsetDateTime]
)

object ReportRequestInfo {
  implicit val ordering: Ordering[ReportRequestInfo] =
    (x: ReportRequestInfo, y: ReportRequestInfo) => x.submittedDate.compareTo(y.submittedDate)

  /**
    * Extract a [[ReportRequestInfo]] from a given xml element.
    *
    * @param e An xml element containing the desired information.
    * @return An option to the report request information.
    */
  def fromXmlElement(e: Elem): Option[ReportRequestInfo] =
    for {
      id <- e.findChildElem(withLocalName("ReportRequestId")).map(_.trimmedText)
      rt <- ReportType.fromParameterValue(
        e.findChildElem(withLocalName("ReportType")).map(_.trimmedText).getOrElse("")
      )
      sd <- e.findChildElem(withLocalName("StartDate")).map(_.trimmedText)
      ed <- e.findChildElem(withLocalName("EndDate")).map(_.trimmedText)
      sc <- e.findChildElem(withLocalName("Scheduled")).map(_.trimmedText)
      rd <- e.findChildElem(withLocalName("SubmittedDate")).map(_.trimmedText)
      st <- ReportProcessingStatus.fromParameterValue(
        e.findChildElem(withLocalName("ReportProcessingStatus")).map(_.trimmedText).getOrElse("")
      )
      rid = e.findChildElem(withLocalName("GeneratedReportId")).map(_.trimmedText)
      spd = e.findChildElem(withLocalName("StartedProcessingDate")).map(_.trimmedText)
      epd = e.findChildElem(withLocalName("CompletedDate")).map(_.trimmedText)
    } yield ReportRequestInfo(
      id = id,
      reportType = rt,
      startDate = OffsetDateTime.parse(sd),
      endDate = OffsetDateTime.parse(ed),
      scheduled = sc == "true",
      submittedDate = OffsetDateTime.parse(rd),
      status = st,
      generatedReportId = rid,
      startedProcessingDate = spd.map(t => OffsetDateTime.parse(t)),
      completedDate = epd.map(t => OffsetDateTime.parse(t))
    )

  /**
    * Try to extract the [[ReportRequestInfo]] informations from the given xml fragment.
    *
    * @param s A string containing the xml fragment with the desired information.
    * @return An option to the extracted report request information.
    */
  def fromXmlString(s: String): Option[ReportRequestInfo] =
    for {
      p <- Try(DocumentParserUsingSax.newInstance()).toOption
      d <- Try(p.parse(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)))).toOption
      e = d.documentElement // The document element is assumed to be "ReportRequestInfo"
      r <- fromXmlElement(e)
    } yield r

  /**
    * Parse the xml `GetReportRequestListResponse` and return a list of [[ReportRequestInfo]]
    * which may be empty.
    *
    * @param s A string containing the xml.
    * @return A list of report request informations.
    */
  def fromXmlGetReportRequestListResponse(s: String): Seq[ReportRequestInfo] = {
    val eo: Option[Elem] = for {
      p <- Try(DocumentParserUsingSax.newInstance()).toOption
      d <- Try(p.parse(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)))).toOption
      e <- d.documentElement.findChildElem(withLocalName("GetReportRequestListResult"))
    } yield e
    eo.fold(Seq.empty[ReportRequestInfo]) { e =>
      val it = for {
        c <- e.filterChildElems(withLocalName("ReportRequestInfo"))
        r <- fromXmlElement(c)
      } yield r
      it
    }
  }

  /**
    * Parse the xml `RequestReportResponse` and return the [[ReportRequestInfo]].
    *
    * @param s A string containing the xml.
    * @return An option to the report request information.
    */
  def fromXmlRequestReportResponse(s: String): Option[ReportRequestInfo] = {
    val eo: Option[Elem] = for {
      p <- Try(DocumentParserUsingSax.newInstance()).toOption
      d <- Try(p.parse(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)))).toOption
      e <- d.documentElement.findElem(withLocalName("ReportRequestInfo"))
    } yield e
    eo.flatMap(e => fromXmlElement(e))
  }
}
