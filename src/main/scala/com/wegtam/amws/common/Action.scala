/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

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
