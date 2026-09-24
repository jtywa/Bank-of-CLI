#### Bank of CLI

Bank of CLI is a layered REPL application representing a simple banking system.
Users can sign up, login and logout, check their balance and perform transactions.

#### Usage
---
To interact with the application, users can enter one of the keywords listed below:
- `signup`
- `login`
- `logout`
- `balance`
- `deposit`
- `withdrawal`
- `transfer`
- `history`

The user may also be prompted to provide inputs such as username, password, transaction amounts.

#### Stack
---
- Java
- Maven
- PostgreSQL
- JDBC
- JUnit5

#### Testing
---
- JUnit/Mockito
- Positive/Negative unit tests in each layer

#### Logging
---
- SLF4J
- Service-layer logging for all events (user actions, errors, warnings)