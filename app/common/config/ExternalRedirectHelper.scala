/*
 * Copyright 2026 HM Revenue & Customs
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

package common.config

import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

trait ExternalRedirectHelper {

  val servicesConfig: ServicesConfig
  val config: Configuration

  private lazy val hubBaseUrl: String = servicesConfig.getString("income-tax-view-change-frontend.hubBaseUrl")

  private lazy val hubAgentBaseUrl: String = s"$hubBaseUrl/agents"

  lazy val individualHomeUrl: String = s"$hubBaseUrl/income-tax"

  def individualHomeUrlWithOrigin(origin: Option[String]): String =
    origin.fold(individualHomeUrl)(o => s"$individualHomeUrl?origin=$o")

  lazy val agentHomeUrl: String = s"$hubAgentBaseUrl/client-income-tax"

  def homePageUrl(isAgent: Boolean, origin: Option[String] = None): String =
    if (isAgent) agentHomeUrl else individualHomeUrlWithOrigin(origin)

  lazy val individualYourTasksUrl: String = s"$hubBaseUrl/your-tasks"

  lazy val agentYourTasksUrl: String = s"$hubAgentBaseUrl/your-tasks"


  lazy val enterClientsUTRUrl: String = s"$hubAgentBaseUrl/client-utr"

  lazy val confirmClientUTRUrl: String = s"$hubAgentBaseUrl/confirm-client-details"

  //Business Details routes

  private lazy val businessDetailsBaseUrl: String = servicesConfig.getString("income-tax-business-details-frontend.baseUrl")
  private lazy val businessDetailsAgentBaseUrl: String = s"$businessDetailsBaseUrl/agents"

  def triggeredMigrationCompleteStepsUrl(isAgent: Boolean): String = {
      val baseUri = if (isAgent) businessDetailsAgentBaseUrl else businessDetailsBaseUrl
      s"$baseUri/complete-steps"
    }

  //Financials routes

  private lazy val financialsBaseUrl: String = servicesConfig.getString("income-tax-financials-frontend.baseUrl")
  private lazy val financialsAgentBaseUrl: String = s"$financialsBaseUrl/agents"

  private lazy val financialsWhatYouOweIndividualUrl: Option[String] => String = origin =>
    s"$financialsBaseUrl/what-you-owe${origin.fold("")(o => s"?origin=$o")}"


  private lazy val financialsWhatYouOweAgentUrl: String = s"$financialsAgentBaseUrl/what-you-owe"

  def financialsWhatYouOweUrl(isAgent: Boolean, origin: Option[String] = None): String =
    if (isAgent)
      financialsWhatYouOweAgentUrl
    else
      financialsWhatYouOweIndividualUrl(origin)

  lazy val financialsAmendablePoaIndividualUrl: String = s"$financialsBaseUrl/adjust-poa/start"

  lazy val financialsAmendablePoaAgentUrl: String = s"$financialsAgentBaseUrl/adjust-poa/start"


  def financialsAmendablePoaUrl(isAgent: Boolean): String =
    if (isAgent)
      financialsAmendablePoaAgentUrl
    else
      financialsAmendablePoaIndividualUrl

  def financialsChargeSummaryIndividualUrl(taxYear: Int,
                                           transactionId: String,
                                           isAccruingInterest: Boolean = false,
                                           origin: Option[String] = None): String = {
    lazy val queryPathNoOrigin = s"?id=$transactionId&isInterestCharge=$isAccruingInterest"
    lazy val queryPathString = origin.fold(queryPathNoOrigin)(o => s"$queryPathNoOrigin&origin=$o")
      s"$financialsBaseUrl/tax-years/$taxYear/charge$queryPathString"
  }

  def financialsChargeSummaryAgentUrl(taxYear: Int,
                                      transactionId: String,
                                      isAccruingInterest: Boolean = false): String = {
    lazy val queryPathString = s"?id=$transactionId&isInterestCharge=$isAccruingInterest"
      s"$financialsAgentBaseUrl/tax-years/$taxYear/charge$queryPathString"
  }

  private lazy val financialsPaymentHistoryIndividualUrl: String = s"$financialsBaseUrl/payment-refund-history"

  private lazy val financialsPaymentHistoryAgentUrl: String = s"$financialsAgentBaseUrl/payment-refund-history"

  def financialsPaymentHistoryUrl(isAgent: Boolean): String =
    if (isAgent)
      financialsPaymentHistoryAgentUrl
    else
      financialsPaymentHistoryIndividualUrl

}
