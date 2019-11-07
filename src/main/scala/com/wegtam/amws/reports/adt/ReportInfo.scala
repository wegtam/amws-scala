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

import com.wegtam.amws.reports.ReportType
import eu.cdevreeze.yaidom.parse.DocumentParserUsingSax
import eu.cdevreeze.yaidom.queryapi.ClarkElemApi._
import eu.cdevreeze.yaidom.simple.Elem

import scala.collection.immutable.Seq
import scala.util.Try

/**
  * A wrapper for the ReportInfo response.
  *
  * @param id               A unique identifier for the report.
  * @param reportType       The ReportType value requested.
  * @param requestId        A unique identifier for the report request.
  * @param availableDate    The date the report is available.
  * @param acknowledged     A Boolean value that indicates if the report was acknowledged by this call to the `UpdateReportAcknowledgements` operation. The value is `true` if the report was acknowledged; otherwise `false`.
  * @param acknowledgedDate The date the report was acknowledged.
  */
final case class ReportInfo(
    id: String,
    reportType: ReportType,
    requestId: String,
    availableDate: OffsetDateTime,
    acknowledged: Boolean,
    acknowledgedDate: Option[OffsetDateTime]
)

object ReportInfo {
  /**
    * Extract a [[ReportInfo]] from the given xml element.
    *
    * @param e An xml element containing the desired information.
    * @return An option to the report information.
    */
  def fromXmlElement(e: Elem): Option[ReportInfo] =
    for {
      id <- e.findChildElem(withLocalName("ReportId")).map(_.trimmedText)
      rt <- ReportType.fromParameterValue(
        e.findChildElem(withLocalName("ReportType")).map(_.trimmedText).getOrElse("")
      )
      ri <- e.findChildElem(withLocalName("ReportRequestId")).map(_.trimmedText)
      ad <- e.findChildElem(withLocalName("AvailableDate")).map(_.trimmedText)
      ack = e.findChildElem(withLocalName("Acknowledged")).map(_.trimmedText).getOrElse("false")
      acd = e.findChildElem(withLocalName("AcknowledgedDate")).map(_.trimmedText)
    } yield ReportInfo(
      id = id,
      reportType = rt,
      requestId = ri,
      availableDate = OffsetDateTime.parse(ad),
      acknowledged = ack == "true",
      acknowledgedDate = acd.map(t => OffsetDateTime.parse(t))
    )

  /**
    * Try to extract the [[ReportInfo]] informations from the given xml fragment.
    *
    * @param s A string containing the xml fragment with the desired information.
    * @return An option to the extracted report information.
    */
  def fromXmlString(s: String): Option[ReportInfo] =
    for {
      p <- Try(DocumentParserUsingSax.newInstance()).toOption
      d <- Try(p.parse(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)))).toOption
      e = d.documentElement // The document element is assumed to be "ReportInfo"
      r <- fromXmlElement(e)
    } yield r

  /**
    * Parse the xml `GetReportListResponse` and return a list of [[ReportInfo]]
    * which may be empty.
    *
    * @param s A string containing the xml.
    * @return A list of report informations.
    */
  def fromXmlGetReportListResponse(s: String): Seq[ReportInfo] = {
    val eo: Option[Elem] = for {
      p <- Try(DocumentParserUsingSax.newInstance()).toOption
      d <- Try(p.parse(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)))).toOption
      e <- d.documentElement.findChildElem(withLocalName("GetReportListResult"))
    } yield e
    eo.fold(Seq.empty[ReportInfo]) { e =>
      val it = for {
        c <- e.filterChildElems(withLocalName("ReportInfo"))
        r <- fromXmlElement(c)
      } yield r
      it
    }
  }
}
