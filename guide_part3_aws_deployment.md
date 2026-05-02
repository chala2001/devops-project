# DevOps Complete Guide - Part 3: AWS Deployment
## Deploy to AWS EC2 (Free Tier)

---

## Table of Contents
1. [What is AWS?](#1-what-is-aws)
2. [Create AWS Free Tier Account](#2-create-aws-free-tier-account)
3. [Launch EC2 Instance](#3-launch-ec2-instance)
4. [Connect to EC2 from Windows](#4-connect-to-ec2-from-windows)
5. [Set Up EC2 for Java](#5-set-up-ec2-for-java)
6. [Manual Deployment to EC2](#6-manual-deployment-to-ec2)
7. [Automated Deployment via Jenkins](#7-automated-deployment-via-jenkins)
8. [Updated Jenkinsfile with AWS Deploy](#8-updated-jenkinsfile-with-aws-deploy)
9. [Security Groups and Networking](#9-security-groups-and-networking)
10. [AWS Free Tier Limits](#10-aws-free-tier-limits)
11. [Complete Project Walkthrough](#11-complete-project-walkthrough)

---

## 1. What is AWS?

**Amazon Web Services (AWS)** is a cloud computing platform. Instead of buying physical servers, you rent virtual servers from Amazon.

**Key Services We'll Use:**

| Service | What It Is | Analogy |
|---------|-----------|---------|
| **EC2** | Virtual server (computer in the cloud) | Renting a computer |
| **Security Groups** | Firewall rules | Lock on the door |
| **Key Pairs** | SSH authentication | Keys to get in |
| **S3** (optional) | File storage | Google Drive in the cloud |

**Free Tier** = AWS gives you 12 months of limited free usage:
- 750 hours/month of t2.micro EC2 (enough for 1 instance running 24/7)
- 30 GB storage
- 15 GB data transfer

---

## 2. Create AWS Free Tier Account

### Step 1: Sign Up
1. Go to: `https://aws.amazon.com/free/`
2. Click **"Create a Free Account"**
3. Enter email and choose an AWS account name → Click **Verify email**
4. Enter the verification code from your email
5. Create a **Root user password** → Click **Continue**

### Step 2: Contact Information
1. Select **"Personal"** account type
2. Fill in your name, phone number, address
3. Click **Continue**

### Step 3: Payment Information
1. Enter a credit/debit card (required but WON'T be charged if you stay in Free Tier)
2. Click **Verify and Continue**

### Step 4: Identity Verification
1. Choose **Text message (SMS)**
2. Enter your phone number
3. Enter the verification code
4. Click **Continue**

### Step 5: Select Support Plan
1. Select **"Basic support - Free"**
2. Click **"Complete sign up"**

### Step 6: Sign In
1. Go to: `https://console.aws.amazon.com`
2. Select **"Root user"**
3. Enter your email and password
4. You're now in the **AWS Management Console**!

---

## 3. Launch EC2 Instance

### Step 1: Navigate to EC2
1. In the AWS Console, type **"EC2"** in the search bar at the top
2. Click **"EC2"** from the results
3. Click **"Launch Instance"** (orange button)

### Step 2: Configure the Instance

**Name and tags:**
- **Name:** `devops-server`

**Application and OS Images (AMI):**
- Select **"Amazon Linux 2023"** (Free tier eligible)
- It should show "Free tier eligible" label

**Instance type:**
- Select **"t2.micro"** (Free tier eligible)
- This gives you: 1 CPU, 1 GB RAM — enough for our app

**Key pair (login):**
- Click **"Create new key pair"**
- **Key pair name:** `devops-key`
- **Key pair type:** RSA
- **Private key file format:** `.pem`
- Click **"Create key pair"**
- The `.pem` file will download automatically
- **SAVE THIS FILE!** Move it to: `C:\Users\ASUS\.ssh\devops-key.pem`

**Network settings:**
- Click **"Edit"**
- **VPC:** Keep default
- **Subnet:** Keep default
- **Auto-assign public IP:** **Enable**
- **Security group:** Click **"Create security group"**
  - **Security group name:** `devops-sg`
  - **Description:** `Security group for DevOps project`
  
  **Inbound rules** (add these):
  | Type | Port Range | Source | Description |
  |------|-----------|--------|-------------|
  | SSH | 22 | My IP | SSH access |
  | Custom TCP | 8080 | 0.0.0.0/0 | Jenkins (if running on EC2) |
  | Custom TCP | 8081 | 0.0.0.0/0 | Our Java app |

  To add a rule: Click **"Add security group rule"** for each row above.

**Configure storage:**
- **Size:** `20` GiB (within Free Tier 30 GiB limit)
- **Type:** gp3

### Step 3: Launch
1. Review your settings on the right sidebar
2. Click **"Launch Instance"**
3. You'll see "Successfully initiated launch"
4. Click **"View all instances"**

### Step 4: Wait for Instance
1. Wait for **Instance state** to show **"Running"** (green)
2. Wait for **Status checks** to show **"2/2 checks passed"**
3. Note your **Public IPv4 address** (e.g., `3.15.xxx.xxx`) — you'll need this!

---

## 4. Connect to EC2 from Windows

### Step 1: Set Key Permissions
Open PowerShell as Administrator:
```powershell
# Remove inheritance
icacls "C:\Users\ASUS\.ssh\devops-key.pem" /inheritance:r

# Grant read to current user only
icacls "C:\Users\ASUS\.ssh\devops-key.pem" /grant:r "$($env:USERNAME):(R)"
```
> SSH requires the key file to have restricted permissions. Without this, SSH will refuse to connect.

### Step 2: Connect via SSH
```powershell
ssh -i C:\Users\ASUS\.ssh\devops-key.pem ec2-user@YOUR_EC2_PUBLIC_IP
```
Replace `YOUR_EC2_PUBLIC_IP` with the IP from Step 4 above.

**When it asks:** `Are you sure you want to continue connecting?` → Type **yes**

You should see:
```
   ,     #_
   ~\_  ####_        Amazon Linux 2023
  ~~  \_#####\
  ~~     \###|
  ~~       \#/ ___
   ~~       V~' '->
    ~~~         /
      ~~._.   _/
         _/ _/
       _/m/'
[ec2-user@ip-xxx-xxx-xxx-xxx ~]$
```

You're now inside your cloud server!

---

## 5. Set Up EC2 for Java

Run these commands INSIDE your EC2 instance (while SSH'd in):

### Install Java
```bash
sudo yum install java-21-amazon-corretto-devel -y
```
> `sudo` = run as administrator. `yum` = package manager (like App Store for Linux). `-y` = answer yes automatically.

Verify:
```bash
java -version
```

### Install Maven
```bash
sudo yum install maven -y
```

If maven is not available via yum:
```bash
cd /opt
sudo wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
sudo tar -xzf apache-maven-3.9.6-bin.tar.gz
sudo ln -s /opt/apache-maven-3.9.6/bin/mvn /usr/local/bin/mvn
```

Verify:
```bash
mvn -version
```

### Install Git
```bash
sudo yum install git -y
git --version
```

---

## 6. Manual Deployment to EC2

Before automating with Jenkins, let's deploy manually to understand each step.

### Step 1: Clone Your Repo on EC2
```bash
cd /home/ec2-user
git clone https://github.com/YOUR_USERNAME/devops-project.git
cd devops-project
```

### Step 2: Build on EC2
```bash
mvn clean package
```

### Step 3: Run the App
```bash
java -jar target/hello-devops-1.0-SNAPSHOT.jar
```

You should see the greeting! Press `Ctrl+C` to stop.

### Step 4: Run in Background (so it keeps running after you disconnect)
```bash
nohup java -jar target/hello-devops-1.0-SNAPSHOT.jar > app.log 2>&1 &
```

**What this means:**
- `nohup` = Don't stop when I disconnect
- `> app.log` = Save output to app.log
- `2>&1` = Also save errors to app.log
- `&` = Run in background

Check if it's running:
```bash
ps aux | grep java
```

Check the log:
```bash
cat app.log
```

Stop the app:
```bash
pkill -f hello-devops
```

---

## 7. Automated Deployment via Jenkins

### Method: SCP + SSH from Jenkins

The idea: Jenkins builds the JAR locally, then copies it to EC2 and runs it.

### Step 1: Install SSH Plugins in Jenkins
1. Go to: **Manage Jenkins** → **Plugins** → **Available plugins**
2. Search and install: **"Publish Over SSH"**
3. Restart Jenkins

### Step 2: Configure SSH Server in Jenkins
1. Go to: **Manage Jenkins** → **System**
2. Scroll down to **"Publish over SSH"**
3. **SSH Servers** → Click **"Add"**:
   - **Name:** `ec2-devops`
   - **Hostname:** `YOUR_EC2_PUBLIC_IP`
   - **Username:** `ec2-user`
   - **Remote Directory:** `/home/ec2-user`
4. Click **"Advanced"**
   - Check **"Use password authentication, or use a different key"**
   - **Key:** Paste the contents of your `devops-key.pem` file:
     ```powershell
     Get-Content C:\Users\ASUS\.ssh\devops-key.pem
     ```
5. Click **"Test Configuration"** → Should show **"Success"**
6. Click **"Save"**

### Alternative Method: Using a Deploy Script

If SSH plugin setup is complex, create a deploy script.

Create `deploy.sh` in your project root:
```bash
#!/bin/bash
# deploy.sh - Deployment script for EC2

EC2_HOST="YOUR_EC2_PUBLIC_IP"
EC2_USER="ec2-user"
KEY_PATH="C:/Users/ASUS/.ssh/devops-key.pem"
APP_NAME="hello-devops-1.0-SNAPSHOT.jar"
REMOTE_DIR="/home/ec2-user/app"

echo "=== Deploying to EC2 ==="

# Step 1: Create remote directory
ssh -i $KEY_PATH -o StrictHostKeyChecking=no $EC2_USER@$EC2_HOST "mkdir -p $REMOTE_DIR"

# Step 2: Stop existing application
ssh -i $KEY_PATH $EC2_USER@$EC2_HOST "pkill -f $APP_NAME || true"

# Step 3: Copy the JAR file to EC2
scp -i $KEY_PATH target/$APP_NAME $EC2_USER@$EC2_HOST:$REMOTE_DIR/

# Step 4: Start the application
ssh -i $KEY_PATH $EC2_USER@$EC2_HOST "cd $REMOTE_DIR && nohup java -jar $APP_NAME > app.log 2>&1 &"

echo "=== Deployment complete ==="
echo "App is running on $EC2_HOST"
```

---

## 8. Updated Jenkinsfile with AWS Deploy

Update your `Jenkinsfile` with AWS deployment:

```groovy
pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
        APP_NAME = 'hello-devops'
        APP_VERSION = '1.0-SNAPSHOT'
        JAR_FILE = "${APP_NAME}-${APP_VERSION}.jar"
        EC2_HOST = 'YOUR_EC2_PUBLIC_IP'    // Replace with your EC2 IP
        EC2_USER = 'ec2-user'
        REMOTE_DIR = '/home/ec2-user/app'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '=== Checking out code from GitHub ==='
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '=== Compiling the application ==='
                bat 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo '=== Running unit tests ==='
                bat 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo '=== Packaging into JAR ==='
                bat 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Deploy to EC2') {
            steps {
                echo '=== Deploying to AWS EC2 ==='
                // Using Publish Over SSH plugin
                sshPublisher(
                    publishers: [
                        sshPublisherDesc(
                            configName: 'ec2-devops',
                            transfers: [
                                sshTransfer(
                                    sourceFiles: "target/${JAR_FILE}",
                                    removePrefix: 'target',
                                    remoteDirectory: 'app',
                                    execCommand: """
                                        pkill -f ${JAR_FILE} || true
                                        sleep 2
                                        cd /home/ec2-user/app
                                        nohup java -jar ${JAR_FILE} > app.log 2>&1 &
                                        echo 'Application started successfully!'
                                    """
                                )
                            ]
                        )
                    ]
                )
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed SUCCESSFULLY!'
            echo "App deployed to http://${EC2_HOST}:8081"
        }
        failure {
            echo 'Pipeline FAILED! Check the logs above.'
        }
        always {
            cleanWs()
        }
    }
}
```

Push the updated Jenkinsfile:
```powershell
cd C:\Users\ASUS\Desktop\Devops_Project
git add .
git commit -m "Update Jenkinsfile with AWS EC2 deployment"
git push
```

---

## 9. Security Groups and Networking

### What is a Security Group?
Think of it as a **firewall** — it controls which traffic can reach your EC2 instance.

### Inbound Rules (who can connect TO your server):
| Rule | Port | Who | Why |
|------|------|-----|-----|
| SSH | 22 | Your IP only | So only you can SSH in |
| HTTP | 80 | Everyone | Web traffic |
| HTTPS | 443 | Everyone | Secure web traffic |
| Custom TCP | 8080 | Everyone | Jenkins UI |
| Custom TCP | 8081 | Everyone | Your Java app |

### How to Edit Security Groups:
1. Go to **EC2 Dashboard** → **Instances**
2. Click on your instance
3. Click the **Security** tab
4. Click the **security group link** (e.g., `sg-xxx`)
5. Click **"Edit inbound rules"**
6. **Add rule** for each entry above
7. Click **"Save rules"**

### Outbound Rules:
- Default allows ALL outbound traffic (your server can reach the internet)
- Keep this as is

---

## 10. AWS Free Tier Limits

**IMPORTANT — Avoid charges!**

| Service | Free Tier Limit | Watch Out For |
|---------|----------------|---------------|
| EC2 t2.micro | 750 hrs/month | Don't launch multiple instances |
| EBS Storage | 30 GB | Don't create large volumes |
| Data Transfer | 15 GB/month | Don't transfer huge files |
| S3 | 5 GB | Don't store too much |

### How to Avoid Charges:
1. **Stop your EC2 when not using it:** Instances → Select → Instance state → Stop
2. **Set up billing alerts:**
   - Search "Billing" in AWS Console
   - Go to **Budgets** → **Create budget**
   - Set a $0 or $1 budget with email alerts
3. **Delete resources when done:** Terminate EC2, delete volumes, release Elastic IPs
4. **Check regularly:** Go to **Billing Dashboard** to see if you owe anything

### How to Stop EC2 Instance:
1. Go to **EC2 Dashboard** → **Instances**
2. Select your instance
3. Click **"Instance state"** → **"Stop instance"**
4. Click **"Stop"**
> Stopping an instance is like shutting down your computer — you can start it again later. But the Public IP may change!

### How to Terminate (Delete) EC2 Instance:
1. Select your instance
2. Click **"Instance state"** → **"Terminate instance"**
3. Click **"Terminate"**
> This permanently deletes the instance. Do this when you're completely done.

---

## 11. Complete Project Walkthrough

Here's the entire flow from start to finish:

### Step-by-Step Summary:

```
1. Install Git, Java, Maven on Windows
2. Create Java project with Maven
3. Write code and tests
4. Initialize Git repo
5. Push to GitHub
6. Install and configure Jenkins
7. Create Jenkinsfile (pipeline as code)
8. Create Jenkins Pipeline job
9. Create AWS account
10. Launch EC2 instance
11. Configure Jenkins SSH to EC2
12. Update Jenkinsfile with deploy stage
13. Push changes → Pipeline runs automatically!
```

### The Complete Flow:
```
Developer writes code on Windows
         |
         v
    git add . + git commit + git push
         |
         v
    GitHub repository (stores code)
         |
         v
    Jenkins detects changes (poll or webhook)
         |
         v
    Jenkins Pipeline runs:
    ├── Checkout: Download code
    ├── Build: mvn compile
    ├── Test: mvn test
    ├── Package: mvn package (creates JAR)
    └── Deploy: SCP JAR to EC2 + SSH start app
         |
         v
    Application running on AWS EC2!
```

### Final Test: The Complete Cycle
```powershell
# On your Windows PC:
cd C:\Users\ASUS\Desktop\Devops_Project

# Make a code change
# (edit HelloDevOps.java - change the greeting message)

# Commit and push
git add .
git commit -m "Update greeting message"
git push

# Now go to Jenkins (http://localhost:8080)
# Watch the pipeline trigger automatically
# Watch each stage execute
# Check EC2 - your updated app is running!
```

---

## Quick AWS Reference

| Task | Steps |
|------|-------|
| Start EC2 | EC2 → Instances → Select → Instance state → Start |
| Stop EC2 | EC2 → Instances → Select → Instance state → Stop |
| SSH into EC2 | `ssh -i key.pem ec2-user@IP_ADDRESS` |
| View running apps | `ps aux \| grep java` |
| View app logs | `cat /home/ec2-user/app/app.log` |
| Stop app | `pkill -f hello-devops` |
| Check billing | Search "Billing" → Dashboard |

---

> **See `guide_part4_final_summary.md` for the complete cheat sheet and study notes for your internship.**
