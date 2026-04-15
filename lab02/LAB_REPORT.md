# SE 3006 — Software Architecture

## Lab 02: Modular Monolith Built with Pure Java

---

# Objective

The objective of this lab is to refactor a tightly coupled layered architecture into a **Modular Monolith** structure using pure Java.  
The system is divided into vertical business modules:

- **Catalog Module** — manages products and stock
    
- **Orders Module** — manages orders
    

The main goals were:

- Apply **Information Hiding** using Java package-private access
    
- Enforce **module boundaries**
    
- Allow communication only via **public interfaces**
    
- Use **Factories** to wire dependencies
    
- Prevent direct access to internal module data
    

---

# Architecture Overview

The system follows a **Modular Monolith** architecture. Each module encapsulates its own logic and internal classes.

```
Main
 ├── Catalog Module
 │     ├── CatalogService (public)
 │     ├── CatalogFactory (public)
 │     ├── Product (package-private)
 │     ├── ProductRepository (package-private)
 │     └── CatalogServiceImpl (package-private)
 │
 └── Orders Module
       ├── OrderController (public)
       ├── OrdersFactory (public)
       ├── OrderService (package-private)
       └── OrderRepository (package-private)
```

Each module hides its implementation details and only exposes what is necessary.

---

# Information Hiding

Information hiding was achieved using Java’s **default (package-private)** access modifier.

Only the following classes are public:

### Catalog Module

- `CatalogService`
    
- `CatalogFactory`
    

### Orders Module

- `OrderController`
    
- `OrdersFactory`
    

All repositories, entities, and implementations remain hidden inside their packages.

This ensures:

- Loose coupling
    
- Encapsulation
    
- Clear module boundaries
    

---

# Module Communication

Modules communicate only through **interfaces**.

The **Orders Module** does NOT know:

- What a Product is
    
- How stock is stored
    
- How repository works
    

It only calls:

```
catalogService.checkAndReduceStock(productId, quantity);
```

This enforces dependency inversion and loose coupling.

---

# Implementation Details

## TASK 1 — Catalog Module Logic

- Implemented `ProductRepository`
    
- Added `findById()` method
    
- Added `save()` method
    
- Injected repository into `CatalogServiceImpl`
    
- Implemented `checkAndReduceStock()`
    

Logic:

- Find product
    
- Check stock availability
    
- Reduce stock
    
- Save updated product
    
- Throw exception if insufficient stock
    

---

## TASK 2 — Catalog Factory

`CatalogFactory` wires internal dependencies.

Steps:

1. Create `ProductRepository`
    
2. Create `CatalogServiceImpl`
    
3. Return `CatalogService`
    

This hides internal wiring from external modules.

---

## TASK 3 — Orders Module

`OrderService` receives `CatalogService` via constructor injection.

`placeOrder()` logic:

- Call `catalogService.checkAndReduceStock()`
    
- Create order
    
- Save order
    

The service never accesses product data directly.

`OrderController`:

- Calls service
    
- Wraps logic inside `try-catch`
    
- Handles success and error messages
    

---

## TASK 4 — Orders Factory

`OrdersFactory` wires:

- `OrderRepository`
    
- `OrderService`
    
- `OrderController`
    

It receives `CatalogService` as external dependency.

This allows cross-module communication while maintaining boundaries.

---

## TASK 5 — Main Bootstrapping

Main class initializes modules:

1. Create Catalog module using `CatalogFactory`
    
2. Pass CatalogService to OrdersFactory
    
3. Create Orders module
    
4. Call controller to test order placement
    

Example flow:

```
CatalogService catalog = CatalogFactory.create();
OrderController controller = OrdersFactory.create(catalog);
controller.placeOrder(1L, 2);
```

---

# Achievements

-  Converted layered architecture to modular monolith  
-  Applied information hiding  
-  Removed tight coupling  
-  Used constructor injection  
-  Enforced interface-based communication  
-  Implemented factory pattern for dependency wiring

---

# Advantages of Modular Monolith

- Easier to maintain
    
- Clear module boundaries
    
- No shared database access
    
- Loose coupling
    
- Easier transition to microservices later
    
- Better testability
    

---

# Conclusion

This lab demonstrated how a **Modular Monolith** improves architecture quality by enforcing boundaries and reducing coupling. Using interfaces, factories, and information hiding, each module became independent while still working inside a single application.

This architecture keeps the simplicity of a monolith while providing the modularity benefits typically seen in microservices.
