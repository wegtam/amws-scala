/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws

import org.scalatest.{ MustMatchers, WordSpec }

import scala.collection.immutable.Seq

class EnumMacrosTest extends WordSpec with MustMatchers {

  "EnumMacros" when {
    "not used on sealed traits" must {
      "not compile" in {
        """EnumMacros.values[UnsealedTestTrait]""" mustNot compile
      }
    }

    "used on sealed traits" when {
      "all subclasses are objects" must {
        "include all objects" in {
          val children: Seq[SealedTestTraitCorrect] = EnumMacros.values[SealedTestTraitCorrect]
          children mustEqual Seq(
            SealedTestTraitCorrect.ChildObject1,
            SealedTestTraitCorrect.ChildObject2
          )
        }
      }

      "not all subclasses are objects" must {
        "not compile" in {
          """EnumMacros.values[SealedTestTraitWrong]""" mustNot compile
        }
      }
    }
  }

}

sealed trait SealedTestTraitCorrect extends Product with Serializable

object SealedTestTraitCorrect {

  case object ChildObject1 extends SealedTestTraitCorrect

  case object ChildObject2 extends SealedTestTraitCorrect

}

sealed trait SealedTestTraitWrong extends Product with Serializable

object SealedTestTraitWrong {

  case object ChildObject1 extends SealedTestTraitWrong

  case object ChildObject2 extends SealedTestTraitWrong

  final case class ChildClass1(a: String, b: Int) extends SealedTestTraitWrong

  final case class ChildClass2(c: Long, d: String) extends SealedTestTraitWrong

}

trait UnsealedTestTrait

object UnsealedTestTrait {

  case object ChildObject1 extends UnsealedTestTrait

  case object ChildObject2 extends UnsealedTestTrait

}
