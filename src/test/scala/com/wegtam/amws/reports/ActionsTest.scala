/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.reports

import org.scalatest.{ MustMatchers, WordSpec }

class ActionsTest extends WordSpec with MustMatchers {

  "RequestReport" must {
    val a = Actions.RequestReport

    "provide toParameterValue" in {
      a.toParameterValue mustEqual "RequestReport"
    }
  }

  "GetReportRequestList" must {
    val a = Actions.GetReportRequestList

    "provide toParameterValue" in {
      a.toParameterValue mustEqual "GetReportRequestList"
    }
  }

  "GetReportList" must {
    val a = Actions.GetReportList

    "provide toParameterValue" in {
      a.toParameterValue mustEqual "GetReportList"
    }
  }
}
