import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
  TDD Tests for processOrderItem() - Simplified Specification
  Student: Malek Ibrahim
  ASU ID: mibrah23
  Assignment 3 - White-Box Testing

  Tests the SIMPLIFIED specification:
  Return codes: 0.0, 2.0, 2.1, 3.0, 3.1, 4.1, 5.0
  Does NOT test: 0.15, 0.20 (promotions), 2.2 (incompatible modifiers), 5.1 (max total)
 */
public class ProcessOrderItemTest {

    private Order order;
    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {
        restaurant = new Restaurant("Test Restaurant");
        Table table = new Table(1, 4);
        order = new Order(table, "Test Customer");
    }


    // TEST SUITE: Return Code 0.0 - Success


    /*
      Test adding a valid item with no modifiers
      Expected: Return 0.0, item added, price updated
     */
    @Test
    public void testValidItem_NoModifiers_ReturnsSuccess() {
        MenuItem burger = restaurant.getMenuItem("B001");

        double result = order.processOrderItem(burger, null);

        assertEquals(0.0, result, 0.01, "Should return 0.0 for successful addition");
        assertEquals(1, order.getItems().size(), "Should have 1 item");
        assertEquals(12.99, order.getTotalPrice(), 0.01, "Total should be burger price");
    }

    /*
      Test adding item with empty modifier list
      Expected: Return 0.0, item added
     */
    @Test
    public void testValidItem_EmptyModifiers_ReturnsSuccess() {
        MenuItem burger = restaurant.getMenuItem("B001");
        List<String> modifiers = new ArrayList<>();

        double result = order.processOrderItem(burger, modifiers);

        assertEquals(0.0, result, 0.01, "Should return 0.0");
        assertEquals(1, order.getItems().size(), "Should have 1 item");
    }

    /*
      Test adding item with valid modifiers
      Expected: Return 0.0, price includes modifiers
     */
    @Test
    public void testValidItem_WithValidModifiers_ReturnsSuccess() {
        MenuItem burger = restaurant.getMenuItem("B001");
        List<String> modifiers = Arrays.asList("EXTRA_CHEESE", "NO_ONIONS");

        double result = order.processOrderItem(burger, modifiers);

        assertEquals(0.0, result, 0.01, "Should return 0.0");
        assertEquals(1, order.getItems().size(), "Should have 1 item");
        // Price: 12.99 + 1.50 - 0.50 = 13.99
        assertEquals(13.99, order.getTotalPrice(), 0.01, "Total should include modifiers");
    }


    // TEST SUITE: Return Code 3.1 - Null Item


    /*
      Test processing null item
      Expected: Return 3.1, no changes to order
     */
    @Test
    public void testNullItem_ReturnsItemNull() {
        double initialTotal = order.getTotalPrice();

        double result = order.processOrderItem(null, null);

        assertEquals(3.1, result, 0.01, "Should return 3.1 for null item");
        assertEquals(0, order.getItems().size(), "Should have no items");
        assertEquals(initialTotal, order.getTotalPrice(), 0.01, "Total should not change");
    }

    // ========================================================================
    // TEST SUITE: Return Code 4.1 - Invalid Item ID
    // ========================================================================

    /*
      Test item with null ID
      Expected: Return 4.1, no changes
     */
    @Test
    public void testItemWithNullId_ReturnsInvalidId() {
        MenuItem item = new MenuItem(null, "Test Item", 10.0, "ENTREE");

        double result = order.processOrderItem(item, null);

        assertEquals(4.1, result, 0.01, "Should return 4.1 for null ID");
        assertEquals(0, order.getItems().size(), "Should have no items");
    }

    /*
      Test item with empty ID
      Expected: Return 4.1, no changes
     */
    @Test
    public void testItemWithEmptyId_ReturnsInvalidId() {
        MenuItem item = new MenuItem("", "Test Item", 10.0, "ENTREE");

        double result = order.processOrderItem(item, null);

        assertEquals(4.1, result, 0.01, "Should return 4.1 for empty ID");
        assertEquals(0, order.getItems().size(), "Should have no items");
    }

    /*
      Test item with non-alphanumeric ID
      Expected: Return 4.1, no changes
     */
    @Test
    public void testItemWithSpecialCharId_ReturnsInvalidId() {
        MenuItem item = new MenuItem("B@01!", "Test Item", 10.0, "ENTREE");

        double result = order.processOrderItem(item, null);

        assertEquals(4.1, result, 0.01, "Should return 4.1 for special char ID");
        assertEquals(0, order.getItems().size(), "Should have no items");
    }

    // ========================================================================
    // TEST SUITE: Return Code 3.0 - Item Unavailable
    // ========================================================================

    /*
      Test item marked as unavailable
      Expected: Return 3.0, no changes
     */
    @Test
    public void testUnavailableItem_ReturnsUnavailable() {
        MenuItem burger = restaurant.getMenuItem("B001");
        burger.setAvailable(false);

        double result = order.processOrderItem(burger, null);

        assertEquals(3.0, result, 0.01, "Should return 3.0 for unavailable item");
        assertEquals(0, order.getItems().size(), "Should have no items");
    }

    /*
      Test item with zero stock
      Expected: Return 3.0, no changes
     */
    @Test
    public void testItemWithZeroStock_ReturnsUnavailable() {
        MenuItem burger = restaurant.getMenuItem("B001");
        burger.setStockCount(0);

        double result = order.processOrderItem(burger, null);

        assertEquals(3.0, result, 0.01, "Should return 3.0 for zero stock");
        assertEquals(0, order.getItems().size(), "Should have no items");
    }


