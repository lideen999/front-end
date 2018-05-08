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
package org.openCypher.v9_0.rewriting.rewriters

import org.openCypher.v9_0.ast.GraphOfAs
import org.openCypher.v9_0.expressions.Expression
import org.openCypher.v9_0.util.{Rewriter, bottomUp}

case object nameGraphOfPatternElements extends Rewriter {

  def apply(that: AnyRef): AnyRef = instance(that)

  private val rewriter = Rewriter.lift {
    case g: GraphOfAs =>
      val rewrittenPattern = g.of.endoRewrite(nameAllPatternElements.namingRewriter)
      g.copy(of = rewrittenPattern)(g.position)
  }

  private val instance = bottomUp(rewriter, _.isInstanceOf[Expression])
}
