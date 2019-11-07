/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.reports.adt

import java.time._

import com.fortysevendeg.scalacheck.datetime.jdk8._
import com.wegtam.amws.reports._
import org.scalacheck.{ Arbitrary, Gen }

/**
  * Provide generators for ScalaCheck.
  */
object ReportRequestInfoGenerators {
  val genReportProcessingStatus: Gen[ReportProcessingStatus] = for {
    s <- Gen.oneOf(ReportProcessingStatus.ALL)
  } yield s

  implicit val arbitraryReportProcessingStatus: Arbitrary[ReportProcessingStatus] = Arbitrary(
    genReportProcessingStatus
  )

  val genReportType: Gen[ReportType] = for {
    t <- Gen.oneOf(ReportType.ALL)
  } yield t

  implicit val arbitraryReportType: Arbitrary[ReportType] = Arbitrary(genReportType)

  val genOffsetDateTime: Gen[OffsetDateTime] = for {
    t <- GenJdk8.genZonedDateTime
  } yield t.toOffsetDateTime

  val genReportRequestInfo: Gen[ReportRequestInfo] = for {
    id     <- Gen.choose(0, Int.MaxValue)
    rt     <- genReportType
    sd     <- genOffsetDateTime
    ed     <- genOffsetDateTime
    sched  <- Gen.oneOf(List(false, true))
    subd   <- genOffsetDateTime
    status <- genReportProcessingStatus
    gid    <- Gen.option(Gen.choose(0, Int.MaxValue))
    spd    <- Gen.option(genOffsetDateTime)
    cd     <- Gen.option(genOffsetDateTime)
  } yield ReportRequestInfo(
    id = id.toString,
    reportType = rt,
    startDate = sd,
    endDate = ed,
    scheduled = sched,
    submittedDate = subd,
    status = status,
    generatedReportId = gid.map(_.toString),
    startedProcessingDate = spd,
    completedDate = cd
  )

  implicit val arbitraryReportRequestInfo: Arbitrary[ReportRequestInfo] = Arbitrary(
    genReportRequestInfo
  )

  val genReportRequestInfoList: Gen[List[ReportRequestInfo]] = for {
    rs <- Gen.nonEmptyListOf(genReportRequestInfo)
  } yield rs

  implicit val arbitraryReportRequestInfoList: Arbitrary[List[ReportRequestInfo]] = Arbitrary(
    genReportRequestInfoList
  )
}
