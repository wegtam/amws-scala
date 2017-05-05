package com.wegtam.amws.common

import com.wegtam.amws.common.Request.ParameterValue

/**
  * A sealed trait for the API actions. Every defined api action
  * has to extend this trait.
  */
trait Action {

  /**
    * The parameter value of the action that can be used in the query.
    *
    * @return A string representation of the action.
    */
  def toParameterValue: ParameterValue

}
