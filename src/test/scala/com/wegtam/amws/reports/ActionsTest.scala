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
