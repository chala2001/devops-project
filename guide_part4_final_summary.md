# DevOps Complete Guide - Part 4: Final Summary & Cheat Sheet
## Everything You Need for Your Internship

---

## Complete File Structure

Your project should look like this when complete:

```
C:\Users\ASUS\Desktop\Devops_Project\
├── guide_part1_git_basics.md          ← Git & GitHub guide
├── guide_part2_java_app_and_jenkins.md ← Java + Jenkins guide
├── guide_part3_aws_deployment.md       ← AWS deployment guide
├── guide_part4_final_summary.md        ← This file (cheat sheet)
├── pom.xml                             ← Maven configuration
├── Jenkinsfile                         ← CI/CD pipeline definition
├── .gitignore                          ← Files Git should ignore
├── README.md                           ← Project description
├── deploy.sh                           ← Deployment script
└── src/
    ├── main/java/com/devops/app/
    │   └── HelloDevOps.java            ← Main application
    └── test/java/com/devops/app/
        └── HelloDevOpsTest.java        ← Unit tests
```

---

## DevOps Concepts Cheat Sheet

### 1. Version Control (Git)
| Concept | Explanation |
|---------|-------------|
| Repository | A project folder tracked by Git |
| Commit | A saved snapshot of your code at a point in time |
| Branch | A parallel copy of code for isolated development |
| Merge | Combining changes from one branch into another |
| Pull Request | A request to review and merge code changes |
| Clone | Copy a remote repository to your local machine |
| Fork | Copy someone else's repo to your GitHub account |
| HEAD | Pointer to the latest commit on current branch |
| Origin | Default name for the remote repository |
| Staging Area | Intermediate area where you prepare files before committing |

### 2. CI/CD Pipeline
| Concept | Explanation |
|---------|-------------|
| CI (Continuous Integration) | Automatically build and test code on every push |
| CD (Continuous Delivery) | Automatically prepare code for deployment |
| CD (Continuous Deployment) | Automatically deploy to production |
| Pipeline | The automated sequence of stages (build → test → deploy) |
| Build | Compiling source code into executable format |
| Artifact | The output of a build (e.g., JAR file) |
| Stage | One step in the pipeline (e.g., Build, Test, Deploy) |
| Agent | The machine that runs the pipeline |
| Trigger | What starts the pipeline (push, schedule, manual) |
| Jenkinsfile | Pipeline defined as code (Infrastructure as Code) |

### 3. AWS Cloud
| Concept | Explanation |
|---------|-------------|
| EC2 | Virtual server in the cloud |
| AMI | Template for creating EC2 instances (like a disk image) |
| Instance Type | Size of the virtual server (CPU, RAM) |
| Security Group | Firewall rules controlling network access |
| Key Pair | SSH keys for secure server access |
| Region | Geographic location of AWS data centers |
| Availability Zone | Isolated data center within a region |
| S3 | Object storage service (files in the cloud) |
| IAM | Identity and Access Management (users and permissions) |
| VPC | Virtual Private Cloud (your private network in AWS) |
| Free Tier | 12 months of limited free AWS usage |
| EBS | Elastic Block Store (hard drives for EC2) |

### 4. DevOps Culture
| Concept | Explanation |
|---------|-------------|
| DevOps | Development + Operations working together |
| IaC | Infrastructure as Code (manage infra with code) |
| Automation | Replace manual tasks with scripts/tools |
| Monitoring | Watching your application's health and performance |
| Containerization | Packaging apps with dependencies (Docker) |
| Orchestration | Managing multiple containers (Kubernetes) |
| Microservices | Breaking apps into small independent services |
| Agile | Iterative development methodology |

---

## Git Commands - Complete Reference

### Setup
```powershell
git config --global user.name "Your Name"
git config --global user.email "you@example.com"
git config --global init.defaultBranch main
git config --list
```

### Create & Clone
```powershell
git init                              # New repo in current folder
git clone <url>                       # Copy remote repo
git clone <url> .                     # Clone into current folder
```

### Daily Workflow
```powershell
git status                            # What changed?
git add .                             # Stage all changes
git add <file>                        # Stage one file
git commit -m "message"               # Save snapshot
git push                              # Upload to GitHub
git pull                              # Download from GitHub
```

### Branching
```powershell
git branch                            # List branches
git branch <name>                     # Create branch
git checkout <name>                   # Switch branch
git checkout -b <name>                # Create + switch
git merge <name>                      # Merge branch into current
git branch -d <name>                  # Delete branch
```

### History & Differences
```powershell
git log --oneline                     # Compact history
git log --oneline --graph --all       # Visual branch history
git diff                              # Unstaged changes
git diff --staged                     # Staged changes
git show <commit-hash>                # Details of a commit
```

### Undo
```powershell
git restore <file>                    # Discard working changes
git restore --staged <file>           # Unstage a file
git reset --soft HEAD~1               # Undo commit, keep staged
git reset HEAD~1                      # Undo commit, keep changes
git reset --hard HEAD~1               # Undo commit + delete changes
git revert <hash>                     # Create undo commit
git stash                             # Save changes temporarily
git stash pop                         # Restore stashed changes
```

### Remote
```powershell
git remote add origin <url>           # Add remote
git remote -v                         # List remotes
git push -u origin main               # First push
git push                              # Subsequent pushes
git fetch                             # Download without merge
git pull                              # Download and merge
```

---

## Jenkins Pipeline Quick Reference

### Jenkinsfile Structure
```groovy
pipeline {
    agent any                     // Where to run
    tools { maven 'Maven3' }      // Tools needed
    environment { KEY = 'value' } // Variables

    stages {
        stage('Name') {
            steps {
                bat 'command'     // Windows command
                // sh 'command'   // Linux command
            }
        }
    }

    post {
        success { echo 'Done!' }
        failure { echo 'Failed!' }
    }
}
```

