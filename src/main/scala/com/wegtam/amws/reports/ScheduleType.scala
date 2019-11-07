/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.reports

import com.wegtam.amws.EnumMacros
import com.wegtam.amws.common.ParameterValue

/**
  * The Schedule enumeration provides the units of time that indicate how
  * often a report request can be requested. For example, the
  * ManageReportSchedule operation uses the Schedule value to indicate how
  * often a report request is submitted.
  *
  * @see http://docs.developer.amazonservices.com/en_US/reports/Reports_Schedule.html
  */
sealed trait ScheduleType extends Product with Serializable {
  /**
    * The parameter value of the schedule type that must be used in the
    * query string.
    *
    * @return A string representation of the schedule type, usable in a query.
    */
  def toParameterValue: ParameterValue
}

object ScheduleType {
  /**
    * Delete a previously created report request schedule.
    */
  case object Never extends ScheduleType {
    override def toParameterValue: ParameterValue = "_NEVER_"
  }

  case object Every15Minutes extends ScheduleType {
    override def toParameterValue: ParameterValue = "_15_MINUTES_"
  }

  case object Every30Minutes extends ScheduleType {
    override def toParameterValue: ParameterValue = "_30_MINUTES_"
  }

  case object EveryHour extends ScheduleType {
    override def toParameterValue: ParameterValue = "_1_HOUR_"
  }

  case object Every2Hours extends ScheduleType {
    override def toParameterValue: ParameterValue = "_2_HOURS_"
  }

  case object Every4Hours extends ScheduleType {
    override def toParameterValue: ParameterValue = "_4_HOURS_"
  }

  case object Every8Hours extends ScheduleType {
    override def toParameterValue: ParameterValue = "_8_HOURS_"
  }

  case object Every12Hours extends ScheduleType {
    override def toParameterValue: ParameterValue = "_12_HOURS_"
  }

  case object EveryDay extends ScheduleType {
    override def toParameterValue: ParameterValue = "_1_DAY_"
  }

  case object Every2Days extends ScheduleType {
    override def toParameterValue: ParameterValue = "_2_DAYS_"
  }

  case object Every3Days extends ScheduleType {
    override def toParameterValue: ParameterValue = "_72_HOURS_"
  }

  case object EveryWeek extends ScheduleType {
    override def toParameterValue: ParameterValue = "_1_WEEK_"
  }

  case object Every14Days extends ScheduleType {
    override def toParameterValue: ParameterValue = "_14_DAYS_"
  }

  case object Every15Days extends ScheduleType {
    override def toParameterValue: ParameterValue = "_15_DAYS_"
  }

  case object Every30Days extends ScheduleType {
    override def toParameterValue: ParameterValue = "_30_DAYS_"
  }

  // A list of all available schedule types. New schedule types must be added here!
  final val ALL: Seq[ScheduleType] = EnumMacros.values[ScheduleType]
}
