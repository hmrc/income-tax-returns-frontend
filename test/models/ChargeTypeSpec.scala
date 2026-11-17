/*
 * Copyright 2025 HM Revenue & Customs
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

package models

import play.api.libs.json.{JsString, JsSuccess, Json}
import testUtils.UnitSpec

class ChargeTypeSpec extends UnitSpec {

  "write" should {
    "return correct String based on the type passed in" in {
      Json.toJson[ChargeType](PoaOneDebit) shouldBe JsString("POA1")
      Json.toJson[ChargeType](PoaTwoDebit) shouldBe JsString("POA2")
      Json.toJson[ChargeType](PoaOneReconciliationDebit) shouldBe JsString("POA1RR-debit")
      Json.toJson[ChargeType](PoaTwoReconciliationDebit) shouldBe JsString("POA2RR-debit")
      Json.toJson[ChargeType](BalancingCharge) shouldBe JsString("BCD")
      Json.toJson[ChargeType](LateSubmissionPenalty) shouldBe JsString("LSP")
      Json.toJson[ChargeType](FirstLatePaymentPenalty) shouldBe JsString("LPP1")
      Json.toJson[ChargeType](SecondLatePaymentPenalty) shouldBe JsString("LPP2")
      Json.toJson[ChargeType](ITSAReturnAmendment) shouldBe JsString("IRA")
      Json.toJson[ChargeType](MfaDebitCharge) shouldBe JsString("MfaDebit")
      Json.toJson[ChargeType](ITSAOverRepayment) shouldBe JsString("ITSAOverRepayment")
    }
  }

  "read" should {
    "return correct type based on the String passed in" in {
      Json.fromJson[ChargeType](JsString("POA1")) shouldBe(JsSuccess(PoaOneDebit))
      Json.fromJson[ChargeType](JsString("POA2")) shouldBe(JsSuccess(PoaTwoDebit))
      Json.fromJson[ChargeType](JsString("POA1RR-debit")) shouldBe(JsSuccess(PoaOneReconciliationDebit))
      Json.fromJson[ChargeType](JsString("POA2RR-debit")) shouldBe(JsSuccess(PoaTwoReconciliationDebit))
      Json.fromJson[ChargeType](JsString("BCD")) shouldBe(JsSuccess(BalancingCharge))
      Json.fromJson[ChargeType](JsString("LSP")) shouldBe(JsSuccess(LateSubmissionPenalty))
      Json.fromJson[ChargeType](JsString("LPP1")) shouldBe(JsSuccess(FirstLatePaymentPenalty))
      Json.fromJson[ChargeType](JsString("LPP2")) shouldBe(JsSuccess(SecondLatePaymentPenalty))
      Json.fromJson[ChargeType](JsString("MfaDebit")) shouldBe(JsSuccess(MfaDebitCharge))
      Json.fromJson[ChargeType](JsString("ITSAOverRepayment")) shouldBe(JsSuccess(ITSAOverRepayment))

    }
  }

  "fromCode" should {
    "return correct charge type based on the String passed in" in {
      TransactionType.fromCode("4920") shouldBe Some(PoaOneDebit)
      TransactionType.fromCode("4930") shouldBe Some(PoaTwoDebit)
      TransactionType.fromCode("4911") shouldBe Some(PoaOneReconciliationDebit)
      TransactionType.fromCode("4913") shouldBe Some(PoaTwoReconciliationDebit)
      TransactionType.fromCode("4910") shouldBe Some(BalancingCharge)
      TransactionType.fromCode("4027") shouldBe Some(LateSubmissionPenalty)
      TransactionType.fromCode("4028") shouldBe Some(FirstLatePaymentPenalty)
      TransactionType.fromCode("4029") shouldBe Some(SecondLatePaymentPenalty)
      TransactionType.fromCode("4000") shouldBe Some(MfaDebitCharge)
      TransactionType.fromCode("4001") shouldBe Some(MfaDebitCharge)
      TransactionType.fromCode("4002") shouldBe Some(MfaDebitCharge)
      TransactionType.fromCode("4003") shouldBe Some(MfaDebitCharge)
      TransactionType.fromCode("5070") shouldBe Some(ITSAOverRepayment)
    }
  }
}
