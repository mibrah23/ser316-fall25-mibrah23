import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * STARTER Test Suite for Assignment 3: White-Box Testing
 *
 * This file contains white-box tests for methods in Order.java (NOT processOrderItem).
 * These tests are designed using white-box techniques like:
 * - Control Flow Graph (CFG) analysis
 * - Statement/branch coverage
 * - Path coverage
 *
 * ASSIGNMENT 3 TEST FILES:
 * 1. WhiteBoxTestStarter.java (this file) - White-box tests for helper methods
 *    - Students: Add tests for canModifyOrder(), calculateBillSplit(), etc.
 *    - Use CFG analysis and coverage tools (JaCoCo)
 *
 * 2. ProcessOrderItemTest.java (students create this) - Tests for YOUR implementation
 *    - Test your own processOrderItem() implementation (simplified spec)
 *    - Use TDD: write tests first, then implement
 *    - Cover return codes: 0.0, 2.0, 2.1, 3.0, 3.1, 4.1, 5.0
 */
public class WhiteBoxTestStarter {

    private Order order;
    private Table table;
    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {
        order = new Order();
        table = new Table(1, 4);
        restaurant = new Restaurant("Test Restaurant");
        order.initOrder(table, "Test Customer");
    }


    // Control Flow Tests for canModifyOrder()


    /*
      CFG Path 1: Node 1 → 2 → 3
      Tests: orderStatus >= 2 (first condition true)

      Expected: return false
      Coverage: Nodes 1, 2, 3 | Edge 2→3
     */
    @Test
    public void testCanModifyOrder_StatusAtLimit_ReturnsFalse() {
        // Arrange: Set status to 2 (at boundary)
        order.setOrderStatus(2);

        // Act
        boolean result = order.canModifyOrder();

        // Assert
        assertFalse(result, "Should return false when orderStatus >= 2");
    }

    /*
      CFG Path 2: Node 1 → 2 → 4 → 5
      Tests: orderStatus < 2, items.size() >= MAX_TOTAL_ITEMS (second condition true)

      Expected: return false
      Coverage: Nodes 1, 2, 4, 5 | Edges 2→4, 4→5
     */
    @Test
    public void testCanModifyOrder_ItemsAtLimit_ReturnsFalse() {
        // Arrange: Add 5 items (MAX_TOTAL_ITEMS = 5)
        MenuItem burger = restaurant.getMenuItem("B001");
        for (int i = 0; i < 5; i++) {
            order.processOrderItem(burger, null);
        }

        // Act
        boolean result = order.canModifyOrder();

        // Assert
        assertFalse(result, "Should return false when items.size() >= MAX_TOTAL_ITEMS");
        assertEquals(5, order.getItems().size(), "Should have 5 items");
    }

    /*
      CFG Path 3: Node 1 → 2 → 4 → 6 → 7
      Tests: orderStatus < 2, items < 5, totalPrice >= MAX_ORDER_TOTAL (third condition true)

      Expected: return false
      Coverage: Nodes 1, 2, 4, 6, 7 | Edges 2→4, 4→6, 6→7
     */
    @Test
    public void testCanModifyOrder_PriceAtLimit_ReturnsFalse() {
        // Arrange: Add items to reach $100 total (MAX_ORDER_TOTAL = 100.0)
        MenuItem testItem = new MenuItem("T001", "Expensive Item", 100.0, "ENTREE");
        testItem.addAllowedModifier("EXTRA_CHEESE");
        order.processOrderItem(testItem, null);

        // Act
        boolean result = order.canModifyOrder();

        // Assert
        assertFalse(result, "Should return false when totalPrice >= MAX_ORDER_TOTAL");
        assertEquals(100.0, order.getTotalPrice(), 0.01, "Total should be at limit");
    }

    /*
      CFG Path 4: Node 1 → 2 → 4 → 6 → 8
      Tests: All conditions false (happy path)

      Expected: return true
      Coverage: Nodes 1, 2, 4, 6, 8 | Edges 2→4, 4→6, 6→8 (all "No" branches)
     */
    @Test
    public void testCanModifyOrder_AllConditionsFalse_ReturnsTrue() {


        // Act
        boolean result = order.canModifyOrder();

        // Assert
        assertTrue(result, "Should return true when all conditions are false");
        assertEquals(0, order.getOrderStatus(), "Status should be 0");
        assertEquals(0, order.getItems().size(), "Should have 0 items");
        assertEquals(0.0, order.getTotalPrice(), 0.01, "Total should be 0");
    }


    // Additional Edge Case Tests


    /*
      Boundary test: orderStatus at boundary - 1
      Tests: orderStatus = 1 (just below limit)
     */
    @Test
    public void testCanModifyOrder_StatusBelowLimit_ReturnsTrue() {
        order.setOrderStatus(1);

        boolean result = order.canModifyOrder();

        assertTrue(result, "Should return true when orderStatus = 1");
    }

    /*
      Boundary test: items.size() at boundary - 1
      Tests: 4 items (just below limit of 5)
     */
    @Test
    public void testCanModifyOrder_ItemsBelowLimit_ReturnsTrue() {
        MenuItem burger = restaurant.getMenuItem("B001");
        for (int i = 0; i < 4; i++) {
            order.processOrderItem(burger, null);
        }

        boolean result = order.canModifyOrder();

        assertTrue(result, "Should return true when items = 4");
    }

    /*
      Boundary test: totalPrice at boundary - 1
      Tests: totalPrice just below $100
     */
    @Test
    public void testCanModifyOrder_PriceBelowLimit_ReturnsTrue() {
        MenuItem testItem = new MenuItem("T001", "Item", 99.99, "ENTREE");
        testItem.addAllowedModifier("EXTRA_CHEESE");
        order.processOrderItem(testItem, null);

        boolean result = order.canModifyOrder();

        assertTrue(result, "Should return true when total = $99.99");
    }
}