    // TEST SUITE: Return Code 5.0 - Order Finalized


    /*
      Test adding item when order status is 3 (delivered)
      Expected: Return 5.0, no changes
     */
    @Test
    public void testOrderFinalized_Status3_ReturnsFinalized() {
        order.setOrderStatus(3);
        MenuItem burger = restaurant.getMenuItem("B001");

        double result = order.processOrderItem(burger, null);

        assertEquals(5.0, result, 0.01, "Should return 5.0 for finalized order");
        assertEquals(0, order.getItems().size(), "Should have no items");
    }

    /*
      Test adding item when order status is 4 (paid)
      Expected: Return 5.0, no changes
     */
    @Test
    public void testOrderFinalized_Status4_ReturnsFinalized() {
        order.setOrderStatus(4);
        MenuItem burger = restaurant.getMenuItem("B001");

        double result = order.processOrderItem(burger, null);

        assertEquals(5.0, result, 0.01, "Should return 5.0 for paid order");
        assertEquals(0, order.getItems().size(), "Should have no items");
    }

    /*
      Test adding item when order status is 2 (ready, still modifiable)
      Expected: Return 0.0, item added
     */
    @Test
    public void testOrderStatus2_StillModifiable_ReturnsSuccess() {
        order.setOrderStatus(2);
        MenuItem burger = restaurant.getMenuItem("B001");

        double result = order.processOrderItem(burger, null);

        assertEquals(0.0, result, 0.01, "Should allow modifications at status 2");
        assertEquals(1, order.getItems().size(), "Should have 1 item");
    }


    // TEST SUITE: Return Code 2.0 - Quantity Limit


    /*
      Test adding 5th item of same ID (at limit, should succeed)
      Expected: Return 0.0, 5 items in order
     */
    @Test
    public void testQuantityLimit_AddFifthItem_ReturnsSuccess() {
        MenuItem burger = restaurant.getMenuItem("B001");

        // Add 4 items
        for (int i = 0; i < 4; i++) {
            order.processOrderItem(burger, null);
        }

        // Add 5th should succeed
        double result = order.processOrderItem(burger, null);

        assertEquals(0.0, result, 0.01, "Should return 0.0 for 5th item");
        assertEquals(5, order.getItemCountById("B001"), "Should have 5 items");
    }

    /*
      Test adding 6th item of same ID (exceeds limit)
      Expected: Return 2.0, still only 5 items
     */
    @Test
    public void testQuantityLimit_AddSixthItem_ReturnsQuantityLimit() {
        MenuItem burger = restaurant.getMenuItem("B001");

        // Add 5 items
        for (int i = 0; i < 5; i++) {
            order.processOrderItem(burger, null);
        }

        // Try to add 6th
        double result = order.processOrderItem(burger, null);

        assertEquals(2.0, result, 0.01, "Should return 2.0 for 6th item");
        assertEquals(5, order.getItemCountById("B001"), "Should still have only 5 items");
    }


    // TEST SUITE: Return Code 2.1 - Invalid Modifier


    /*
      Test adding item with modifier not allowed for that item
      Expected: Return 2.1, no changes
     */
    @Test
    public void testInvalidModifier_ReturnsInvalidModifier() {
        MenuItem soda = restaurant.getMenuItem("D001"); // Soda has no allowed modifiers
        List<String> modifiers = Arrays.asList("EXTRA_CHEESE");

        double result = order.processOrderItem(soda, modifiers);

        assertEquals(2.1, result, 0.01, "Should return 2.1 for invalid modifier");
        assertEquals(0, order.getItems().size(), "Should have no items");
    }

    /*
      Test with multiple modifiers where one is invalid
      Expected: Return 2.1, no changes
     */
    @Test
    public void testSomeModifiersInvalid_ReturnsInvalidModifier() {
        MenuItem burger = restaurant.getMenuItem("B001");
        // CROUTONS is not allowed on burger
        List<String> modifiers = Arrays.asList("EXTRA_CHEESE", "CROUTONS");

        double result = order.processOrderItem(burger, modifiers);

        assertEquals(2.1, result, 0.01, "Should return 2.1 when any modifier is invalid");
        assertEquals(0, order.getItems().size(), "Should have no items");
    }


    // EDGE CASES & INTEGRATION TESTS


    /*
      Test order state is not modified when validation fails
      Expected: No side effects from failed operations
     */
    @Test
    public void testValidationFailure_NoSideEffects() {
        MenuItem burger = restaurant.getMenuItem("B001");
        order.processOrderItem(burger, null); // Add one valid item

        double initialTotal = order.getTotalPrice();
        int initialSize = order.getItems().size();

        // Try to add null item
        order.processOrderItem(null, null);

        assertEquals(initialTotal, order.getTotalPrice(), 0.01, "Total should not change");
        assertEquals(initialSize, order.getItems().size(), "Item count should not change");
    }

    /*
      Test that different item IDs have separate quantity limits
      Expected: Each item ID tracked independently
     */
    @Test
    public void testQuantityLimit_DifferentItems_IndependentLimits() {
        MenuItem burger = restaurant.getMenuItem("B001");
        MenuItem salad = restaurant.getMenuItem("S001");

        // Add 5 burgers
        for (int i = 0; i < 5; i++) {
            order.processOrderItem(burger, null);
        }

        // Adding salad should still work (different ID)
        double result = order.processOrderItem(salad, null);

        assertEquals(0.0, result, 0.01, "Different item should have independent limit");
        assertEquals(6, order.getItems().size(), "Should have 6 total items");
    }
}