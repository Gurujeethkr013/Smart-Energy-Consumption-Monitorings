# Smart Energy Monitoring System — Java Edition

## Requirements
- JDK 17 or newer
- Maven 3.8+ (or use Maven support in VS Code)
- VS Code with Extension Pack for Java / Spring Boot support (recommended)
- Internet connection for Chart.js graph CDN

## Run
1. Extract this ZIP.
2. Open the extracted `Smart_Energy_Java_Project` folder in VS Code.
3. Ensure `java -version` reports Java 17 or newer and `mvn -version` works.
4. In the VS Code terminal run:
   ```bash
   mvn spring-boot:run
   ```
5. Open http://localhost:8080

If Maven is not installed, install Maven or open the folder in an IDE that supports Maven and run `SmartEnergyApplication.java`.

## Features
- Java Spring Boot REST backend
- Dashboard frontend (HTML/CSS/JavaScript)
- Simulated live power, energy, cost and carbon estimate
- Seven-day sample usage chart
- Device toggle demo
- Optimization recommendations

## Important
This version uses simulated values and does not connect to real meters or control actual appliances. Chart.js loads from a CDN.
