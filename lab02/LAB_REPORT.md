# SE3006 - Software Architecture

## Lab 02 – Modular Monolith vs Layered Architecture Comparison

---

# Overview

In **Lab 01**, a **Layered Architecture** was implemented using pure Java. Although layers were separated, the system still suffered from **tight coupling**, because the `OrderService` directly depended on `ProductRepository`.

In **Lab 02**, the system was refactored into a **Modular Monolith** architecture. The application was divided into independent business modules:

- `catalog` module (product & stock management)
    
- `orders` module (order management)
    

The main goal was to remove tight coupling and enforce **module boundaries** using **interfaces**, **factories**, and **information hiding**.

---

# Lab 01 – Tight Coupling (Layered Architecture)

### Structure

```
Controller → Service → Repository
```

### Dependency Flow

```
OrderController
      ↓
OrderService
      ↓
ProductRepository
```

### Problem

The **OrderService directly accessed ProductRepository**, which belongs to product domain logic.

This created:

- Tight coupling between order and product logic
    
- Shared internal data access
    
- Difficult refactoring
    
- Poor modularity
    

Example from Lab 01:

```java
Product product = productRepository.findById(productId);
```

Here, the Orders logic knows:

- Product structure
    
- Product storage
    
- Stock logic
    

This violates **Separation of Business Domains**.

---

# Lab 02 – Modular Monolith

In Lab 02, the system was divided into two independent modules:

```
catalog module
orders module
```

Modules communicate **only through interfaces**.

### New Dependency Flow

```
OrderController
      ↓
OrderService
      ↓
CatalogService (Interface)
      ↓
Catalog Module Internal Classes
```

The Orders module **no longer accesses ProductRepository**.

Instead, it calls:

```java
catalogService.checkAndReduceStock(productId, quantity);
```

This hides:

- Product entity
    
- ProductRepository
    
- Stock logic
    
- Data storage
    

---

# Key Architectural Differences

|Feature|Lab 01 (Layered)|Lab 02 (Modular Monolith)|
|---|---|---|
|Coupling|Tight|Loose|
|Business separation|No|Yes|
|Direct repository access|Yes|No|
|Module boundaries|No|Yes|
|Interface-based communication|No|Yes|
|Information hiding|No|Yes|
|Factory usage|No|Yes|
|Dependency direction|Layer-based|Module-based|
|Maintainability|Lower|Higher|
|Scalability|Limited|Better|

---

# Information Hiding (Lab 02)

Only public classes:

### Catalog Module

- `CatalogService`
    
- `CatalogFactory`
    

### Orders Module

- `OrderController`
    
- `OrdersFactory`
    

Hidden classes (package-private):

- `Product`
    
- `ProductRepository`
    
- `CatalogServiceImpl`
    
- `OrderRepository`
    
- `OrderService`
    

This prevents cross-module access.

---

# Dependency Comparison

### Lab 01 (Tight Coupling)

```
OrderService → ProductRepository
```

Orders module knows product details.

---

### Lab 02 (Loose Coupling)

```
OrderService → CatalogService (interface only)
```

Orders module knows **only behavior**, not implementation.

---

# Factory Pattern Usage (Lab 02)

Factories were introduced to wire dependencies internally.

### CatalogFactory

Creates:

```
ProductRepository
CatalogServiceImpl
```

Returns:

```
CatalogService
```

### OrdersFactory

Creates:

```
OrderRepository
OrderService
OrderController
```

Receives:

```
CatalogService (external dependency)
```

This keeps module internals hidden.

---

# Advantages of Modular Monolith

- Removes tight coupling
    
- Clear module ownership
    
- Better maintainability
    
- Easier testing
    
- Easier refactoring
    
- Prepares system for microservices migration
    
- Enforces domain separation
    

---

# Conclusion

Lab 01 demonstrated a basic layered architecture, but it resulted in **tight coupling** because the Orders logic directly accessed product data.

Lab 02 improved the design by introducing a **Modular Monolith**, where:

- Modules are independent
    
- Communication happens via interfaces
    
- Internal implementations are hidden
    
- Dependencies are wired using factories
    

This resulted in **loose coupling**, **better separation of concerns**, and a more maintainable architecture.

The comparison clearly shows that Modular Monolith architecture provides stronger boundaries and better scalability than the basic layered architecture used in Lab 01.