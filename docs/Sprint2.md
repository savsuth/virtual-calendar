# v0.5 Design Notes

## Design Changes and Justifications From Assignment 4

### 1. Introduction of ICalendarService as an Adapter
- **Change:**  
  A new interface `ICalendarService` and its implementation `CalendarService` were introduced between the controller and the model.
- **Justification:**  
  - **Separation of Concerns:** The CalendarService acts as an adapter that abstracts the underlying model operations.  
  - **Encapsulation:** Controllers and command classes interact only with the service layer rather than directly with the model.  
  - **Flexibility:** This allows changes to the model without affecting higher layers and supports future extensions.

### 2. Centralized Model Operations via ICalendarService
- **Change:**  
  All model operations (e.g., adding events, editing events, exporting) are now routed through the CalendarService rather than directly manipulating the CalendarModel.
- **Justification:**  
  This centralization ensures consistent business logic application and simplifies testing.

### 3. Refactoring of Edit Operations with EditEventOperations
- **Change:**  
  Event-editing logic has been refactored into the `EditEventOperations` class, called by `CalendarService`.
- **Justification:**  
  - **Single Responsibility:** Separates command parsing from core editing logic.  
  - **Maintainability:** Future changes to editing rules are isolated.  
  - **Reusability:** Edit logic can be reused across different contexts.

### 4. Abstract Event Model
- **Change:**  
  `AbstractEvent` now shares common code across `SingleEvent` and `RecurringEvent`, including auto-decline features and effective date calculations.
- **Justification:**  
  Reduces duplication and improves consistency between event types.

### 5. Concrete Classes to Their Interfaces
- **Change:**  
  Controllers and services now depend on interfaces (e.g., `ICalendarService`, `IEditEventOperations`) rather than concrete implementations.
- **Justification:**  
  Prevents leakage of implementation details and supports dependency inversion for greater modularity. 