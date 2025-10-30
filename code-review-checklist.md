# Code Review Checklist

**Reviewer Name:** Malek Ibrahim
**Date:** 10/29/2025
**Branch:** Review

## Instructions
Review ALL source files in the project and identify defects using the categories below. Log at least 7 defects total:
- At least 1 from CS (Coding Standards)
- At least 1 from CG (Code Quality/General)
- At least 1 from FD (Functional Defects)
- Remaining can be from any category

## Review Categories

- **CS**: Coding Standards (naming conventions, formatting, style violations)
- **CG**: Code Quality/General (design issues, code smells, maintainability)
- **FD**: Functional Defects (logic errors, incorrect behavior, bugs)
- **MD**: Miscellaneous (documentation, comments, other issues)

## Defect Log

| Defect ID | File | Line(s) | Category | Description | Severity |
|-----------|------|---------|----------|-------------|----------|
| 1 | Order.java | 137-151 | CS | Method areModifiersCompatible contains empty else blocks  | Low |
| 2 | Order.java | 153-170 | CG | calculateModifierPrice uses multiple if statements instead of a switch statement | Medium |
| 3 | Order.java | 172-186 | FD | calculatePromotion method adds promotions to appliedPromotions list every time it's called | High |
| 4 | Order.java | 374-391 | CG | processPayment method has complex nested conditionals | Medium |
| 5 | MenuItem.java | 141-145 | CG | Absence of error handling if the stock is at 0 | High |
| 6 | Restaurant.java | 107-109 | FD | Returns discount amount instead of discounted price | High |
| 7 | Restaurant.java | 87-89 | CG | removeMenuItem is private | Medium |
| 8 | | | | | |
| 9 | | | | | |
| 10 | | | | | |

**Severity Levels:**
- **Critical**: Causes system failure, data corruption, or security issues
- **High**: Major functional defect or significant quality issue
- **Medium**: Moderate issue affecting maintainability or minor functional problem
- **Low**: Minor style issue or cosmetic problem

## Example Entry

| Defect ID | File | Line(s) | Category | Description | Severity |
|-----------|------|---------|----------|-------------|----------|
| 1 | Order.java | 65 | MD | Method initOrder() is missing JavaDoc documentation | Low |

## Notes
- Be specific with line numbers
- Provide clear, actionable descriptions
- Consider: readability, maintainability, correctness, performance, security
- Focus on issues that impact code quality or functionality
