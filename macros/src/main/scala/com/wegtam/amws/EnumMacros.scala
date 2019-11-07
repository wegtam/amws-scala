/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws

import scala.collection.immutable.Seq
import scala.language.experimental.macros
import scala.reflect.macros.whitebox

/**
  * Contains macros which are intended to help with enumerations like sealed
  * trait hierarchies.
  */
object EnumMacros {
  /**
    * Return a sequence of all known subclasses of a sealed trait which is provided
    * as the type parameter.
    *
    * <p><strong>The subclasses must be objects!</strong></p>
    *
    * @tparam A A sealed trait type.
    * @return A list of all known subclasses of the sealed trait.
    */
  def values[A]: Seq[A] = macro values_macro_impl[A]

  def values_macro_impl[A: c.WeakTypeTag](c: whitebox.Context): c.Expr[Seq[A]] = {
    import c.universe._

    val symbol = weakTypeOf[A].typeSymbol
    if (symbol.isClass && symbol.asClass.isSealed && symbol.asClass.isTrait) {
      val cs = symbol.asClass.knownDirectSubclasses.toList
      if (cs.forall(_.isModuleClass)) {
        c.Expr[Seq[A]] {
          def sourceModuleRef(sym: Symbol) = Ident(
            sym
              .asInstanceOf[
                scala.reflect.internal.Symbols#Symbol
              ]
              .sourceModule
              .asInstanceOf[Symbol]
          )

          Apply(
            Select(
              reify(Seq).tree,
              TermName("apply")
            ),
            cs.map(s => sourceModuleRef(s))
          )
        }
      } else {
        c.abort(c.enclosingPosition, "All sub classes of the sealed trait must be objects!")
      }
    } else {
      c.abort(c.enclosingPosition, "Value enumeration only works on sealed traits!")
    }
  }
}
