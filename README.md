# Lab: Round Robin Load Balancing using Spring Cloud API Gateway

**Aim:** Run 3 instances of `driver-service` on dynamic ports, register them in Eureka,
route through API Gateway at `/gateway/**`, and verify round-robin distribution.

| Module          | Port            | Role                                   |
|-----------------|-----------------|----------------------------------------|
| eureka-server   | 8761            | Service registry (discovery server)    |
| driver-service  | 0 (dynamic) x3  | Backend service, 3 instances           |
| api-gateway     | 8080            | Routes `/gateway/**` -> `lb://DRIVER-SERVICE` |

Stack: Java 21, Spring Boot 3.3.5, Spring Cloud 2023.0.3.

## Key configuration
- driver-service: `server.port: 0` (random port) + `instance-id: ${spring.application.name}:${random.value}` (unique id per instance).
- api-gateway route:
  ```yaml
  uri: lb://DRIVER-SERVICE      # Spring Cloud LoadBalancer, Round Robin by default
  predicates: [ Path=/gateway/** ]
  filters:   [ StripPrefix=1 ]  # /gateway/driver/info -> /driver/info
  ```

## Steps
1. Build: `mvn clean package -DskipTests` (in `lb-lab`)
2. Start Eureka (wait ~15 s): `java -jar eureka-server/target/eureka-server-1.0.jar`
3. Start 3 driver instances, each in its own terminal: `java -jar driver-service/target/driver-service-1.0.jar`
4. Start gateway: `java -jar api-gateway/target/api-gateway-1.0.jar`
5. Wait ~30 s for registration.

## Links
- Eureka dashboard: http://localhost:8761  -> DRIVER-SERVICE shows **UP (3)**
- Gateway endpoint: http://localhost:8080/gateway/driver/info
- Gateway routes:   http://localhost:8080/actuator/gateway/routes (needs actuator; optional)
- Direct instance:  http://localhost:<port>/driver/info (port from Eureka dashboard / console log "Tomcat started on port ...")

## Verify
Refresh http://localhost:8080/gateway/driver/info several times, or run (PowerShell):
```powershell
1..6 | % { Invoke-RestMethod http://localhost:8080/gateway/driver/info | ConvertTo-Json -Compress }
```
**Expected:** `port` cycles A -> B -> C -> A -> B -> C. Sample result:
```
port 54838 -> 54837 -> 54836 -> 54838 -> 54837 -> 54836
```

## Result
Requests sent to the gateway were distributed equally across the 3 dynamically registered
driver-service instances in round-robin order.
