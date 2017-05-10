/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.reports

import com.wegtam.amws.common.Request.ParameterValue

/**
  * A report has a processing status.
  */
sealed trait ReportProcessingStatus {

  /**
    * The parameter value of the processing status that can be used
    * in a query and is also the value returned in responses.
    *
    * @return A string representation of the processing status.
    */
  def toParameterValue: ParameterValue

}

object ReportProcessingStatus {

  /**
    * The report has been submitted but no further processing has been done.
    */
  case object Submitted extends ReportProcessingStatus {
    override def toParameterValue: ParameterValue = "_SUBMITTED_"
  }

  /**
    * The report is currently being processed.
    */
  case object InProgress extends ReportProcessingStatus {
    override def toParameterValue: ParameterValue = "_IN_PROGRESS_"
  }

  /**
    * The processing of the report has been cancelled.
    */
  case object Cancelled extends ReportProcessingStatus {
    override def toParameterValue: ParameterValue = "_CANCELLED_"
  }

  /**
    * The processing is done and the report is ready to be fetched.
    */
  case object Done extends ReportProcessingStatus {
    override def toParameterValue: ParameterValue = "_DONE_"
  }

  /**
    * The processing is done but the report contains no data.
    */
  case object DoneNoData extends ReportProcessingStatus {
    override def toParameterValue: ParameterValue = "_DONE_NO_DATA_"
  }

}
