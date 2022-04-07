package com.joedoe.quarkus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

@QuarkusTest
public class AccountResourceTest {
  @Test
  void testRetrieveAll() {
    Response result = given().when().get("/accounts").then().statusCode(200).body( //
        containsString("George Baird"), //
        containsString("Mary Taylor"), //
        containsString("Diana Rigg"), //
        containsString("Joedoe")). //
        extract().response();

    List<Account> accounts = result.jsonPath().getList("$");
    assertThat(accounts, not(empty()));
    assertThat(accounts, hasSize(4));
  }

  @Test
  void testGetAccount() {
    Account account = given().when().get("/accounts/{accountNumber}", 545454545).then().statusCode(200).extract()
        .as(Account.class);

    assertThat(account.getAccountNumber(), equalTo(545454545L));
    assertThat(account.getCustomerName(), equalTo("Diana Rigg"));
    assertThat(account.getBalance(), equalTo(new BigDecimal("422.00")));
    assertThat(account.getStatus(), equalTo(AccountStatus.OPEN));
  }
}
