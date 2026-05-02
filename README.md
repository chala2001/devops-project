# DevOps Project - CI/CD Pipeline with Jenkins & AWS

A simple Java application demonstrating a complete DevOps workflow:
- **Version Control:** Git & GitHub
- **Build Tool:** Maven
- **CI/CD:** Jenkins Pipeline
- **Cloud:** AWS EC2 (Free Tier)

## Quick Start
```bash
mvn clean package
java -jar target/hello-devops-1.0-SNAPSHOT.jar
```

## Pipeline Stages
1. **Checkout** - Clone from GitHub
2. **Build** - Compile with Maven
3. **Test** - Run JUnit tests
4. **Package** - Create JAR artifact
5. **Deploy** - Deploy to AWS EC2