### Key Jenkins URLs (local)
| URL | What |
|-----|------|
| http://localhost:8080 | Jenkins home |
| http://localhost:8080/job/devops-pipeline | Your pipeline |
| http://localhost:8080/manage | Settings |

---

## AWS EC2 Quick Reference

### SSH Connection
```powershell
ssh -i C:\Users\ASUS\.ssh\devops-key.pem ec2-user@YOUR_IP
```

### Common EC2 Commands (Linux)
```bash
# System
sudo yum update -y                    # Update system
sudo yum install java-21-amazon-corretto-devel -y  # Install Java

# Application
java -jar app.jar                     # Run app (foreground)
nohup java -jar app.jar > log.txt 2>&1 &  # Run in background
ps aux | grep java                    # Check if running
pkill -f app.jar                      # Stop app
cat log.txt                           # View logs
tail -f log.txt                       # Live log view
```

### File Transfer (from Windows to EC2)
```powershell
# Copy file to EC2
scp -i C:\Users\ASUS\.ssh\devops-key.pem .\target\app.jar ec2-user@YOUR_IP:/home/ec2-user/

# Copy folder to EC2
scp -i C:\Users\ASUS\.ssh\devops-key.pem -r .\folder\ ec2-user@YOUR_IP:/home/ec2-user/
```

---

## Interview / Internship Talking Points

### "Explain your CI/CD pipeline"
> "I built a CI/CD pipeline using Jenkins that automates the entire software delivery process. When a developer pushes code to GitHub, Jenkins automatically detects the change, checks out the code, compiles it using Maven, runs unit tests, packages it into a JAR file, and deploys it to an AWS EC2 instance. If any stage fails, the pipeline stops and notifies the team."

### "What is the difference between CI and CD?"
> "CI — Continuous Integration — is about automatically building and testing code every time someone pushes a change. It catches bugs early. CD — Continuous Delivery — takes it further by automatically preparing the code for deployment. Continuous Deployment goes even further by automatically deploying to production without manual approval."

### "Why use Jenkins?"
> "Jenkins is a free, open-source automation server with 1800+ plugins. It supports pipeline as code through Jenkinsfile, can integrate with virtually any tool, and is the industry standard used by companies like Netflix and Facebook."

### "What is a Security Group in AWS?"
> "A Security Group acts as a virtual firewall for EC2 instances. It controls inbound and outbound traffic through rules that specify the protocol, port, and source/destination. For example, I open port 22 for SSH access from my IP only, and port 8080 for the application to be accessible from anywhere."

### "Explain Git branching strategy"
> "We use a branching strategy where `main` holds production-ready code. Developers create feature branches like `feature/login` for new work. Once complete, they create a Pull Request for code review. After approval, the feature branch is merged into main and deleted."

---

## Today's Action Plan (Step by Step)

### Morning (2-3 hours)
- [ ] Install Git on Windows
- [ ] Configure Git (name, email)
- [ ] Create GitHub account
- [ ] Set up SSH keys
- [ ] Practice Git commands (init, add, commit, push, pull, branch, merge)
- [ ] Create the Java project files
- [ ] Build and test with Maven
- [ ] Push project to GitHub

### Afternoon (2-3 hours)
- [ ] Install Jenkins
- [ ] Configure Jenkins (JDK, Maven, credentials)
- [ ] Create Jenkinsfile
- [ ] Create Pipeline job in Jenkins
- [ ] Run pipeline and watch it pass
- [ ] Make a code change, push, verify auto-build

### Evening (2-3 hours)
- [ ] Create AWS Free Tier account
- [ ] Launch EC2 instance
- [ ] Connect via SSH
- [ ] Set up Java on EC2
- [ ] Manual deploy to EC2
- [ ] Configure Jenkins → EC2 deployment
- [ ] Run full pipeline: Push → Build → Test → Deploy to AWS
- [ ] Review all concepts and practice explaining them

---

## Troubleshooting Common Issues

### Git Issues
| Problem | Solution |
|---------|----------|
| `git push` rejected | Run `git pull` first, resolve conflicts, then push |
| Permission denied (SSH) | Re-add SSH key: `ssh-add ~/.ssh/id_ed25519` |
| Wrong branch | `git checkout main` to switch to main |

### Maven Issues
| Problem | Solution |
|---------|----------|
| `mvn not recognized` | Add Maven to PATH environment variable |
| Build fails | Check `pom.xml` for syntax errors |
| Tests fail | Check test code matches the main code |

### Jenkins Issues
| Problem | Solution |
|---------|----------|
| Can't access localhost:8080 | Check if Jenkins service is running (Services app) |
| Pipeline fails at Build | Ensure Maven and JDK are configured in Tools |
| SCM checkout fails | Check GitHub credentials in Jenkins |

### AWS Issues
| Problem | Solution |
|---------|----------|
| Can't SSH to EC2 | Check Security Group allows port 22 from your IP |
| Permission denied (key) | Run `icacls` to fix key permissions |
| EC2 IP changed | EC2 gets new IP after stop/start. Use Elastic IP for permanent IP |

---

## What to Learn Next

After completing this project, explore:

1. **Docker** — Containerize your Java app
2. **Docker Compose** — Run multi-container apps
3. **Kubernetes** — Orchestrate containers at scale
4. **Terraform** — Define AWS infrastructure as code
5. **Ansible** — Automate server configuration
6. **Prometheus + Grafana** — Monitor your applications
7. **GitHub Actions** — CI/CD built into GitHub (alternative to Jenkins)
8. **AWS ECS/EKS** — Run containers on AWS

---

**Good luck with your internship! You've got this! 🚀**
