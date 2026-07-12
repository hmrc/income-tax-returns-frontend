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

package common.config

import com.google.inject.Inject
import play.api.Configuration
import play.api.i18n.{Lang, Messages}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import javax.inject.Singleton

@Singleton
class FrontendAppConfig @Inject()(val servicesConfig: ServicesConfig, val config: Configuration) extends ExternalRedirectHelper {

  lazy val hasEnabledTestOnlyRoutes: Boolean = config.get[String]("play.http.router") == "testOnlyDoNotUseInAppConf.Routes"

  //App
  lazy val basePath: String = servicesConfig.getString("base.context-root")
  lazy val agentBasePath: String = s"$basePath/agents"
  lazy val baseUrl: String = servicesConfig.getString("base.url")
  lazy val appName: String = servicesConfig.getString("appName")

  //Feedback Config
  private lazy val contactHost: String = servicesConfig.getString("contact-frontend.host")
  private lazy val contactFrontendService: String = servicesConfig.baseUrl("contact-frontend")
  lazy val contactFormServiceIdentifier: String = "ITVC"
  lazy val contactFrontendBaseUrl: String = s"$contactFrontendService"
  lazy val reportAProblemNonJSUrl: String = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
  lazy val betaFeedbackUrl = s"$basePath/feedback"
  lazy val agentBetaFeedbackUrl = s"$agentBasePath/feedback"
  lazy val noIncomeSourcesContactUrl: String = s"$contactHost/contact/report-technical-problem?service=$contactFormServiceIdentifier"

  //Income tax obligations service
  lazy val incomeTaxObligationsService: String = servicesConfig.baseUrl("income-tax-obligations")

  //Income tax business details service
  lazy val incomeTaxBusinessDetailsBaseUrl: String = servicesConfig.baseUrl("income-tax-business-details")

  //Income tax calculation service
  lazy val incomeTaxCalculationService: String = servicesConfig.baseUrl("income-tax-calculation")

  //Income tax financial details service
  lazy val incomeTaxFinancialDetailsService: String = servicesConfig.baseUrl("income-tax-financial-details")
  
  //View L&P
  def saViewLandPService(utr: String): String = servicesConfig.getString("old-sa-viewer-frontend.host") + s"/$utr/account"

  //individual sa302
  def sa302:String = s"$baseUrl$basePath/mortgage-evidence/proof-of-income"
  //agent sa302
  def sa302Agent:String = s"$baseUrl$agentBasePath/mortgage-evidence/proof-of-income"

  //GG Sign In via BAS Gateway
  lazy val signInUrl: String = servicesConfig.getString("base.sign-in")
  lazy val ggSignInUrl: String = servicesConfig.getString("government-gateway.sign-in.url")

  //Exit Survey
  lazy val exitSurveyBaseUrl: String = servicesConfig.getString("feedback-frontend.host") + servicesConfig.getString("feedback-frontend.url")

  def exitSurveyUrl(identifier: String): String = s"$exitSurveyBaseUrl/$identifier"

  //Sign out
  lazy val ggUrl: String = servicesConfig.getString("government-gateway.url")

  def ggSignOutUrl(identifier: String): String = s"$ggUrl/bas-gateway/sign-out-without-state?continue=${exitSurveyUrl(identifier)}"
  //Agent Services Account
  lazy val setUpAgentServicesAccountUrl: String = servicesConfig.getString("set-up-agent-services-account.url")

  //Subscription Service
  lazy val signUpUrl: String = servicesConfig.getString("mtd-subscription-service.url")

  lazy val citizenDetailsUrl: String = servicesConfig.baseUrl("citizen-details")
  
  lazy val enterSurveyUrl: String = servicesConfig.getString("enter-survey.url")

  // Submission service
  // This URL has a set year and environment. Please use submissionFrontendTaxOverviewUrl instead.
  lazy val submissionFrontendTaxOverviewUrl: Int => String = taxYear =>
    servicesConfig.getString("income-tax-submission-frontend.host") + s"/update-and-submit-income-tax-return/$taxYear/view"

  lazy val submissionFrontendFinalDeclarationUrl: Int => String = taxYear =>
    servicesConfig.getString("income-tax-submission-frontend.host") + s"/update-and-submit-income-tax-return/$taxYear/declaration"

