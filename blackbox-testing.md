# Black Box Testing Report - Assignment 2

**Student Name:** Malek Ibrahim
**ASU ID:** mibrah23
**Date:** 10/29

---

## Part 1: Equivalence Partitioning

Identify equivalence partitions for the `processOrderItem()` method based on the specification.

### Input: MenuItem

| Partition | Valid/Invalid | Description | Expected Return Code |
|-----------|---------------|-------------|---------------------|
| EP1 | Valid | Valid menu item, available | 0.0 or promotion code |
| EP2 | Invalid | Null menu item | 3.1 |
| EP3 | Invalid | Menu item with invalid ID | 4.1 |
| EP4 | Invalid | Menu item unavailable (available=false) | 3.0 |



### Input: Modifiers

| Partition | Valid/Invalid | Description | Expected Return Code |
|-----------|---------------|-------------|---------------------|
| EP5 | Valid | Null, empty, or all valid modifiers for item | Process normally |
| EP6 | Invalid | Contains invalid modifier for item | 2.1 |
| EP7 | Invalid | Incompatible modifier combination (NO_X + EXTRA_X) | 2.2 |

### Other Partitions (add more as needed)

| Partition | Valid/Invalid | Description | Expected Return Code |
|-----------|---------------|-------------|---------------------|
| EP8 | Valid | Order status 0-2 (pending/preparing/ready) | Allow modifications |
| EP9 | Invalid | Order status 3-4 (delivered/paid) | 5.0|
| EP10 | Valid | Item quantity for ID: 0-4 items currently in order | Allow addition |
| EP11 | Invalid | Item quantity for ID: 5 items (at limit) | 2.0 |
| EP12 | Valid | APPETIZER category | 0.20 (20% discount) |
| EP13 | Valid | ENTREE with 2+ modifiers | 0.15 |
| EP14 | Valid | Other categories or ENTREE with <2 modifiers | 0.0|

---

## Part 2: Boundary Value Analysis

### Quantity Boundaries

| Boundary | Value | Expected Result |
|----------|-------|-----------------|
| Lower | 1 item in order | Item added successfully |
| Normal | 3 items in order | Item added successfully |
| Upper | 5 items of same ID | 5th item added successfully |
| Upper+1 | Try to add 6th item of same ID | Rejected with code 2.0 |

### Add more boundaries as needed:

---

## Part 3: Test Cases Designed

List at least 20 test cases you designed based on your EP/BVA analysis.

| Test ID | EP/BVA | Test Description | Expected Return | Expected State |
|---------|--------|------------------|-----------------|----------------|
| TC01 | EP1, EP5 | Add valid burger, no modifiers | 0.0 | 1 item, $12.99 |
| TC02 | EP2 | Add null menu item | 3.1 | No change |
| TC03 | EP3 | Add item with null ID | 4.1 | No change |
| TC04 | EP3 | Add item with special character ID (B@01!) | 4.1 | No change |
| TC05 | EP4 | Add unavailable item (available=false) | 3.0 | No change |
| TC06 | EP9, BVA | Order status = 3 (delivered) | 5.0 | No change |
| TC07 | EP9 | Order status = 4 (paid) | 5.0 | No change |
| TC08 | EP11, BVA | Add 6th item of same ID (at quantity limit) | 2.0 | No change |
| TC09XY | EP10, BVA | Add 5th item of same ID (below limit) | 0.0 | 5 items, total updated |
| TC10 | EP6 | Add burger with CROUTONS (invalid modifier) | 2.1 | No change |
| TC11 | EP7 | Add burger with NO_CHEESE + EXTRA_CHEESE | 2.2 | No change |
| TC12 | EP7 | Add burger with NO_ONIONS + EXTRA_ONIONS | 2.2 | No change |
| TC13 | EP12 | Add appetizer (Caesar Salad) | 0.20 | 1 item, $7.19 (20% off) |
| TC14 | EP13, BVA | Add ENTREE with 2 modifiers | 0.15 | 1 item, ~$11.74 (15% off) |
| TC15 | EP14, BVA | Add ENTREE with 1 modifier | 0.0 | 1 item, $14.49 |
| TC16 | EP15, BVA | Order total at $90, add $10 item (reaches $100) | 0.0 | 10 items, $100.00 |
| TC17 | EP16, BVA | Order total at $91, add $10 item (exceeds $100) | 5.1 | No change |
| TC18 | EP5 | Add burger with multiple valid modifiers | 0.0 or 0.15 | 1 item, price with mods |
| TC19 | EP5 | Add burger with empty modifier list | 0.0 | 1 item, $12.99 |
| TC20 | EP8, BVA | Order status = 2 (ready, still modifiable) | 0.0 | 1 item, total updated |
| TC21 | EP5 | Add burger with EXTRA_CHEESE (verify +$1.50) | 0.0 | 1 item, $14.49 |
| TC22 | EP4 | Add item with stock = 0 | 3.0 | No change |
| TC23 | EP14 | Add dessert (Chocolate Cake) | 0.0 | 1 item, $6.99 |
| TC24 | EP17 | Add salad with GLUTEN_FREE dietary flag | 0.20 | 1 item, $7.19 (with promo) |

