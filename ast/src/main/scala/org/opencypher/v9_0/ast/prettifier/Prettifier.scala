/*
 * Copyright (c) Neo4j Sweden AB (http://neo4j.com)
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
package org.opencypher.v9_0.ast.prettifier

import org.opencypher.v9_0.ast.ActionResource
import org.opencypher.v9_0.ast.AdministrationCommand
import org.opencypher.v9_0.ast.AliasedReturnItem
import org.opencypher.v9_0.ast.AllDatabasesQualifier
import org.opencypher.v9_0.ast.AllDatabasesScope
import org.opencypher.v9_0.ast.AllGraphsScope
import org.opencypher.v9_0.ast.AllLabelResource
import org.opencypher.v9_0.ast.AllNodes
import org.opencypher.v9_0.ast.AllPropertyResource
import org.opencypher.v9_0.ast.AllQualifier
import org.opencypher.v9_0.ast.AllRelationships
import org.opencypher.v9_0.ast.AlterUser
import org.opencypher.v9_0.ast.AscSortItem
import org.opencypher.v9_0.ast.Clause
import org.opencypher.v9_0.ast.Create
import org.opencypher.v9_0.ast.CreateDatabase
import org.opencypher.v9_0.ast.CreateGraph
import org.opencypher.v9_0.ast.CreateIndex
import org.opencypher.v9_0.ast.CreateIndexOldSyntax
import org.opencypher.v9_0.ast.CreateNodeKeyConstraint
import org.opencypher.v9_0.ast.CreateNodePropertyExistenceConstraint
import org.opencypher.v9_0.ast.CreateRelationshipPropertyExistenceConstraint
import org.opencypher.v9_0.ast.CreateRole
import org.opencypher.v9_0.ast.CreateUniquePropertyConstraint
import org.opencypher.v9_0.ast.CreateUser
import org.opencypher.v9_0.ast.CreateView
import org.opencypher.v9_0.ast.DatabasePrivilege
import org.opencypher.v9_0.ast.DatabaseScope
import org.opencypher.v9_0.ast.DbmsPrivilege
import org.opencypher.v9_0.ast.DefaultDatabaseScope
import org.opencypher.v9_0.ast.DefaultGraphScope
import org.opencypher.v9_0.ast.Delete
import org.opencypher.v9_0.ast.DenyPrivilege
import org.opencypher.v9_0.ast.DescSortItem
import org.opencypher.v9_0.ast.DestroyData
import org.opencypher.v9_0.ast.DropConstraintOnName
import org.opencypher.v9_0.ast.DropDatabase
import org.opencypher.v9_0.ast.DropGraph
import org.opencypher.v9_0.ast.DropIndex
import org.opencypher.v9_0.ast.DropIndexOnName
import org.opencypher.v9_0.ast.DropNodeKeyConstraint
import org.opencypher.v9_0.ast.DropNodePropertyExistenceConstraint
import org.opencypher.v9_0.ast.DropRelationshipPropertyExistenceConstraint
import org.opencypher.v9_0.ast.DropRole
import org.opencypher.v9_0.ast.DropUniquePropertyConstraint
import org.opencypher.v9_0.ast.DropUser
import org.opencypher.v9_0.ast.DropView
import org.opencypher.v9_0.ast.DumpData
import org.opencypher.v9_0.ast.ElementQualifier
import org.opencypher.v9_0.ast.ElementsAllQualifier
import org.opencypher.v9_0.ast.Foreach
import org.opencypher.v9_0.ast.FromGraph
import org.opencypher.v9_0.ast.FunctionAllQualifier
import org.opencypher.v9_0.ast.FunctionQualifier
import org.opencypher.v9_0.ast.GrantPrivilege
import org.opencypher.v9_0.ast.GrantRolesToUsers
import org.opencypher.v9_0.ast.GraphAction
import org.opencypher.v9_0.ast.GraphPrivilege
import org.opencypher.v9_0.ast.GraphScope
import org.opencypher.v9_0.ast.GraphSelection
import org.opencypher.v9_0.ast.IfExistsDo
import org.opencypher.v9_0.ast.IfExistsDoNothing
import org.opencypher.v9_0.ast.IfExistsInvalidSyntax
import org.opencypher.v9_0.ast.IfExistsReplace
import org.opencypher.v9_0.ast.IfExistsThrowError
import org.opencypher.v9_0.ast.LabelAllQualifier
import org.opencypher.v9_0.ast.LabelQualifier
import org.opencypher.v9_0.ast.LabelsResource
import org.opencypher.v9_0.ast.Limit
import org.opencypher.v9_0.ast.LoadCSV
import org.opencypher.v9_0.ast.Match
import org.opencypher.v9_0.ast.Merge
import org.opencypher.v9_0.ast.MergeAction
import org.opencypher.v9_0.ast.MultiGraphDDL
import org.opencypher.v9_0.ast.NamedDatabaseScope
import org.opencypher.v9_0.ast.NamedGraphScope
import org.opencypher.v9_0.ast.NodeByIds
import org.opencypher.v9_0.ast.NodeByParameter
import org.opencypher.v9_0.ast.OnCreate
import org.opencypher.v9_0.ast.OnMatch
import org.opencypher.v9_0.ast.OrderBy
import org.opencypher.v9_0.ast.PrivilegeQualifier
import org.opencypher.v9_0.ast.ProcedureAllQualifier
import org.opencypher.v9_0.ast.ProcedureQualifier
import org.opencypher.v9_0.ast.ProcedureResult
import org.opencypher.v9_0.ast.ProcedureResultItem
import org.opencypher.v9_0.ast.ProjectingUnionAll
import org.opencypher.v9_0.ast.ProjectingUnionDistinct
import org.opencypher.v9_0.ast.PropertiesResource
import org.opencypher.v9_0.ast.PropertyResource
import org.opencypher.v9_0.ast.Query
import org.opencypher.v9_0.ast.QueryPart
import org.opencypher.v9_0.ast.RelationshipAllQualifier
import org.opencypher.v9_0.ast.RelationshipByIds
import org.opencypher.v9_0.ast.RelationshipByParameter
import org.opencypher.v9_0.ast.RelationshipQualifier
import org.opencypher.v9_0.ast.Remove
import org.opencypher.v9_0.ast.RemoveLabelItem
import org.opencypher.v9_0.ast.RemovePropertyItem
import org.opencypher.v9_0.ast.Return
import org.opencypher.v9_0.ast.ReturnItem
import org.opencypher.v9_0.ast.ReturnItems
import org.opencypher.v9_0.ast.RevokePrivilege
import org.opencypher.v9_0.ast.RevokeRolesFromUsers
import org.opencypher.v9_0.ast.SchemaCommand
import org.opencypher.v9_0.ast.SeekOnly
import org.opencypher.v9_0.ast.SetClause
import org.opencypher.v9_0.ast.SetExactPropertiesFromMapItem
import org.opencypher.v9_0.ast.SetIncludingPropertiesFromMapItem
import org.opencypher.v9_0.ast.SetLabelItem
import org.opencypher.v9_0.ast.SetOwnPassword
import org.opencypher.v9_0.ast.SetPropertyItem
import org.opencypher.v9_0.ast.ShowAllPrivileges
import org.opencypher.v9_0.ast.ShowConstraints
import org.opencypher.v9_0.ast.ShowCurrentUser
import org.opencypher.v9_0.ast.ShowDatabase
import org.opencypher.v9_0.ast.ShowIndexesClause
import org.opencypher.v9_0.ast.ShowPrivilegeCommands
import org.opencypher.v9_0.ast.ShowPrivilegeScope
import org.opencypher.v9_0.ast.ShowPrivileges
import org.opencypher.v9_0.ast.ShowRoles
import org.opencypher.v9_0.ast.ShowRolesPrivileges
import org.opencypher.v9_0.ast.ShowUserPrivileges
import org.opencypher.v9_0.ast.ShowUsers
import org.opencypher.v9_0.ast.ShowUsersPrivileges
import org.opencypher.v9_0.ast.SingleQuery
import org.opencypher.v9_0.ast.Skip
import org.opencypher.v9_0.ast.Start
import org.opencypher.v9_0.ast.StartDatabase
import org.opencypher.v9_0.ast.Statement
import org.opencypher.v9_0.ast.StopDatabase
import org.opencypher.v9_0.ast.SubQuery
import org.opencypher.v9_0.ast.UnaliasedReturnItem
import org.opencypher.v9_0.ast.Union
import org.opencypher.v9_0.ast.Union.UnionMapping
import org.opencypher.v9_0.ast.UnionAll
import org.opencypher.v9_0.ast.UnionDistinct
import org.opencypher.v9_0.ast.UnresolvedCall
import org.opencypher.v9_0.ast.Unwind
import org.opencypher.v9_0.ast.UseGraph
import org.opencypher.v9_0.ast.UserAllQualifier
import org.opencypher.v9_0.ast.UserQualifier
import org.opencypher.v9_0.ast.UsingHint
import org.opencypher.v9_0.ast.UsingIndexHint
import org.opencypher.v9_0.ast.UsingJoinHint
import org.opencypher.v9_0.ast.UsingScanHint
import org.opencypher.v9_0.ast.Where
import org.opencypher.v9_0.ast.With
import org.opencypher.v9_0.ast.Yield
import org.opencypher.v9_0.ast.YieldOrWhere
import org.opencypher.v9_0.expressions.CoerceTo
import org.opencypher.v9_0.expressions.Expression
import org.opencypher.v9_0.expressions.ImplicitProcedureArgument
import org.opencypher.v9_0.expressions.LabelName
import org.opencypher.v9_0.expressions.Namespace
import org.opencypher.v9_0.expressions.Parameter
import org.opencypher.v9_0.expressions.ParameterWithOldSyntax
import org.opencypher.v9_0.expressions.Property
import org.opencypher.v9_0.expressions.RelTypeName
import org.opencypher.v9_0.expressions.Variable

//noinspection DuplicatedCode
case class Prettifier(
  expr: ExpressionStringifier,
  extension: Prettifier.ClausePrettifier = Prettifier.EmptyExtension,
  useInCommands: Boolean = true
) {

  private val NL = System.lineSeparator()

  private val base = IndentingQueryPrettifier()

  def asString(statement: Statement): String = statement match {
    case q: Query                 => base.query(q)
    case c: SchemaCommand         => asString(c)
    case c: AdministrationCommand => asString(c)
    case c: MultiGraphDDL         => asString(c)
    case _ => throw new IllegalStateException(s"Unknown statement: $statement")
  }

  def asString(command: SchemaCommand): String = {
    def backtick(s: String) = ExpressionStringifier.backtick(s)
    def propertiesToString(properties: Seq[Property]): String = properties.map(propertyToString).mkString("(", ", ", ")")
    def propertyToString(property: Property): String = s"${expr(property.map)}.${ExpressionStringifier.backtick(property.propertyKey.name)}"
    def propertyToStringExistenceConstraint(property: Property, oldSyntax: Boolean): String =
      if (oldSyntax) s"exists(${propertyToString(property)})" else s"(${propertyToString(property)}) IS NOT NULL"
    def getStartOfCommand(name: Option[String], ifExistsDo: IfExistsDo, schemaType: String): String = {
      val nameString = name.map(n => s"${backtick(n)} ").getOrElse("")
      ifExistsDo match {
        case IfExistsDoNothing     => s"CREATE $schemaType ${nameString}IF NOT EXISTS "
        case IfExistsInvalidSyntax => s"CREATE OR REPLACE $schemaType ${nameString}IF NOT EXISTS "
        case IfExistsReplace       => s"CREATE OR REPLACE $schemaType $nameString"
        case IfExistsThrowError    => s"CREATE $schemaType $nameString"
      }
    }
    def optionsToString(options: Map[String, Expression]) =
      if (options.nonEmpty) s" OPTIONS ${options.map({ case (s, e) => s"${backtick(s)}: ${expr(e)}" }).mkString("{", ", ", "}")}" else ""

    val useString = asString(command.useGraph)
    val commandString = command match {

      case CreateIndexOldSyntax(LabelName(label), properties, _) =>
        s"CREATE INDEX ON :${backtick(label)}${properties.map(p => backtick(p.name)).mkString("(", ", ", ")")}"

      case CreateIndex(Variable(variable), LabelName(label), properties, name, ifExistsDo, options, _) =>
        val startOfCommand = getStartOfCommand(name, ifExistsDo, "INDEX")
        s"${startOfCommand}FOR (${backtick(variable)}:${backtick(label)}) ON ${propertiesToString(properties)}${optionsToString(options)}"

      case DropIndex(LabelName(label), properties, _) =>
        s"DROP INDEX ON :${backtick(label)}${properties.map(p => backtick(p.name)).mkString("(", ", ", ")")}"

      case DropIndexOnName(name, ifExists, _) =>
        val ifExistsString = if (ifExists) " IF EXISTS" else ""
        s"DROP INDEX ${backtick(name)}$ifExistsString"

      case CreateNodeKeyConstraint(Variable(variable), LabelName(label), properties, name, ifExistsDo, options, _) =>
        val startOfCommand = getStartOfCommand(name, ifExistsDo, "CONSTRAINT")
        s"${startOfCommand}ON (${backtick(variable)}:${backtick(label)}) ASSERT ${propertiesToString(properties)} IS NODE KEY${optionsToString(options)}"

      case DropNodeKeyConstraint(Variable(variable), LabelName(label), properties, _) =>
        s"DROP CONSTRAINT ON (${backtick(variable)}:${backtick(label)}) ASSERT ${propertiesToString(properties)} IS NODE KEY"

      case CreateUniquePropertyConstraint(Variable(variable), LabelName(label), properties, name, ifExistsDo, options, _) =>
        val startOfCommand = getStartOfCommand(name, ifExistsDo, "CONSTRAINT")
        s"${startOfCommand}ON (${backtick(variable)}:${backtick(label)}) ASSERT ${propertiesToString(properties)} IS UNIQUE${optionsToString(options)}"

      case DropUniquePropertyConstraint(Variable(variable), LabelName(label), properties, _) =>
        s"DROP CONSTRAINT ON (${backtick(variable)}:${backtick(label)}) ASSERT ${propertiesToString(properties)} IS UNIQUE"

      case CreateNodePropertyExistenceConstraint(Variable(variable), LabelName(label), property, name, ifExistsDo, oldSyntax, options, _) =>
        val startOfCommand = getStartOfCommand(name, ifExistsDo, "CONSTRAINT")
        val propertyString = propertyToStringExistenceConstraint(property, oldSyntax)
        val optionsString = options.map(o => if (o.nonEmpty) optionsToString(o) else " OPTIONS {}").getOrElse("")
        s"${startOfCommand}ON (${backtick(variable)}:${backtick(label)}) ASSERT $propertyString$optionsString"

      case DropNodePropertyExistenceConstraint(Variable(variable), LabelName(label), property, _) =>
        s"DROP CONSTRAINT ON (${backtick(variable)}:${backtick(label)}) ASSERT exists(${propertyToString(property)})"

      case CreateRelationshipPropertyExistenceConstraint(Variable(variable), RelTypeName(relType), property, name, ifExistsDo, oldSyntax, options, _) =>
        val startOfCommand = getStartOfCommand(name, ifExistsDo, "CONSTRAINT")
        val propertyString = propertyToStringExistenceConstraint(property, oldSyntax)
        val optionsString = options.map(o => if (o.nonEmpty) optionsToString(o) else " OPTIONS {}").getOrElse("")
        s"${startOfCommand}ON ()-[${backtick(variable)}:${backtick(relType)}]-() ASSERT $propertyString$optionsString"

      case DropRelationshipPropertyExistenceConstraint(Variable(variable), RelTypeName(relType), property, _) =>
        s"DROP CONSTRAINT ON ()-[${backtick(variable)}:${backtick(relType)}]-() ASSERT exists(${propertyToString(property)})"

      case DropConstraintOnName(name, ifExists, _) =>
        val ifExistsString = if (ifExists) " IF EXISTS" else ""
        s"DROP CONSTRAINT ${backtick(name)}$ifExistsString"

      case ShowConstraints(constraintType, verbose, _) =>
        val output = if (verbose) "VERBOSE" else "BRIEF"
        s"SHOW ${constraintType.prettyPrint} CONSTRAINTS $output"

      case _ => throw new IllegalStateException(s"Unknown command: $command")
    }
    useString + commandString
  }

  def asString(adminCommand: AdministrationCommand): String =  {
    val useString = asString(adminCommand.useGraph)

    def showClausesAsString(yieldOrWhere: YieldOrWhere): (String, String) = {
      val ind: IndentingQueryPrettifier = base.indented()
      yieldOrWhere match {
        case Some(Left((y,r))) => ("\n" + ind.asString(y), r.map(ind.asString).map("\n" + _).getOrElse(""))
        case Some(Right(w)) => ("\n" + ind.asString(w), "")
        case None => ("", "")
      }
    }
    val commandString = adminCommand match {

      // User commands

      case x @ ShowUsers(yields,_) =>
        val (y: String, r: String) = showClausesAsString(yields)
        s"${x.name}$y$r"

      case x @ ShowCurrentUser(yields,_) =>
        val (y: String, r: String) = showClausesAsString(yields)
        s"${x.name}$y$r"

      case x @ CreateUser(userName, isEncryptedPassword, initialPassword, userOptions, ifExistsDo) =>
        val userNameString = Prettifier.escapeName(userName)
        val ifNotExists = ifExistsDo match {
          case IfExistsDoNothing | IfExistsInvalidSyntax => " IF NOT EXISTS"
          case _                    => ""
        }
        val setPasswordString = if(isEncryptedPassword) "SET ENCRYPTED PASSWORD" else "SET PASSWORD"
        val password = expr.escapePassword(initialPassword)
        val passwordString = s"$setPasswordString $password CHANGE ${if (userOptions.requirePasswordChange.getOrElse(true)) "" else "NOT "}REQUIRED"
        val statusString = if (userOptions.suspended.isDefined) s" SET STATUS ${if (userOptions.suspended.get) "SUSPENDED" else "ACTIVE"}" else ""
        s"${x.name} $userNameString$ifNotExists $passwordString$statusString"

      case x @ DropUser(userName, ifExists) =>
        if (ifExists) s"${x.name} ${Prettifier.escapeName(userName)} IF EXISTS"
        else s"${x.name} ${Prettifier.escapeName(userName)}"

      case x @ AlterUser(userName, isEncryptedPassword, initialPassword, userOptions, ifExists) =>
        val userNameString = Prettifier.escapeName(userName)
        val ifExistsString = if (ifExists) " IF EXISTS" else ""
        val passwordString = initialPassword.map(" " + expr.escapePassword(_)).getOrElse("")
        val passwordModeString = if (userOptions.requirePasswordChange.isDefined)
          s" CHANGE ${if (!userOptions.requirePasswordChange.get) "NOT " else ""}REQUIRED"
        else
          ""
        val setPasswordString = if(isEncryptedPassword.getOrElse(false)) "SET ENCRYPTED PASSWORD" else "SET PASSWORD"
        val passwordPrefix = if (passwordString.nonEmpty || passwordModeString.nonEmpty) s" $setPasswordString" else ""
        val statusString = if (userOptions.suspended.isDefined) s" SET STATUS ${if (userOptions.suspended.get) "SUSPENDED" else "ACTIVE"}" else ""
        s"${x.name} $userNameString$ifExistsString$passwordPrefix$passwordString$passwordModeString$statusString"

      case x @ SetOwnPassword(newPassword, currentPassword) =>
        s"${x.name} FROM ${expr.escapePassword(currentPassword)} TO ${expr.escapePassword(newPassword)}"

      // Role commands

      case x @ ShowRoles(withUsers, _, yields,_) =>
        val (y: String, r: String) = showClausesAsString(yields)
        s"${x.name}${if (withUsers) " WITH USERS" else ""}$y$r"

      case x @ CreateRole(roleName, None, ifExistsDo) =>
        ifExistsDo match {
          case IfExistsDoNothing | IfExistsInvalidSyntax => s"${x.name} ${Prettifier.escapeName(roleName)} IF NOT EXISTS"
          case _                    => s"${x.name} ${Prettifier.escapeName(roleName)}"
        }

      case x @ CreateRole(roleName, Some(fromRole), ifExistsDo) =>
        ifExistsDo match {
          case IfExistsDoNothing | IfExistsInvalidSyntax => s"${x.name} ${Prettifier.escapeName(roleName)} IF NOT EXISTS AS COPY OF ${Prettifier.escapeName(fromRole)}"
          case _                    => s"${x.name} ${Prettifier.escapeName(roleName)} AS COPY OF ${Prettifier.escapeName(fromRole)}"
        }

      case x @ DropRole(roleName, ifExists) =>
        if (ifExists) s"${x.name} ${Prettifier.escapeName(roleName)} IF EXISTS"
        else s"${x.name} ${Prettifier.escapeName(roleName)}"

      case x @ GrantRolesToUsers(roleNames, userNames) =>
        val start = if (roleNames.length > 1) s"${x.name}S" else x.name
        s"$start ${roleNames.map(Prettifier.escapeName).mkString(", ")} TO ${userNames.map(Prettifier.escapeName).mkString(", ")}"

      case x @ RevokeRolesFromUsers(roleNames, userNames) =>
        val start = if (roleNames.length > 1) s"${x.name}S" else x.name
        s"$start ${roleNames.map(Prettifier.escapeName).mkString(", ")} FROM ${userNames.map(Prettifier.escapeName).mkString(", ")}"

      // Privilege commands
      // dbms privileges

      case x @ GrantPrivilege(DbmsPrivilege(_), _, qualifiers, roleNames) =>
        s"${x.name}${Prettifier.extractQualifierString(qualifiers)} ON DBMS TO ${Prettifier.escapeNames(roleNames)}"

      case x @ DenyPrivilege(DbmsPrivilege(_), _, qualifiers, roleNames) =>
        s"${x.name}${Prettifier.extractQualifierString(qualifiers)} ON DBMS TO ${Prettifier.escapeNames(roleNames)}"

      case x @ RevokePrivilege(DbmsPrivilege(_), _, qualifiers, roleNames, _) =>
        s"${x.name}${Prettifier.extractQualifierString(qualifiers)} ON DBMS FROM ${Prettifier.escapeNames(roleNames)}"

      // database privileges

      case x @ GrantPrivilege(DatabasePrivilege(_, dbScope), _, qualifier, roleNames) =>
        Prettifier.prettifyDatabasePrivilege(x.name, dbScope, qualifier, "TO", roleNames)

      case x @ DenyPrivilege(DatabasePrivilege(_, dbScope), _, qualifier, roleNames) =>
        Prettifier.prettifyDatabasePrivilege(x.name, dbScope, qualifier, "TO", roleNames)

      case x @ RevokePrivilege(DatabasePrivilege(_, dbScope), _, qualifier, roleNames, _) =>
        Prettifier.prettifyDatabasePrivilege(x.name, dbScope, qualifier, "FROM", roleNames)

      // graph privileges

      case x @ GrantPrivilege(GraphPrivilege(action, graphScope), resource, qualifier, roleNames) =>
        val qualifierString = Prettifier.prettifyGraphQualifier(action, qualifier)
        Prettifier.prettifyGraphPrivilege(x.name, graphScope, qualifierString, resource, "TO", roleNames)

      case x @ DenyPrivilege(GraphPrivilege(action, graphScope), resource, qualifier, roleNames) =>
        val qualifierString = Prettifier.prettifyGraphQualifier(action, qualifier)
        Prettifier.prettifyGraphPrivilege(x.name, graphScope, qualifierString, resource, "TO", roleNames)

      case x @ RevokePrivilege(GraphPrivilege(action, graphScope), resource, qualifier, roleNames, _) =>
        val qualifierString = Prettifier.prettifyGraphQualifier(action, qualifier)
        Prettifier.prettifyGraphPrivilege(x.name, graphScope, qualifierString, resource, "FROM", roleNames)

      // show privileges

      case ShowPrivileges(scope, yields, _) =>
        val (y: String, r: String) = showClausesAsString(yields)
        s"SHOW ${Prettifier.extractScope(scope)} PRIVILEGES$y$r"

      case ShowPrivilegeCommands(scope, asRevoke, yields, _) =>
        val (y: String, r: String) = showClausesAsString(yields)
        val asCommand = if (asRevoke) " AS REVOKE COMMANDS" else " AS COMMANDS"
        s"SHOW ${Prettifier.extractScope(scope)} PRIVILEGES$asCommand$y$r"

      // Database commands

      case x @ ShowDatabase(scope, yields,_) =>
        val (y: String, r: String) = showClausesAsString(yields)
        val optionalName = scope match {
          case NamedDatabaseScope(dbName) => s" ${Prettifier.escapeName(dbName)}"
          case _ => ""
        }
        s"${x.name}$optionalName$y$r"

      case x @ CreateDatabase(dbName, ifExistsDo, waitUntilComplete) =>
        ifExistsDo match {
          case IfExistsDoNothing | IfExistsInvalidSyntax => s"${x.name} ${Prettifier.escapeName(dbName)} IF NOT EXISTS${waitUntilComplete.name}"
          case _                                               => s"${x.name} ${Prettifier.escapeName(dbName)}${waitUntilComplete.name}"
        }

      case x @ DropDatabase(dbName, ifExists, additionalAction, waitUntilComplete) =>
        (ifExists, additionalAction) match {
          case (false, DestroyData) => s"${x.name} ${Prettifier.escapeName(dbName)} DESTROY DATA${waitUntilComplete.name}"
          case (true, DestroyData) => s"${x.name} ${Prettifier.escapeName(dbName)} IF EXISTS DESTROY DATA${waitUntilComplete.name}"
          case (false, DumpData) => s"${x.name} ${Prettifier.escapeName(dbName)} DUMP DATA${waitUntilComplete.name}"
          case (true, DumpData) => s"${x.name} ${Prettifier.escapeName(dbName)} IF EXISTS DUMP DATA${waitUntilComplete.name}"
        }

      case x @ StartDatabase(dbName, waitUntilComplete) =>
        s"${x.name} ${Prettifier.escapeName(dbName)}${waitUntilComplete.name}"

      case x @ StopDatabase(dbName, waitUntilComplete) =>
        s"${x.name} ${Prettifier.escapeName(dbName)}${waitUntilComplete.name}"
    }
    useString + commandString
  }

  def asString(multiGraph: MultiGraphDDL): String = multiGraph match {
    case x @ CreateGraph(catalogName, query) =>
      val graphName = catalogName.parts.mkString(".")
      s"${x.name} $graphName {$NL${base.indented().queryPart(query)}$NL}"

    case x @ DropGraph(catalogName) =>
      val graphName = catalogName.parts.mkString(".")
      s"${x.name} $graphName"

    case CreateView(catalogName, params, query, _) =>
      val graphName = catalogName.parts.mkString(".")
      val paramString = params.map(p => "$" + p.name).mkString("(", ", ", ")")
      s"CATALOG CREATE VIEW $graphName$paramString {$NL${base.indented().queryPart(query)}$NL}"

    case DropView(catalogName) =>
      val graphName = catalogName.parts.mkString(".")
      s"CATALOG DROP VIEW $graphName"
  }

  private def asString(use: Option[GraphSelection]) = {
    use.filter(_ => useInCommands).map(u => base.dispatch(u) + NL).getOrElse("")
  }

  case class IndentingQueryPrettifier(indentLevel: Int = 0) extends Prettifier.QueryPrettifier {
    def indented(): IndentingQueryPrettifier = copy(indentLevel + 1)
    val INDENT: String = "  " * indentLevel

    private def asNewLine(l: String) = NL + l

    def query(q: Query): String = {
      val hint = q.periodicCommitHint.map(INDENT + "USING PERIODIC COMMIT" + _.size.map(" " + expr(_)).getOrElse("") + NL).getOrElse("")
      val query = queryPart(q.part)
      s"$hint$query"
    }

    def queryPart(part: QueryPart): String =
      part match {
        case SingleQuery(clauses) =>
          clauses.map(dispatch).mkString(NL)

        case union: Union =>
          val lhs = queryPart(union.part)
          val rhs = queryPart(union.query)
          val operation = union match {
            case _: UnionAll      => s"${INDENT}UNION ALL"
            case _: UnionDistinct => s"${INDENT}UNION"

            case u: ProjectingUnionAll      =>
              s"${INDENT}UNION ALL mappings: (${u.unionMappings.map(asString).mkString(", ")})"
            case u: ProjectingUnionDistinct =>
              s"${INDENT}UNION mappings: (${u.unionMappings.map(asString).mkString(", ")})"
          }
          Seq(lhs, operation, rhs).mkString(NL)
      }

    private def asString(u: UnionMapping): String = {
      s"${u.unionVariable.name}: [${u.variableInPart.name}, ${u.variableInQuery.name}]"
    }

    def asString(clause: Clause): String = dispatch(clause)

    def dispatch(clause: Clause): String = clause match {
      case u: UseGraph          => asString(u)
      case f: FromGraph         => asString(f)
      case e: Return            => asString(e)
      case m: Match             => asString(m)
      case c: SubQuery          => asString(c)
      case w: With              => asString(w)
      case y: Yield             => asString(y)
      case c: Create            => asString(c)
      case u: Unwind            => asString(u)
      case u: UnresolvedCall    => asString(u)
      case s: ShowIndexesClause => asString(s)
      case s: SetClause         => asString(s)
      case r: Remove            => asString(r)
      case d: Delete            => asString(d)
      case m: Merge             => asString(m)
      case l: LoadCSV           => asString(l)
      case f: Foreach           => asString(f)
      case s: Start             => asString(s)
      case c =>
        val ext = extension.asString(this)
        ext.applyOrElse(c, fallback)
    }

    private def fallback(clause: Clause): String =
      clause.asCanonicalStringVal

    def asString(u: UseGraph): String =
      s"${INDENT}USE ${expr(u.expression)}"

    def asString(f: FromGraph): String =
      s"${INDENT}FROM ${expr(f.expression)}"

    def asString(m: Match): String = {
      val o = if (m.optional) "OPTIONAL " else ""
      val p = expr.patterns.apply(m.pattern)
      val ind = indented()
      val w = m.where.map(ind.asString).map(asNewLine).getOrElse("")
      val h = m.hints.map(ind.asString).map(asNewLine).mkString
      s"$INDENT${o}MATCH $p$h$w"
    }

    def asString(c: SubQuery): String = {
      s"""${INDENT}CALL {
         |${indented().queryPart(c.part)}
         |$INDENT}""".stripMargin
    }

    def asString(w: Where): String =
      s"${INDENT}WHERE ${expr(w.expression)}"

    def asString(m: UsingHint): String = {
      m match {
        case UsingIndexHint(v, l, ps, s) => Seq(
          s"${INDENT}USING INDEX ", if (s == SeekOnly) "SEEK " else "",
          expr(v), ":", expr(l),
          ps.map(expr(_)).mkString("(", ",", ")")
        ).mkString

        case UsingScanHint(v, l) => Seq(
          s"${INDENT}USING SCAN ", expr(v), ":", expr(l)
        ).mkString

        case UsingJoinHint(vs) => Seq(
          s"${INDENT}USING JOIN ON ", vs.map(expr(_)).toIterable.mkString(", ")
        ).mkString
      }
    }

    def asString(ma: MergeAction): String = ma match {
      case OnMatch(set)  => s"${INDENT}ON MATCH ${asString(set)}"
      case OnCreate(set) => s"${INDENT}ON CREATE ${asString(set)}"
    }

    def asString(m: Merge): String = {
      val p = expr.patterns.apply(m.pattern)
      val ind = indented()
      val a = m.actions.map(ind.asString).map(asNewLine).mkString
      s"${INDENT}MERGE $p$a"
    }

    def asString(o: Skip): String = s"${INDENT}SKIP ${expr(o.expression)}"
    def asString(o: Limit): String = s"${INDENT}LIMIT ${expr(o.expression)}"

    def asString(o: OrderBy): String = s"${INDENT}ORDER BY " + {
      o.sortItems.map {
        case AscSortItem(expression)  => expr(expression) + " ASCENDING"
        case DescSortItem(expression) => expr(expression) + " DESCENDING"
      }.mkString(", ")
    }

    def asString(r: ReturnItem): String = r match {
      case AliasedReturnItem(e, v)   => expr(e) + " AS " + expr(v)
      case UnaliasedReturnItem(e, _) => expr(e)
    }

    def asString(r: ReturnItems): String = {
      val as = if (r.includeExisting) Seq("*") else Seq()
      val is = r.items.map(asString)
      (as ++ is).mkString(", ")
    }

    def asString(r: Return): String = {
      val d = if (r.distinct) " DISTINCT" else ""
      val i = asString(r.returnItems)
      val ind = indented()
      val o = r.orderBy.map(ind.asString).map(asNewLine).getOrElse("")
      val l = r.limit.map(ind.asString).map(asNewLine).getOrElse("")
      val s = r.skip.map(ind.asString).map(asNewLine).getOrElse("")
      s"${INDENT}RETURN$d $i$o$s$l"
    }

    def asString(w: With): String = {
      val d = if (w.distinct) " DISTINCT" else ""
      val i = asString(w.returnItems)
      val ind = indented()
      val o = w.orderBy.map(ind.asString).map(asNewLine).getOrElse("")
      val l = w.limit.map(ind.asString).map(asNewLine).getOrElse("")
      val s = w.skip.map(ind.asString).map(asNewLine).getOrElse("")
      val wh = w.where.map(ind.asString).map(asNewLine).getOrElse("")
      s"${INDENT}WITH$d $i$o$s$l$wh"
    }

    def asString(y: Yield): String = {
      val i = asString(y.returnItems)
      val ind = indented()
      val o = y.orderBy.map(ind.asString).map(asNewLine).getOrElse("")
      val l = y.limit.map(ind.asString).map(asNewLine).getOrElse("")
      val s = y.skip.map(ind.asString).map(asNewLine).getOrElse("")
      val wh = y.where.map(ind.asString).map(asNewLine).getOrElse("")
      s"${INDENT}YIELD $i$o$s$l$wh"
    }

    def asString(c: Create): String = {
      val p = expr.patterns.apply(c.pattern)
      s"${INDENT}CREATE $p"
    }

    def asString(u: Unwind): String = {
      s"${INDENT}UNWIND ${expr(u.expression)} AS ${expr(u.variable)}"
    }

    def asString(u: UnresolvedCall): String = {
      val namespace = expr(u.procedureNamespace)
      val prefix = if (namespace.isEmpty) "" else namespace + "."
      val args = u.declaredArguments.map(_.filter {
        case CoerceTo(_: ImplicitProcedureArgument, _) => false
        case _: ImplicitProcedureArgument              => false
        case _                                         => true
      })
      val arguments = args.map(list => list.map(expr(_)).mkString("(", ", ", ")")).getOrElse("")
      val ind = indented()
      val yields = if (u.yieldAll) asNewLine(s"${indented().INDENT}YIELD *")
      else u.declaredResult.filter(_.items.nonEmpty).map(ind.asString).map(asNewLine).getOrElse("")
      s"${INDENT}CALL $prefix${expr(u.procedureName)}$arguments$yields"
    }

    def asString(r: ProcedureResult): String = {
      def item(i: ProcedureResultItem) = i.output.map(expr(_) + " AS ").getOrElse("") + expr(i.variable)
      val items = r.items.map(item).mkString(", ")
      val ind = indented()
      val where = r.where.map(ind.asString).map(asNewLine).getOrElse("")
      s"${INDENT}YIELD $items$where"
    }

    def asString(s: ShowIndexesClause): String = {
      val indexType = if (s.all) "ALL" else "BTREE"
      val indexOutput = s match {
        case ShowIndexesClause(_, _, true, _, _, false) => " BRIEF"
        case ShowIndexesClause(_, _, _, true, _, false) => " VERBOSE"
        case _ => ""
      }
      val ind = indented()
      val where = s.where.map(ind.asString).map(asNewLine).getOrElse("")
      s"SHOW $indexType INDEXES$indexOutput$where"
    }

    def asString(s: SetClause): String = {
      val items = s.items.map {
        case SetPropertyItem(prop, exp)                       => s"${expr(prop)} = ${expr(exp)}"
        case SetLabelItem(variable, labels)                   => expr(variable) + labels.map(l => s":${expr(l)}").mkString("")
        case SetIncludingPropertiesFromMapItem(variable, exp) => s"${expr(variable)} += ${expr(exp)}"
        case SetExactPropertiesFromMapItem(variable, exp)     => s"${expr(variable)} = ${expr(exp)}"
        case _                                                => s.asCanonicalStringVal
      }
      s"${INDENT}SET ${items.mkString(", ")}"
    }

    def asString(r: Remove): String = {
      val items = r.items.map {
        case RemovePropertyItem(prop)          => s"${expr(prop)}"
        case RemoveLabelItem(variable, labels) => expr(variable) + labels.map(l => s":${expr(l)}").mkString("")
        case _                                 => r.asCanonicalStringVal
      }
      s"${INDENT}REMOVE ${items.mkString(", ")}"
    }

    def asString(v: LoadCSV): String = {
      val withHeaders = if (v.withHeaders) " WITH HEADERS" else ""
      val url = expr(v.urlString)
      val varName = expr(v.variable)
      val fieldTerminator = v.fieldTerminator.map(x => " FIELDTERMINATOR " + expr(x)).getOrElse("")
      s"${INDENT}LOAD CSV$withHeaders FROM $url AS $varName$fieldTerminator"
    }

    def asString(delete: Delete): String = {
      val detach = if (delete.forced) "DETACH " else ""
      s"$INDENT${detach}DELETE ${delete.expressions.map(expr(_)).mkString(", ")}"
    }

    def asString(foreach: Foreach): String = {
      val varName = expr(foreach.variable)
      val list = expr(foreach.expression)
      val updates = foreach.updates.map(dispatch).mkString(s"$NL  ", s"$NL  ", NL)
      s"${INDENT}FOREACH ( $varName IN $list |$updates)"
    }

    def asString(start: Start): String = {
      val startItems =
        start.items.map {
          case AllNodes(v)                                               => s"${expr(v)} = NODE( * )"
          case NodeByIds(v, ids)                                         => s"${expr(v)} = NODE( ${ids.map(expr(_)).mkString(", ")} )"
          case NodeByParameter(v, param: Parameter)                      => s"${expr(v)} = NODE( ${expr(param)} )"
          case NodeByParameter(v, param: ParameterWithOldSyntax)         => s"${expr(v)} = NODE( ${expr(param)} )"
          case AllRelationships(v)                                       => s"${expr(v)} = RELATIONSHIP( * )"
          case RelationshipByIds(v, ids)                                 => s"${expr(v)} = RELATIONSHIP( ${ids.map(expr(_)).mkString(", ")} )"
          case RelationshipByParameter(v, param: Parameter)              => s"${expr(v)} = RELATIONSHIP( ${expr(param)} )"
          case RelationshipByParameter(v, param: ParameterWithOldSyntax) => s"${expr(v)} = RELATIONSHIP( ${expr(param)} )"
        }

      val ind = indented()
      val where = start.where.map(ind.asString).map(asNewLine).getOrElse("")
      s"${INDENT}START ${startItems.mkString(s",$NL      ")}$where"
    }
  }
}

object Prettifier {

  trait QueryPrettifier {
    def INDENT: String
    def asString(clause: Clause): String
  }

  trait ClausePrettifier {
    def asString(ctx: QueryPrettifier): PartialFunction[Clause, String]
  }

  object EmptyExtension extends ClausePrettifier {
    def asString(ctx: QueryPrettifier): PartialFunction[Clause, String] = PartialFunction.empty
  }

  def extractScope(scope: ShowPrivilegeScope): String = {
    scope match {
      case ShowUserPrivileges(name) =>
        if(name.isDefined)
          s"USER ${escapeName(name.get)}"
        else
          "USER"
      case ShowUsersPrivileges(names) =>
        if (names.size == 1)
          s"USER ${escapeName(names.head)}"
        else
          s"USERS ${escapeNames(names)}"
      case ShowRolesPrivileges(names) =>
        if (names.size == 1)
          s"ROLE ${escapeName(names.head)}"
        else
          s"ROLES ${escapeNames(names)}"
      case ShowAllPrivileges()      => "ALL"
      case _                        => "<unknown>"
    }
  }

  def revokeOperation(operation: String, revokeType: String) = s"$operation($revokeType)"

  def prettifyDatabasePrivilege(privilegeName: String,
                                dbScope: List[DatabaseScope],
                                qualifier: List[PrivilegeQualifier],
                                preposition: String,
                                roleNames: Seq[Either[String, Parameter]]): String = {
    val (dbName, default, multiple) = Prettifier.extractDbScope(dbScope)
    val db = if (default) {
      s"DEFAULT DATABASE"
    } else if (multiple) {
      s"DATABASES $dbName"
    } else {
      s"DATABASE $dbName"
    }
    s"$privilegeName${extractQualifierString(qualifier)} ON $db $preposition ${escapeNames(roleNames)}"
  }

  def prettifyGraphPrivilege(privilegeName: String,
                             graphScope: List[GraphScope],
                             qualifierString: String,
                             resource: Option[ActionResource],
                             preposition: String,
                             roleNames: Seq[Either[String, Parameter]]): String = {

    val resourceName = resource match {
      case Some(PropertyResource(name)) => s" {${ExpressionStringifier.backtick(name)}}"
      case Some(PropertiesResource(names)) => s" {${names.map(ExpressionStringifier.backtick(_)).mkString(", ")}}"
      case Some(AllPropertyResource()) => " {*}"
      case Some(LabelsResource(names)) => s" ${names.map(ExpressionStringifier.backtick(_)).mkString(", ")}"
      case Some(AllLabelResource()) => " *"
      case None => ""
      case _ => throw new IllegalStateException(s"Unknown resource: $resource")
    }
    val scope = s"${extractGraphScope(graphScope)}"
    s"$privilegeName$resourceName ON $scope$qualifierString $preposition ${Prettifier.escapeNames(roleNames)}"
  }

  def prettifyGraphQualifier(action: GraphAction, qualifier: List[PrivilegeQualifier]): String = {
    // For WRITE, we don't want to print out the qualifier. For SET and REMOVE LABEL, it is printed out in another position.
    if (action.name.equals("WRITE") || action.name.equals("SET LABEL") || action.name.equals("REMOVE LABEL")) {
      ""
    } else {
      extractQualifierString(qualifier)
    }
  }

  def extractQualifierPart(qualifier: List[PrivilegeQualifier]): Option[String] = {
    def stringifyQualifiedName(nameSpace: Namespace, name: String) = {
      val namespace = nameSpace.parts.map(ExpressionStringifier.backtick(_, globbing = true)).mkString(".")
      val prefix = if (namespace.isEmpty) "" else namespace + "."
      prefix + ExpressionStringifier.backtick(name, globbing = true)
    }

    def stringify: PartialFunction[PrivilegeQualifier,String] = {
      case LabelQualifier(name) => ExpressionStringifier.backtick(name)
      case RelationshipQualifier(name) => ExpressionStringifier.backtick(name)
      case ElementQualifier(name) => ExpressionStringifier.backtick(name)
      case UserQualifier(name) => escapeName(name)
      case ProcedureQualifier(nameSpace, procedureName) =>
        stringifyQualifiedName(nameSpace, procedureName.name)
      case FunctionQualifier(nameSpace, functionName) =>
        stringifyQualifiedName(nameSpace, functionName.name)
    }

    qualifier match {
      case l@LabelQualifier(_) :: Nil => Some("NODE " + l.map(stringify).mkString(", "))
      case l@LabelQualifier(_) :: _ => Some("NODES " + l.map(stringify).mkString(", "))
      case LabelAllQualifier() :: Nil => Some("NODES *")
      case rels@RelationshipQualifier(_) :: Nil => Some("RELATIONSHIP " + rels.map(stringify).mkString(", "))
      case rels@RelationshipQualifier(_) :: _ => Some("RELATIONSHIPS " + rels.map(stringify).mkString(", "))
      case RelationshipAllQualifier() :: Nil => Some("RELATIONSHIPS *")
      case elems@ElementQualifier(_) :: _ => Some("ELEMENTS " + elems.map(stringify).mkString(", "))
      case ElementsAllQualifier() :: Nil => Some("ELEMENTS *")
      case UserQualifier(user) :: Nil => Some("(" + escapeName(user) + ")")
      case users@UserQualifier(_) :: _ => Some("(" + users.map(stringify).mkString(", ") + ")")
      case UserAllQualifier() :: Nil => Some("(*)")
      case AllQualifier() :: Nil => None
      case AllDatabasesQualifier() :: Nil => None
      case p@ProcedureQualifier(_, _) :: _ => Some(p.map(stringify).mkString(", "))
      case ProcedureAllQualifier() :: Nil => Some("*")
      case p@FunctionQualifier(_, _) :: _ => Some(p.map(stringify).mkString(", "))
      case FunctionAllQualifier() :: Nil => Some("*")
      case _ => Some("<unknown>")
    }
  }

  private def extractQualifierString(qualifier: List[PrivilegeQualifier]): String = {
    val qualifierPart = extractQualifierPart(qualifier)
    qualifierPart match {
      case Some(string) => s" $string"
      case _ => ""
    }
  }

  def extractDbScope(dbScope: List[DatabaseScope]): (String, Boolean, Boolean) = dbScope match {
    case NamedDatabaseScope(name) :: Nil => (escapeName(name), false, false)
    case AllDatabasesScope() :: Nil => ("*", false, false)
    case DefaultDatabaseScope() :: Nil => ("DEFAULT", true, false)
    case namedDatabaseScopes => (escapeNames(namedDatabaseScopes.collect { case NamedDatabaseScope(name) => name }), false, true)
  }

    def extractGraphScope(graphScope: List[GraphScope]): String = {
      graphScope match {
        case NamedGraphScope(name) :: Nil => s"GRAPH ${escapeName(name)}"
        case AllGraphsScope() :: Nil => "GRAPH *"
        case DefaultGraphScope() :: Nil => "DEFAULT GRAPH"
        case namedGraphScopes => s"GRAPHS ${escapeNames(namedGraphScopes.collect { case NamedGraphScope(name) => name })}"
      }
  }

  def escapeName(name: Either[String, Parameter]): String = name match {
    case Left(s) => ExpressionStringifier.backtick(s)
    case Right(p) => s"$$${ExpressionStringifier.backtick(p.name)}"
  }

  def escapeNames(names: Seq[Either[String, Parameter]]): String = names.map(escapeName).mkString(", ")

}
