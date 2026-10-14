Feature: Applying promotions at checkout

  Scenario: Flat discount beats buy 2 get 1
    Given a cart for store "DMART-MUMBAI-12"
    And the cart has 3 of "MAGGI-70G" priced at 14
    And the cart has 1 of "COKE-1L" priced at 60
    When the cart is checked out
    Then the response status is 200
    And the total discount is 20
    And the final amount is 82
    And the applied promotion is "Rs 20 off Coke"

  Scenario: Buy 2 get 1 gives two free items for six
    Given a cart for store "DMART-MUMBAI-12"
    And the cart has 6 of "MAGGI-70G" priced at 14
    When the cart is checked out
    Then the response status is 200
    And the total discount is 28
    And the applied promotion is "Buy 2 Get 1 Maggi"

  Scenario: Expired offer is ignored
    Given a cart for store "DMART-MUMBAI-12"
    And the cart has 1 of "RICE-25KG" priced at 1500
    When the cart is checked out
    Then the total discount is 150
    And the applied promotion is "10% off above Rs 1000"

  Scenario: Empty cart is rejected
    Given a cart for store "DMART-MUMBAI-12"
    When the cart is checked out
    Then the response status is 400