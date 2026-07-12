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
  
  lazy val hubBaseUrl: String = servicesConfig.getString("income-tax-view-change-frontend.baseUrl")
  lazy val hubAgentBaseUrl: String = s"${hubBaseUrl}/agents"
  
  lazy val individualHomeUrl: String =
    hubBaseUrl

  lazy val individualHomeUrlWithOrigin: Option[String] => String = origin =>
    origin.fold(hubBaseUrl)(o =>s"$hubBaseUrl?origin=$o")

  lazy val homePageUrl: String = {
    individualHomeUrl
  }

  lazy val agentHomeUrl: String =
    hubAgentBaseUrl
    
  def homePageUrl(isAgent: Boolean): String = if (isAgent) agentHomeUrl else individualHomeUrl

  lazy val enterClientsUTRUrl: String =
    s"$hubAgentBaseUrl/client-utr"
  lazy val confirmClientUTRUrl: String =
    s"$hubAgentBaseUrl/confirm-client-details"
  
  //Obligation routes
  
  lazy val obligationsBaseUrl: String = servicesConfig.getString("income-tax-obligations-frontend.baseUrl")
  lazy val obligationsAgentBaseUrl: String = s"$obligationsBaseUrl/agents"
  
  lazy val obligationsWaitToSignUpIndividualUrl: Boolean => String = newObligationsEnabled =>
    if (newObligationsEnabled)
      s"$obligationsBaseUrl/access-service-from-next-tax-year"
    else
      s"$hubBaseUrl/access-service-from-next-tax-year"

  lazy val obligationsWaitToSignUpAgentUrl: Boolean => String = newObligationsEnabled =>
    if (newObligationsEnabled)
      s"$obligationsAgentBaseUrl/view-client-from-next-tax-year"
    else
      s"$hubAgentBaseUrl/view-client-from-next-tax-year"
  

  //Financials routes

  lazy val financialsBaseUrl: String = servicesConfig.getString("income-tax-financials-frontend.baseUrl")
  lazy val financialsAgentBaseUrl: String = s"$financialsBaseUrl/agents"

  lazy val financialsWhatYouOweIndividualUrl: (Boolean, Option[String]) => String = (financialsFrontendEnabled, origin) =>
    if (financialsFrontendEnabled)
      s"$financialsBaseUrl/what-you-owe${origin.fold("")(o => s"?origin=$o")}"
    else
      s"$hubBaseUrl/what-you-owe${origin.fold("")(o => s"?origin=$o")}"

  lazy val financialsWhatYouOweAgentUrl: Boolean => String = financialsFrontendEnabled =>
    if (financialsFrontendEnabled)
      s"$financialsAgentBaseUrl/what-your-client-owes"
    else
      s"$hubAgentBaseUrl/what-your-client-owes"

  def financialsWhatYouOweUrl(isAgent: Boolean, origin: Option[String] = None, financialsFrontendEnabled: Boolean): String =
    if (isAgent)
      financialsWhatYouOweAgentUrl(financialsFrontendEnabled)
    else
      financialsWhatYouOweIndividualUrl(financialsFrontendEnabled, origin)

  lazy val financialsAmendablePoaIndividualUrl: (Boolean) => String = financialsFrontendEnabled =>
    if (financialsFrontendEnabled)
      s"$financialsBaseUrl/adjust-poa/start"
    else
      s"$hubBaseUrl/adjust-poa/start"

  lazy val financialsAmendablePoaAgentUrl: Boolean => String = financialsFrontendEnabled =>
    if (financialsFrontendEnabled)
      s"$financialsAgentBaseUrl/adjust-poa/start"
    else
      s"$hubAgentBaseUrl/adjust-poa/start"

  def financialsAmendablePoaUrl(isAgent: Boolean, financialsFrontendEnabled: Boolean): String =
    if (isAgent)
      financialsAmendablePoaAgentUrl(financialsFrontendEnabled)
    else
      financialsAmendablePoaIndividualUrl(financialsFrontendEnabled)

  def financialsChargeSummaryIndividualUrl(taxYear: Int,
                                           transactionId: String,
                                           isAccruingInterest: Boolean,
                                           origin: Option[String] = None,
                                           financialsFrontendEnabled: Boolean): String = {
    lazy val queryPathNoOrigin = s"?id=$transactionId&isInterestCharge=$isAccruingInterest"
    lazy val queryPathString = origin.fold(queryPathNoOrigin)(o => s"$queryPathNoOrigin&origin=$o")
    if (financialsFrontendEnabled)
      s"$financialsBaseUrl/tax-years/$taxYear/charge$queryPathString"
    else
      s"$hubBaseUrl/tax-years/$taxYear/charge$queryPathString"
  }

  def financialsChargeSummaryAgentUrl(taxYear: Int,
                                      transactionId: String,
                                      isAccruingInterest: Boolean,
                                      financialsFrontendEnabled: Boolean): String = {
    lazy val queryPathString = s"?id=$transactionId&isInterestCharge=$isAccruingInterest"
    if (financialsFrontendEnabled)
      s"$financialsAgentBaseUrl/tax-years/$taxYear/charge$queryPathString"
    else
      s"$hubAgentBaseUrl/tax-years/$taxYear/charge$queryPathString"
  }
}
