/*
 * Copyright © 2002-2018 Neo4j Sweden AB (http://neo4j.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencypher.v9_0.expressions

import org.opencypher.v9_0.util.InputPosition

case class Variable(name: String)(val position: InputPosition) extends LogicalVariable {

  override def copyId = copy()(position)

  override def renameId(newName: String) = copy(name = newName)(position)

  override def bumpId = copy()(position.bumped())

  override def asCanonicalStringVal: String = name
}

object Variable {
  implicit val byName: Ordering[Variable] =
    Ordering.by { (variable: Variable) =>
      (variable.name, variable.position)
    }(Ordering.Tuple2(implicitly[Ordering[String]], InputPosition.byOffset))
}