  lazy val submissionFrontendTaxYearsPage: Int => String = taxYear =>
    servicesConfig.getString("income-tax-submission-frontend.host") + s"/update-and-submit-income-tax-return/$taxYear/start"
  
  // income-tax-session-data url
  lazy val incomeTaxSessionDataUrl: String = servicesConfig.baseUrl("income-tax-session-data")
  
  //penalties frontend
  lazy val incomeTaxPenaltiesFrontendLPP2Calculation: String => String = chargeRef => servicesConfig.getString("income-tax-penalties-frontend.homeUrl") + s"/second-lpp-calculation?penaltyId=$chargeRef"
  lazy val incomeTaxPenaltiesFrontendLPP2CalculationAgent: String => String = chargeRef => servicesConfig.getString("income-tax-penalties-frontend.homeUrl") + s"/agent-second-lpp-calculation?penaltyId=$chargeRef"

  lazy val agentServicesAccountFrontend: String = servicesConfig.baseUrl("agent-services-account-frontend")

  // Service Navigation Links
  lazy val ptaFrontendBase: String = servicesConfig.getString("personal-tax-account.url")
  lazy val btaFrontendBase: String = servicesConfig.getString("business-tax-account.url")
  lazy val helpAndContactBase: String = servicesConfig.getString("help-and-contact-frontend.url")
  lazy val trackingBase: String = servicesConfig.getString("tracking-frontend.url")

  lazy val businessTaxAccountManageAccountUrl: String = s"$btaFrontendBase/manage-account"
  lazy val businessTaxAccountMessagesUrl: String = s"$btaFrontendBase/messages"
  lazy val businessTaxAccountHelpUrl: String = s"$helpAndContactBase/business-account/help"

  lazy val personalTaxAccountMessagesUrl: String = s"$ptaFrontendBase/messages"
  lazy val personalTaxAccountCheckProgressUrl: String = s"$trackingBase/track"
  lazy val personalTaxAccountProfileUrl: String = s"$ptaFrontendBase/profile-and-settings"
  lazy val personalTaxAccountBtaUrl: String = s"$btaFrontendBase"

  //Translation
  def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy")
  )

  //Auth variables
  lazy val requiredConfidenceLevel: Int = servicesConfig.getInt("auth.confidenceLevel")

  lazy val ivUrl = servicesConfig.getString("identity-verification-frontend.host")
  lazy val relativeIVUpliftParams = servicesConfig.getBoolean("identity-verification-frontend.use-relative-params")

  def incomeSourceOverrides(): Option[Seq[String]] = config.getOptional[Seq[String]]("afterIncomeSourceCreated")

  def poaAdjustmentOverrides(): Option[Seq[String]] = config.getOptional[Seq[String]]("afterPoaAmountAdjusted")

  def triggeredMigrationOverrides(): Option[Seq[String]] = config.getOptional[Seq[String]]("afterMigration")
  
  lazy val readFeatureSwitchesFromMongo: Boolean = servicesConfig.getBoolean("feature-switches.read-from-mongo")
  
  lazy val isTimeMachineEnabled: Boolean = servicesConfig.getBoolean("feature-switch.enable-time-machine")
  lazy val timeMachineAddYears: Int = servicesConfig.getInt("time-machine.add-years")
  lazy val timeMachineAddDays: Int = servicesConfig.getInt("time-machine.add-days")

  lazy val isSessionDataStorageEnabled: Boolean = servicesConfig.getBoolean("feature-switch.enable-session-data-storage")
  
  //External-Urls

  def selfAssessmentTaxReturnLink(isAgent: Boolean)(implicit messages: Messages): String =
    messages.lang.code match {
      case _ if isAgent => "https://www.gov.uk/guidance/self-assessment-for-agents-online-service"
      case "en" => "https://www.gov.uk/self-assessment-tax-returns"
      case "cy" => "https://www.gov.uk/ffurflenni-treth-hunanasesiad/trosolwg"
      case _ => "https://www.gov.uk/self-assessment-tax-returns"
    }

  def findHmrcContactsSALink(): String =
    "https://www.gov.uk/find-hmrc-contacts/self-assessment-general-enquiries"
  
  lazy val dynamicStubUrl: String = servicesConfig.baseUrl("itvc-dynamic-stub")
}