---

## Part 4: Bug Analysis

### Easter Eggs Found

List any easter egg messages you discovered:
-EGG9: Got me
-

### Implementation Results

| Implementation | Status | # Tests Passed | # Tests Failed | Major Bugs Found |
|----------------|--------|----------------|----------------|------------------|
| Order0 | Buggy | 20 | 4 | 4 |
| Order1 | Buggy | 22 | 2 | 2 |
| Order2 | Buggy | 14 | 10 | 10 |
| Order3 | Buggy | 17 | 7 | 7 |
| Order4 | Buggy | 13 | 11 | 11 |

### Bugs Discovered

**Order0:**
- Bug 1: [Returns 0.2 instead of 2.0 when adding 6th item of same type] - Revealed by test: [TC08] - Boundary Error
- Bug 2: [NullPointerException when item has null ID] - Revealed by test: [TC03] - Validation Error
- Bug 3: [Returns 2.0 instead of 5.1 when order total exceeds $100] - Revealed by test: [TC17] - Logic Error
- Bug 4: [Returns 2.0 instead of 0.0 at $100 boundary] - Revealed by test: [TC16] - Boundary Error

**Order1:**
- Bug 1: [Returns 2.0 instead of 5.1 when order total exceeds $100] - Revealed by test: [TC17] - Logic Error
- Bug 2: [Returns 2.0 instead of 0.0 at $100 boundary] - Revealed by test: [TC16] - Boundary Error

**Order2:**
- Bug 1: [Doesn't add items to order] - Revealed by test: [TC01] - State management error
- Bug 2: [Returns 0.2 instead of 2.0 at quantity limit] - Revealed by test: [TC08] - Boundary Error
- Bug 3: [Fails to add items even with valid inputs] - Revealed by test: [TC01] - functional Error


**Order3:**
- Bug 1: [Returns 2.2 instead of 2.1 for invalid modifiers] - Revealed by test: [TC10] - Logic Error
- Bug 2: [Returns 2.1 instead of 2.2 for incompatible modifiers] - Revealed by test: [TC11] - Logic Error
- Bug 3: [Doesn't apply 20% appetizer discount] - Revealed by test: [TC13] - Calculation Error
- Bug 4: [Promotion calculation incorrect for entrees with 2+ modifiers] - Revealed by test: [TC14] - Calculation Error
- Bug 5: [Returns 2.0 instead of 5.1 when exceeding $100] - Revealed by test: [TC17] - Logic Error
- Bug 6: [Returns 2.0 instead of 0.0 at $100 boundary] - Revealed by test: [TC16] - Boundary Error


**Order4:**
- Bug 1: [Never updates total price (always returns $0.00)] - Revealed by tests: [TC01, 15, 19, 20, 21, 23] - Critical state management error
- Bug 2: [Doesn't apply promotions to total] - Revealed by test: [TC13] - Calculation error
- Bug 3: [Returns 2.0 instead of 5.1 when exceeding $100] - Revealed by test: [TC17] - Logic Error 
- Bug 4: [Returns 2.0 instead of 0.0 at $100 boundary] - Revealed by test: [TC16] - Boundary Error



### Most Correct Implementation

**Answer:** Order1 appears to be the most correct implementation.

**Justification:**
Order1 passed 22 out of 24 tests (91.7% pass rate), failing only on the max order total boundary checks (TC16 and TC17).While Order2 has critical functionality broken (doesn't add items), Order4 never updates prices, Order0 has a NullPointerException vulnerability, and Order3 has multiple logic errors in modifier validation and promotion application.

---

## Part 5: Reflection

**Which testing technique was most effective for finding bugs?**
Boundary value analysis was very effective for finding bugs in this assignment.

**What was the most challenging aspect of this assignment?**
The most challenging aspect was designing test cases that verify both the return code and the state changes

