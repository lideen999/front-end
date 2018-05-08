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
package org.openCypher.v9_0.expressions.functions

import org.openCypher.v9_0.expressions.{TypeSignature, TypeSignatures}
import org.openCypher.v9_0.util.symbols._

case object Range extends Function with TypeSignatures {
  def name = "range"

  override val signatures = Vector(
    TypeSignature(argumentTypes = Vector(CTInteger, CTInteger), outputType = CTList(CTInteger)),
    TypeSignature(argumentTypes = Vector(CTInteger, CTInteger, CTInteger), outputType = CTList(CTInteger))
  )
}
