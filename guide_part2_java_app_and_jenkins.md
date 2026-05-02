# DevOps Complete Guide - Part 2: Java App & Jenkins CI/CD
## Build, Automate, Deploy

---

## Table of Contents
1. [Install Java (JDK)](#1-install-java-jdk)
2. [Install Maven](#2-install-maven)
3. [Create the Java Application](#3-create-the-java-application)
4. [Build & Run Locally](#4-build--run-locally)
5. [Push to GitHub](#5-push-to-github)
6. [What is CI/CD?](#6-what-is-cicd)
7. [Install Jenkins on Windows](#7-install-jenkins-on-windows)
8. [Configure Jenkins](#8-configure-jenkins)
9. [Create Jenkinsfile](#9-create-jenkinsfile)
10. [Create Jenkins Pipeline](#10-create-jenkins-pipeline)
11. [Trigger Builds Automatically](#11-trigger-builds-automatically)

---

## 1. Install Java (JDK)

Jenkins and our app both need Java.

### Step 1: Download JDK
1. Go to: `https://adoptium.net/`
2. Click **"Latest LTS Release"** (JDK 21)
3. Select: **Windows x64** → **.msi** installer
4. Click **Download**

### Step 2: Run Installer
1. Double-click the downloaded `.msi` file
2. Click **Next**
3. On **Custom Setup** screen:
   - Click the feature **"Set JAVA_HOME variable"**
   - Change it to **"Will be installed on local hard drive"**
4. Click **Next** → **Install** → **Finish**

### Step 3: Verify
Open a **NEW** PowerShell window:
```powershell
java -version
```
Expected: `openjdk version "21.x.x"`

```powershell
echo $env:JAVA_HOME
```
Expected: Something like `C:\Program Files\Eclipse Adoptium\jdk-21...`

If JAVA_HOME is empty, set it manually:
```powershell
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Eclipse Adoptium\jdk-21.0.4.7-hotspot", "Machine")
```
Then restart PowerShell.

---

## 2. Install Maven

Maven is a build tool for Java — it compiles, tests, and packages your code.

### Step 1: Download Maven
1. Go to: `https://maven.apache.org/download.cgi`
2. Under **"Files"**, download the **Binary zip archive** (`apache-maven-3.x.x-bin.zip`)
3. Extract the zip to `C:\Program Files\Maven`
   - Right-click the zip → **Extract All** → Choose `C:\Program Files\Maven`

### Step 2: Set Environment Variables
1. Press `Win + S`, type **"Environment Variables"**
2. Click **"Edit the system environment variables"**
3. Click **"Environment Variables"** button
4. Under **System variables**, click **"New"**:
   - **Variable name:** `MAVEN_HOME`
   - **Variable value:** `C:\Program Files\Maven\apache-maven-3.x.x` (replace with actual folder name)
5. Find **"Path"** in System variables → Click **"Edit"**
6. Click **"New"** → Add: `%MAVEN_HOME%\bin`
7. Click **OK** on all windows

### Step 3: Verify
Open a **NEW** PowerShell:
```powershell
mvn -version
```
Expected output shows Maven version, Java version, and OS info.

---

## 3. Create the Java Application

We'll create a simple Maven project with a Hello World app and a unit test.

### Step 1: Create Project Structure

Run in PowerShell:
```powershell
cd C:\Users\ASUS\Desktop\Devops_Project

# Create directories
New-Item -ItemType Directory -Path "src\main\java\com\devops\app" -Force
New-Item -ItemType Directory -Path "src\test\java\com\devops\app" -Force
```

### Step 2: Create pom.xml (Maven config)
Create `pom.xml` in the project root (`C:\Users\ASUS\Desktop\Devops_Project\pom.xml`):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
    
    <!-- Project identity -->
    <groupId>com.devops.app</groupId>
    <artifactId>hello-devops</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    
    <name>Hello DevOps</name>
    <description>A simple Java app for learning DevOps</description>
    
    <!-- Java version -->
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <!-- Dependencies (libraries we use) -->
    <dependencies>
        <!-- JUnit 5 for testing -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <!-- Make the JAR executable -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.3.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>com.devops.app.HelloDevOps</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

**What each part means:**
- `groupId`: Your organization (like a package name)
- `artifactId`: Your project name
- `version`: Current version (`SNAPSHOT` = in development)
- `dependencies`: Libraries your project uses (JUnit for testing)
- `plugins`: Tools Maven uses during build

### Step 3: Create Main Java File
Create `src/main/java/com/devops/app/HelloDevOps.java`:

```java
package com.devops.app;

/**
 * Main application class for our DevOps demo project.
 * This simple app demonstrates a buildable, testable Java application
 * that we'll deploy through a CI/CD pipeline.
 */
public class HelloDevOps {

    /**
     * Returns a greeting message.
     * This method is separated from main() so we can unit test it.
     */
    public String getGreeting() {
        return "Hello from DevOps Pipeline!";
    }

    /**
     * Returns application version info.
     */
    public String getVersion() {
        return "1.0-SNAPSHOT";
    }

    /**
     * Performs a simple calculation (to show testing).
     */
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * Entry point of the application.
     */
    public static void main(String[] args) {
        HelloDevOps app = new HelloDevOps();
        
        System.out.println("================================");
        System.out.println("  " + app.getGreeting());
        System.out.println("  Version: " + app.getVersion());
        System.out.println("  2 + 3 = " + app.add(2, 3));
        System.out.println("================================");
        System.out.println("  Build successful!");
        System.out.println("  Deployed via Jenkins CI/CD");
        System.out.println("================================");
    }
}
```

### Step 4: Create Unit Test
Create `src/test/java/com/devops/app/HelloDevOpsTest.java`:

```java
package com.devops.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HelloDevOps.
 * Jenkins will run these tests automatically in the CI/CD pipeline.
 * If ANY test fails, the pipeline stops and notifies you.
 */
public class HelloDevOpsTest {

    @Test
    void testGreeting() {
        HelloDevOps app = new HelloDevOps();
        assertEquals("Hello from DevOps Pipeline!", app.getGreeting());
    }

    @Test
    void testVersion() {
        HelloDevOps app = new HelloDevOps();
        assertNotNull(app.getVersion());
        assertTrue(app.getVersion().contains("SNAPSHOT"));
    }

    @Test
    void testAdd() {
        HelloDevOps app = new HelloDevOps();
        assertEquals(5, app.add(2, 3));
        assertEquals(0, app.add(0, 0));
        assertEquals(-1, app.add(2, -3));
    }
}
```

---

## 4. Build & Run Locally

### Compile the project:
```powershell
cd C:\Users\ASUS\Desktop\Devops_Project
mvn compile
```
**What happens:** Maven downloads dependencies and compiles `.java` → `.class` files.

### Run tests:
```powershell
mvn test
```
**What happens:** Runs all files ending in `Test.java`. You should see `Tests run: 3, Failures: 0`.

### Package into JAR:
```powershell
mvn package
```
**What happens:** Compiles, tests, and creates a `.jar` file in the `target/` folder.

### Run the JAR:
```powershell
java -jar target\hello-devops-1.0-SNAPSHOT.jar
```
You should see the greeting message printed!

### Clean build files:
```powershell
mvn clean
```
Deletes the `target/` folder.

### Do everything at once:
```powershell
mvn clean package
```

---

## 5. Push to GitHub

### Step 1: Create GitHub Repository
1. Go to `https://github.com`
2. Click the **"+"** icon (top right) → **"New repository"**
3. Fill in:
   - **Repository name:** `devops-project`
   - **Description:** `Learning DevOps - CI/CD with Jenkins and AWS`
   - **Public** (select this)
   - Do NOT check "Add a README" (we already have one)
4. Click **"Create repository"**

### Step 2: Create .gitignore
Create `.gitignore` in project root:
```
# Maven build output
target/

# IDE files
.idea/
.vscode/
*.iml
.classpath
.project
.settings/

# OS files
Thumbs.db
.DS_Store

# Jenkins
.jenkins/
```

### Step 3: Push Code
```powershell
cd C:\Users\ASUS\Desktop\Devops_Project
git init
git add .
git status
git commit -m "Initial commit - Java app with Maven and tests"
git branch -M main
git remote add origin git@github.com:YOUR_USERNAME/devops-project.git
git push -u origin main
```

Replace `YOUR_USERNAME` with your actual GitHub username!

Refresh your GitHub page — you should see all your files!

---

## 6. What is CI/CD?

### CI = Continuous Integration
**Every time you push code to GitHub**, an automated system:
1. Downloads your code
2. Compiles it
3. Runs all tests
4. Reports if anything is broken

**Why?** Catches bugs immediately. No more "it works on my machine" problems.

### CD = Continuous Delivery / Deployment
After CI passes:
- **Continuous Delivery** = Automatically prepares the code for deployment (you click a button to deploy)
- **Continuous Deployment** = Automatically deploys to production (no human needed)

### Our Pipeline:
```
Push Code → GitHub → Jenkins detects change → Build → Test → Package → Deploy to AWS EC2
```

### What is Jenkins?
- Free, open-source automation server
- Written in Java
- Runs your CI/CD pipeline
- Has 1800+ plugins for almost anything
- Industry standard (used by Netflix, Facebook, etc.)

---

## 7. Install Jenkins on Windows

### Step 1: Download Jenkins
1. Go to: `https://www.jenkins.io/download/`
2. Under **LTS (Long-Term Support)**, click **"Windows"**
3. Download the `.msi` installer

### Step 2: Run Installer
1. Double-click the `.msi` file
2. Click **Next**
3. **Logon Type:** Select **"Run service as LocalSystem"** → Click **Next**
4. **Port:** Keep default **8080** → Click **Test Port** → Should say "Port is available" → Click **Next**
5. **Java Home:** Should auto-detect your JDK. If not, browse to your JDK path → Click **Next**
6. Click **Install** → **Finish**

Jenkins will now start as a Windows service automatically.

### Step 3: Unlock Jenkins
1. Open browser: `http://localhost:8080`
2. You'll see "Unlock Jenkins" page
3. It asks for the **Administrator password** found at a file path shown on screen
4. Open PowerShell and run:
```powershell
Get-Content "C:\ProgramData\Jenkins\.jenkins\secrets\initialAdminPassword"
```
5. Copy the password → Paste it in the browser → Click **Continue**

### Step 4: Install Plugins
1. Click **"Install suggested plugins"**
2. Wait for all plugins to install (takes 2-5 minutes)
3. You'll see green checkmarks for each plugin

### Step 5: Create Admin User
1. Fill in:
   - **Username:** `admin`
   - **Password:** `admin123` (or your choice — remember this!)
   - **Full name:** `Your Name`
   - **Email:** `your.email@example.com`
2. Click **"Save and Continue"**

### Step 6: Instance Configuration
1. **Jenkins URL:** Keep `http://localhost:8080/` → Click **"Save and Finish"**
2. Click **"Start using Jenkins"**

You should see the Jenkins Dashboard!

---

## 8. Configure Jenkins

### Install Additional Plugins
1. Go to: **Manage Jenkins** → **Plugins** → **Available plugins**
2. Search for and install:
   - **Pipeline** (should be pre-installed)
   - **Git** (should be pre-installed)
   - **GitHub Integration**
   - **Maven Integration**
3. Click **"Install"** → Check **"Restart Jenkins when installation is complete"**

### Configure JDK in Jenkins
1. Go to: **Manage Jenkins** → **Tools**
2. Scroll to **JDK installations**
3. Click **"Add JDK"**
   - **Name:** `JDK21`
   - Uncheck "Install automatically"
   - **JAVA_HOME:** `C:\Program Files\Eclipse Adoptium\jdk-21.0.4.7-hotspot` (your actual path)
4. Click **Save**

### Configure Maven in Jenkins
1. Still in **Tools** page
2. Scroll to **Maven installations**
3. Click **"Add Maven"**
   - **Name:** `Maven3`
   - Check **"Install automatically"** → Select latest version
4. Click **Save**

### Add GitHub Credentials
1. Go to: **Manage Jenkins** → **Credentials**
2. Click **(global)** → **"Add Credentials"**
3. **Kind:** SSH Username with private key
4. **ID:** `github-ssh`
5. **Username:** `git`
6. **Private Key:** Enter directly → paste your SSH private key:
```powershell
Get-Content $env:USERPROFILE\.ssh\id_ed25519
```
7. Click **Create**

---

## 9. Create Jenkinsfile

The `Jenkinsfile` defines your entire pipeline as code. Create this file in your project root.

Create `Jenkinsfile` in `C:\Users\ASUS\Desktop\Devops_Project\Jenkinsfile`:

```groovy
pipeline {
    // Run on any available agent (machine)
    agent any
    
    // Define tools to use
    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }
    
    // Environment variables
    environment {
        APP_NAME = 'hello-devops'
        APP_VERSION = '1.0-SNAPSHOT'
    }
    
    // The actual pipeline stages
    stages {
        
        // Stage 1: Checkout code from GitHub
        stage('Checkout') {
            steps {
                echo '=== Stage 1: Checking out code from GitHub ==='
                // Jenkins does this automatically for Pipeline jobs
                // but we make it explicit for clarity
                checkout scm
            }
        }
        
        // Stage 2: Compile the code
        stage('Build') {
            steps {
                echo '=== Stage 2: Compiling the application ==='
                bat 'mvn clean compile'
                // 'bat' runs Windows batch commands
                // On Linux, use 'sh' instead
            }
        }
        
        // Stage 3: Run unit tests
        stage('Test') {
            steps {
                echo '=== Stage 3: Running unit tests ==='
                bat 'mvn test'
            }
            // After tests, publish results
            post {
                always {
                    // Publish test results so Jenkins shows them nicely
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        // Stage 4: Package into JAR
        stage('Package') {
            steps {
                echo '=== Stage 4: Packaging application into JAR ==='
                bat 'mvn package -DskipTests'
                // -DskipTests because we already tested
                
                // Archive the JAR so we can download it from Jenkins
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
        
        // Stage 5: Deploy (we'll add AWS deployment later)
        stage('Deploy') {
            steps {
                echo '=== Stage 5: Deploying application ==='
                echo "Deploying ${APP_NAME} version ${APP_VERSION}"
                bat "java -jar target\\${APP_NAME}-${APP_VERSION}.jar"
            }
        }
    }
    
    // Actions after pipeline completes
    post {
        success {
            echo 'Pipeline completed SUCCESSFULLY!'
        }
        failure {
            echo 'Pipeline FAILED! Check the logs above.'
        }
        always {
            echo 'Pipeline finished. Cleaning workspace...'
            cleanWs()
        }
    }
}
```

**Key concepts explained:**
- `pipeline {}` — The entire pipeline definition
- `agent any` — Run on any available machine
- `tools {}` — Which tools Jenkins should set up
- `stages {}` — Contains all the stages (steps) of your pipeline
- `stage('Name') {}` — One step in the pipeline
- `steps {}` — The actual commands to run
- `bat` — Run a Windows command (use `sh` on Linux)
- `post {}` — Actions after pipeline finishes (success/failure)
- `checkout scm` — Download code from the configured Git repo
- `archiveArtifacts` — Save build output in Jenkins
- `junit` — Parse and display test results
- `cleanWs()` — Clean the workspace after build

### Push the Jenkinsfile to GitHub:
```powershell
cd C:\Users\ASUS\Desktop\Devops_Project
git add Jenkinsfile
git commit -m "Add Jenkinsfile for CI/CD pipeline"
git push
```

---

## 10. Create Jenkins Pipeline

### Step 1: Create New Pipeline Job
1. Open Jenkins: `http://localhost:8080`
2. Click **"New Item"** (left sidebar)
3. Enter name: `devops-pipeline`
4. Select **"Pipeline"**
5. Click **OK**

### Step 2: Configure the Pipeline
On the configuration page:

**General Section:**
- Check **"GitHub project"**
- **Project url:** `https://github.com/YOUR_USERNAME/devops-project`

**Build Triggers Section:**
- Check **"Poll SCM"**
- **Schedule:** `H/5 * * * *`
  > This means: Check GitHub every 5 minutes for changes

**Pipeline Section:**
- **Definition:** Select **"Pipeline script from SCM"**
- **SCM:** Select **"Git"**
- **Repository URL:** `git@github.com:YOUR_USERNAME/devops-project.git`
- **Credentials:** Select the `github-ssh` credential you created
- **Branch Specifier:** `*/main`
- **Script Path:** `Jenkinsfile`

Click **"Save"**

### Step 3: Run the Pipeline
1. Click **"Build Now"** (left sidebar)
2. You'll see a new build appear under **"Build History"**
3. Click the build number (e.g., `#1`)
4. Click **"Console Output"** to see live logs
5. Watch each stage execute: Checkout → Build → Test → Package → Deploy

If all stages are green, your CI/CD pipeline works!

### Step 4: View Pipeline Visualization
1. Go back to the pipeline job page
2. You'll see a **stage view** showing each stage with execution time
3. Green = passed, Red = failed

---

## 11. Trigger Builds Automatically

### Option A: Poll SCM (Already configured)
Jenkins checks GitHub every 5 minutes. If there are new commits, it builds.

### Option B: GitHub Webhook (Instant)
> Note: This requires Jenkins to be accessible from the internet. For local Jenkins, use ngrok or skip this.

1. Go to your GitHub repo → **Settings** → **Webhooks**
2. Click **"Add webhook"**
3. **Payload URL:** `http://YOUR_JENKINS_URL/github-webhook/`
4. **Content type:** `application/json`
5. **Events:** Select **"Just the push event"**
6. Click **"Add webhook"**

### Test Automatic Builds
Make a change and push:
```powershell
cd C:\Users\ASUS\Desktop\Devops_Project
# Edit HelloDevOps.java or any file
git add .
git commit -m "Trigger automatic build - updated greeting"
git push
```

Within 5 minutes (Poll SCM) or instantly (Webhook), Jenkins will detect the change and start a new build!

---

## Pipeline Stages Summary

```
┌──────────┐   ┌──────────┐   ┌──────────┐   ┌──────────┐   ┌──────────┐
│ CHECKOUT │ → │  BUILD   │ → │   TEST   │ → │ PACKAGE  │ → │  DEPLOY  │
│          │   │          │   │          │   │          │   │          │
│ Get code │   │ Compile  │   │ Run unit │   │ Create   │   │ Run/Ship │
│ from Git │   │ Java     │   │ tests    │   │ JAR file │   │ the app  │
└──────────┘   └──────────┘   └──────────┘   └──────────┘   └──────────┘
```

---

> **Next: See `guide_part3_aws_deployment.md` for deploying to AWS EC2 with the complete pipeline.**
