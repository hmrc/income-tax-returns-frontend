/*
 * Copyright 2023 HM Revenue & Customs
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

package common.models.incomeSourceDetails

import common.enums.TriggeredMigration.Channel.{CustomerLed, HmrcConfirmed}
import play.api.libs.json.{Format, JsValue, Json, OFormat}
import play.api.Logging

sealed trait IncomeSourceDetailsResponse {
  def toJson: JsValue
}

case class IncomeSourceDetailsModel(
                                     nino: String,
                                     mtdbsa: String,
                                     yearOfMigration: Option[String],
                                     businesses: List[BusinessDetailsModel],
                                     properties: List[PropertyDetailsModel],
                                     channel: String = "Customer-led"
                                   ) extends IncomeSourceDetailsResponse with Logging {

  val hasPropertyIncome: Boolean = properties.nonEmpty
  val hasBusinessIncome: Boolean = businesses.nonEmpty
  val hasAnyIncomeSources: Boolean = hasBusinessIncome || hasPropertyIncome

  override def toJson: JsValue = Json.toJson(this)

  def earliestSubmissionTaxYear: Option[Int] = {
    val allEndYears = (businesses.flatMap(_.firstAccountingPeriodEndDate) ++ properties.flatMap(_.firstAccountingPeriodEndDate))
      .map(_.getYear)
    allEndYears.sorted.headOption
  }

  def isConfirmedUser: Boolean = {
    Set(CustomerLed.getValue, HmrcConfirmed.getValue).contains(channel)
  }
}

case class IncomeSourceDetailsError(status: Int, reason: String) extends IncomeSourceDetailsResponse {
  override def toJson: JsValue = Json.toJson(this)
}

object IncomeSourceDetailsModel {
  implicit val format: Format[IncomeSourceDetailsModel] = Json.format[IncomeSourceDetailsModel]
}

object IncomeSourceDetailsError {
  implicit val format: Format[IncomeSourceDetailsError] = Json.format[IncomeSourceDetailsError]
}
