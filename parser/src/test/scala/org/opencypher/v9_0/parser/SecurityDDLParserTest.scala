/*
 * Copyright © 2002-2019 Neo4j Sweden AB (http://neo4j.com)
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
package org.opencypher.v9_0.parser

import org.opencypher.v9_0.ast
import org.opencypher.v9_0.ast.{AllGraphsScope, AstConstructionTestSupport}
import org.opencypher.v9_0.expressions.{Parameter => Param}
import org.opencypher.v9_0.util.symbols._
import org.parboiled.scala.Rule1

class SecurityDDLParserTest
  extends ParserAstTest[ast.Statement] with Statement with AstConstructionTestSupport {

  implicit val parser: Rule1[ast.Statement] = Statement

  test("SHOW USERS") {
    yields(ast.ShowUsers())
  }

  test("CATALOG CREATE USER foo SET PASSWORD 'password'") {
    yields(ast.CreateUser("foo", Some("password"), None, requirePasswordChange = true, suspended = false))
  }

  test("CREATE uSER foo SET PASSWORD $password") {
    yields(ast.CreateUser("foo", None, Some(Param("password", CTAny)(_)), requirePasswordChange = true, suspended = false))
  }

  test("CREATE USER \"foo\" SET PASSwORD 'password'") {
    failsToParse
  }

  test("CREATE USER `foo` SET PASSwORD 'password'") {
    yields(ast.CreateUser("foo", Some("password"), None, requirePasswordChange = true, suspended = false))
  }

  test("CREATE USER !#\"~ SeT PASSWORD 'password'") {
    failsToParse
  }

  test("CREATE USER `!#\"~` SeT PASSWORD 'password'") {
    yields(ast.CreateUser("!#\"~", Some("password"), None, requirePasswordChange = true, suspended = false))
  }

  test("CREATE USER foo SeT PASSWORD 'pasS5Wor%d'") {
    yields(ast.CreateUser("foo", Some("pasS5Wor%d"), None, requirePasswordChange = true, suspended = false))
  }

  test("CREaTE USER foo SET PASSWORD 'password' CHANGE REQUIRED") {
    yields(ast.CreateUser("foo", Some("password"), None, requirePasswordChange = true, suspended = false))
  }

  test("CATALOG CREATE USER foo SET PASSWORD $password CHANGE REQUIRED") {
    yields(ast.CreateUser("foo", None, Some(Param("password", CTAny)(_)), requirePasswordChange = true, suspended = false))
  }

  test("CREATE USER foo SET PASSWORD 'password' SET PASSWORD CHANGE required") {
    yields(ast.CreateUser("foo", Some("password"), None, requirePasswordChange = true, suspended = false))
  }

  test("CREATE USER foo SET PASSWORD 'password' CHAngE NOT REQUIRED") {
    yields(ast.CreateUser("foo", Some("password"), None, requirePasswordChange = false, suspended = false))
  }

  test("CREATE USER foo SET PASSWORD 'password' SET PASSWORD CHANGE NOT REQUIRED") {
    yields(ast.CreateUser("foo", Some("password"), None, requirePasswordChange = false, suspended = false))
  }

  test("CREATE USER foo SET PASSWORD $password SET  PASSWORD CHANGE NOT REQUIRED") {
    yields(ast.CreateUser("foo", None, Some(Param("password", CTAny)(_)), requirePasswordChange = false, suspended = false))
  }

  test("CATALOG CREATE USER foo SET PASSWORD 'password' SET STATUS SUSPENDed") {
    yields(ast.CreateUser("foo", Some("password"), None, requirePasswordChange = true, suspended = true))
  }

  test("CREATE USER foo SET PASSWORD 'password' SET STATUS ACtiVE") {
    yields(ast.CreateUser("foo", Some("password"), None, requirePasswordChange = true, suspended = false))
  }

  test("CREATE USER foo SET PASSWORD 'password' SET PASSWORD CHANGE NOT REQUIRED SET   STATuS SUSPENDED") {
    yields(ast.CreateUser("foo", Some("password"), None, requirePasswordChange = false, suspended = true))
  }

  test("CREATE USER foo SET PASSWORD $password CHANGE REQUIRED SET STATUS SUSPENDED") {
    yields(ast.CreateUser("foo", None, Some(Param("password", CTAny)(_)), requirePasswordChange = true, suspended = true))
  }

  test("CREATE USER foo") {
    failsToParse
  }

  test("CREATE USER foo SET PASSWORD null CHANGE REQUIRED") {
    failsToParse
  }

  test("CATALOG CREATE USER fo,o SET PASSWORD 'password'") {
    failsToParse
  }

  test("CREATE USER f:oo SET PASSWORD 'password'") {
    failsToParse
  }

  test("CREATE USER foo PASSWORD 'password'") {
    failsToParse
  }

  test("CREATE USER foo SET PASSWORD 'password' WITH STAUS ACTIVE") {
    failsToParse
  }

  test("CREATE USER foo SET PASSWORD CHANGE REQUIRED") {
    failsToParse
  }

  test("CREATE USER foo SET STATUS SUSPENDED") {
    failsToParse
  }

  test("CREATE USER foo SET PASSWORD CHANGE REQUIRED STATUS ACTIVE") {
    failsToParse
  }

  test("DROP USER foo") {
    yields(ast.DropUser("foo"))
  }

  test("CATALOG ALTER USER foo SET PASSWORD 'password'") {
    yields(ast.AlterUser("foo", Some("password"), None, None, None))
  }

  test("ALTER USER foo SET PASSWORD $password") {
    yields(ast.AlterUser("foo", None, Some(Param("password", CTAny)(_)), None, None))
  }

  test("CATALOG ALTER USER foo SET PASSWORD CHANGE REQUIRED") {
    yields(ast.AlterUser("foo", None, None, requirePasswordChange = Some(true), None))
  }

  test("CATALOG ALTER USER foo SET PASSWORD CHANGE NOT REQUIRED") {
    yields(ast.AlterUser("foo", None, None, requirePasswordChange = Some(false), None))
  }

  test("ALTER USER foo SET STATUS SUSPENDED") {
    yields(ast.AlterUser("foo", None, None, None, suspended = Some(true)))
  }

  test("ALTER USER foo SET STATUS ACTIVE") {
    yields(ast.AlterUser("foo", None, None, None, suspended = Some(false)))
  }

  test("CATALOG ALTER USER foo SET PASSWORD 'password' CHANGE REQUIRED") {
    yields(ast.AlterUser("foo", Some("password"), None, requirePasswordChange = Some(true), None))
  }

  test("ALTER USER foo SET PASSWORD $password SET PASSWORD CHANGE NOT REQUIRED") {
    yields(ast.AlterUser("foo", None, Some(Param("password", CTAny)(_)), requirePasswordChange = Some(false), None))
  }

  test("CATALOG ALTER USER foo SET PASSWORD 'password' SET STATUS ACTIVE") {
    yields(ast.AlterUser("foo", Some("password"), None, None, suspended = Some(false)))
  }

  test("CATALOG ALTER USER foo SET PASSWORD CHANGE NOT REQUIRED SET STATUS ACTIVE") {
    yields(ast.AlterUser("foo", None, None, requirePasswordChange = Some(false), suspended = Some(false)))
  }

  test("ALTER USER foo SET PASSWORD $password SET PASSWORD CHANGE NOT REQUIRED SET STATUS SUSPENDED") {
    yields(ast.AlterUser("foo", None, Some(Param("password", CTAny)(_)), requirePasswordChange = Some(false), suspended = Some(true)))
  }

  test("ALTER USER foo SET PASSWORD") {
    failsToParse
  }

  test("ALTER USER foo SET STATUS") {
    failsToParse
  }

  test("ALTER USER foo SET PASSWORD null") {
    failsToParse
  }

  test("ALTER USER foo SET PASSWORD 'password' SET PASSWORD SET STATUS ACTIVE") {
    failsToParse
  }

  test("ALTER USER foo SET PASSWORD STATUS ACTIVE") {
    failsToParse
  }

  test("SHOW ROLES") {
    yields(ast.ShowRoles(withUsers = false, showAll = true))
  }

  test("SHOW ALL ROLES") {
    yields(ast.ShowRoles(withUsers = false, showAll = true))
  }

  test("CATALOG SHOW POPULATED ROLES") {
    yields(ast.ShowRoles(withUsers = false, showAll = false))
  }

  test("SHOW ROLES WITH USERS") {
    yields(ast.ShowRoles(withUsers = true, showAll = true))
  }

  test("CATALOG SHOW ALL ROLES WITH USERS") {
    yields(ast.ShowRoles(withUsers = true, showAll = true))
  }

  test("SHOW POPULATED ROLES WITH USERS") {
    yields(ast.ShowRoles(withUsers = true, showAll = false))
  }

  test("CREATE ROLE foo") {
    yields(ast.CreateRole("foo", None))
  }

  test("CATALOG CREATE ROLE \"foo\"") {
    failsToParse
  }

  test("CATALOG CREATE ROLE `foo`") {
    yields(ast.CreateRole("foo", None))
  }

  test("CREATE ROLE f%o") {
    failsToParse
  }

  test("CREATE ROLE foo AS COPY OF bar") {
    yields(ast.CreateRole("foo", Some("bar")))
  }

  test("CREATE ROLE foo AS COPY OF") {
    failsToParse
  }

  test("DROP ROLE foo") {
    yields(ast.DropRole("foo"))
  }

  test("SHOW PRIVILEGES") {
    yields(ast.ShowPrivileges(ast.ShowAllPrivileges() _))
  }

  test("SHOW ALL PRIVILEGES") {
    yields(ast.ShowPrivileges(ast.ShowAllPrivileges() _))
  }

  test("SHOW USER PRIVILEGES") {
    failsToParse
  }

  test("SHOW USER user PRIVILEGES") {
    yields(ast.ShowPrivileges(ast.ShowUserPrivileges("user") _))
  }

  test("SHOW USER us%er PRIVILEGES") {
    failsToParse
  }

  test("SHOW USER `us%er` PRIVILEGES") {
    yields(ast.ShowPrivileges(ast.ShowUserPrivileges("us%er") _))
  }

  test("SHOW ROLE PRIVILEGES") {
    failsToParse
  }

  test("SHOW ROLE role PRIVILEGES") {
    yields(ast.ShowPrivileges(ast.ShowRolePrivileges("role") _))
  }

  test("SHOW ROLE ro%le PRIVILEGES") {
    failsToParse
  }

  test("SHOW ROLE `ro%le` PRIVILEGES") {
    yields(ast.ShowPrivileges(ast.ShowRolePrivileges("ro%le") _))
  }

  test("GRANT TRAVERSE GRAPH * NODES * (*) TO role") {
    failsToParse
  }

  test("GRANT TRAVERSE ON GRAPH * NODES * (*)") {
    failsToParse
  }

  test("GRANT TRAVERSE ON GRAPH * NODES * TO role") {
    yields(ast.GrantTraverse(ast.AllGraphsScope() _, ast.AllQualifier() _, "role"))
  }

  test("GRANT TRAVERSE ON GRAPH * TO role") {
    yields(ast.GrantTraverse(ast.AllGraphsScope() _, ast.AllQualifier() _, "role"))
  }

  test("GRANT TRAVERSE ON GRAPH foo TO role") {
    yields(ast.GrantTraverse(ast.NamedGraphScope("foo") _, ast.AllQualifier() _, "role"))
  }

  test("GRANT TRAVERSE ON GRAPH foo NODES * TO role") {
    yields(ast.GrantTraverse(ast.NamedGraphScope("foo") _, ast.AllQualifier() _, "role"))
  }

  test("GRANT TRAVERSE ON GRAPH foo NODES A TO role") {
    yields(ast.GrantTraverse(ast.NamedGraphScope("foo") _, ast.LabelQualifier("A") _, "role"))
  }

  test("GRANT TRAVERSE ON GRAPH * NODES * (*) TO role") {
    yields(ast.GrantTraverse(ast.AllGraphsScope() _, ast.AllQualifier() _, "role"))
  }

  test("GRANT TRAVERSE ON GRAPH foo NODES * (*) TO role") {
    yields(ast.GrantTraverse(ast.NamedGraphScope("foo") _, ast.AllQualifier() _, "role"))
  }

  test("GRANT TRAVERSE ON GRAPH * NODES A (*) TO role") {
    yields(ast.GrantTraverse(ast.AllGraphsScope() _, ast.LabelQualifier("A") _, "role"))
  }

  test("GRANT TRAVERSE ON GRAPH foo NODES A (*) TO role") {
    yields(ast.GrantTraverse(ast.NamedGraphScope("foo") _, ast.LabelQualifier("A") _, "role"))
  }

  test("GRANT TRAVERSE ON GRAPH foo NODES A (foo) TO role") {
    failsToParse
  }

  test("GRANT READ (*) GRAPH * NODES * (*) TO role") {
    failsToParse
  }

  test("GRANT READ (*) ON GRAPH * NODES * (*)") {
    failsToParse
  }

  test("GRANT READ (*) ON GRAPH * NODES * TO role") {
    yields(ast.GrantRead(ast.AllResource() _, ast.AllGraphsScope() _, ast.AllQualifier() _, "role"))
  }

  test("GRANT READ (*) ON GRAPH * TO role") {
    yields(ast.GrantRead(ast.AllResource() _, ast.AllGraphsScope() _, ast.AllQualifier() _, "role"))
  }

  test("GRANT READ (*) ON GRAPH foo TO role") {
    yields(ast.GrantRead(ast.AllResource() _, ast.NamedGraphScope("foo") _, ast.AllQualifier() _, "role"))
  }

  test("GRANT READ (*) ON GRAPH foo NODES * TO role") {
    yields(ast.GrantRead(ast.AllResource() _, ast.NamedGraphScope("foo") _, ast.AllQualifier() _, "role"))
  }

  test("GRANT READ (*) ON GRAPH foo NODES A TO role") {
    yields(ast.GrantRead(ast.AllResource() _, ast.NamedGraphScope("foo") _, ast.LabelQualifier("A") _, "role"))
  }

  test("GRANT READ (*) ON GRAPH * NODES * (*) TO role") {
    yields(ast.GrantRead(ast.AllResource() _, ast.AllGraphsScope() _, ast.AllQualifier() _, "role"))
  }

  test("GRANT READ (*) ON GRAPH foo NODES * (*) TO role") {
    yields(ast.GrantRead(ast.AllResource() _, ast.NamedGraphScope("foo") _, ast.AllQualifier() _, "role"))
  }

  test("GRANT READ (*) ON GRAPH * NODES A (*) TO role") {
    yields(ast.GrantRead(ast.AllResource() _, ast.AllGraphsScope() _, ast.LabelQualifier("A") _, "role"))
  }

  test("GRANT READ (*) ON GRAPH foo NODES A (*) TO role") {
    yields(ast.GrantRead(ast.AllResource() _, ast.NamedGraphScope("foo") _, ast.LabelQualifier("A") _, "role"))
  }

  test("GRANT READ (*) ON GRAPH foo NODES A (foo) TO role") {
    failsToParse
  }

  test("GRANT READ (bar) GRAPH * NODES * (*) TO role") {
    failsToParse
  }

  test("GRANT READ (bar) ON GRAPH * NODES * (*)") {
    failsToParse
  }

  test("GRANT READ (bar) ON GRAPH * NODES * TO role") {
    yields(ast.GrantRead(ast.PropertyResource("bar") _, ast.AllGraphsScope() _, ast.AllQualifier() _, "role"))
  }

  test("GRANT READ (bar) ON GRAPH * TO role") {
    yields(ast.GrantRead(ast.PropertyResource("bar") _, ast.AllGraphsScope() _, ast.AllQualifier() _, "role"))
  }

  test("GRANT READ (bar) ON GRAPH foo TO role") {
    yields(ast.GrantRead(ast.PropertyResource("bar") _, ast.NamedGraphScope("foo") _, ast.AllQualifier() _, "role"))
  }

  test("GRANT READ (bar) ON GRAPH foo NODES * TO role") {
    yields(ast.GrantRead(ast.PropertyResource("bar") _, ast.NamedGraphScope("foo") _, ast.AllQualifier() _, "role"))
  }

  test("GRANT READ (bar) ON GRAPH foo NODES A TO role") {
    yields(ast.GrantRead(ast.PropertyResource("bar") _, ast.NamedGraphScope("foo") _, ast.LabelQualifier("A") _, "role"))
  }

  test("GRANT READ (bar) ON GRAPH * NODES * (*) TO role") {
    yields(ast.GrantRead(ast.PropertyResource("bar") _, ast.AllGraphsScope() _, ast.AllQualifier() _, "role"))
  }

  test("GRANT READ (bar) ON GRAPH foo NODES * (*) TO role") {
    yields(ast.GrantRead(ast.PropertyResource("bar") _, ast.NamedGraphScope("foo") _, ast.AllQualifier() _, "role"))
  }

  test("GRANT READ (bar) ON GRAPH * NODES A (*) TO role") {
    yields(ast.GrantRead(ast.PropertyResource("bar") _, ast.AllGraphsScope() _, ast.LabelQualifier("A") _, "role"))
  }

  test("GRANT READ (bar) ON GRAPH foo NODES A (*) TO role") {
    yields(ast.GrantRead(ast.PropertyResource("bar") _, ast.NamedGraphScope("foo") _, ast.LabelQualifier("A") _, "role"))
  }

  test("GRANT READ (bar) ON GRAPH foo NODES A (foo) TO role") {
    failsToParse
  }
}
