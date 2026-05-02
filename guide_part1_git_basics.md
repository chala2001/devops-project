# DevOps Complete Guide - Part 1: Git & GitHub
## From Zero to Hero (Windows)

---

## Table of Contents
1. [What is Git?](#1-what-is-git)
2. [Install Git on Windows](#2-install-git-on-windows)
3. [Configure Git](#3-configure-git)
4. [Create GitHub Account](#4-create-github-account)
5. [Git Core Concepts](#5-git-core-concepts)
6. [Every Git Command You Need](#6-every-git-command-you-need)
7. [Branching & Merging](#7-branching--merging)
8. [Pull Requests](#8-pull-requests)
9. [Hands-On Exercise](#9-hands-on-exercise)

---

## 1. What is Git?

**Git** is a **version control system**. Think of it like "Track Changes" in Microsoft Word, but for code.

**Why do we need it?**
- Track every change you make to your code
- Go back to any previous version if something breaks
- Multiple people can work on the same project without conflicts
- Required in every software company

**GitHub** is a website that hosts your Git repositories online (like Google Drive for code).

**Key Terms:**
| Term | Meaning |
|------|---------|
| **Repository (repo)** | A folder tracked by Git |
| **Commit** | A saved snapshot of your code |
| **Branch** | A parallel version of your code |
| **Push** | Upload your commits to GitHub |
| **Pull** | Download changes from GitHub |
| **Clone** | Copy a GitHub repo to your computer |
| **Merge** | Combine two branches together |
| **Staging Area** | A preparation area before committing |

---

## 2. Install Git on Windows

### Step 1: Download Git
1. Open your browser
2. Go to: `https://git-scm.com/downloads`
3. Click **"Download for Windows"**
4. The `.exe` file will download (around 50MB)

### Step 2: Run the Installer
1. Double-click the downloaded `.exe` file
2. Click **"Yes"** when Windows asks for permission
3. **Installation screens** (click Next for most, but pay attention to these):

   **Screen: Select Components**
   - Keep all defaults checked
   - Click **Next**

   **Screen: Default Editor**
   - Change from Vim to **"Use Visual Studio Code as Git's default editor"** (if you have VS Code)
   - If not, select **"Use Notepad as Git's default editor"**
   - Click **Next**

   **Screen: Initial Branch Name**
   - Select **"Override the default branch name"**
   - Type: `main`
   - Click **Next**

   **Screen: PATH Environment**
   - Select **"Git from the command line and also from 3rd-party software"** (recommended)
   - Click **Next**

   **For all remaining screens:** Click **Next** with defaults, then click **Install**

4. Click **Finish** when done

### Step 3: Verify Installation
Open **PowerShell** (press `Win + X`, click "Windows PowerShell") and type:

```powershell
git --version
```

**Expected output:** `git version 2.xx.x.windows.x`

If you see a version number, Git is installed!

---

## 3. Configure Git

After installing, you must tell Git who you are. Open PowerShell:

### Set Your Name
```powershell
git config --global user.name "Your Full Name"
```
**Example:**
```powershell
git config --global user.name "John Doe"
```
> **What this does:** Every commit you make will have this name attached to it.

### Set Your Email
```powershell
git config --global user.email "your.email@example.com"
```
> **What this does:** Links your commits to your GitHub account. Use the SAME email you'll use for GitHub.

### Set Default Branch Name
```powershell
git config --global init.defaultBranch main
```
> **What this does:** When you create a new repo, the default branch will be called `main` instead of `master`.

### Verify Your Configuration
```powershell
git config --list
```
You should see your name, email, and other settings listed.

---

## 4. Create GitHub Account

### Step 1: Sign Up
1. Go to `https://github.com`
2. Click **"Sign Up"** (top right)
3. Enter your email address, click **Continue**
4. Create a password, click **Continue**
5. Choose a username, click **Continue**
6. Solve the puzzle verification
7. Click **"Create account"**
8. Enter the verification code sent to your email

### Step 2: Generate SSH Key (So you don't type password every time)
Open PowerShell:

```powershell
ssh-keygen -t ed25519 -C "your.email@example.com"
```

**When it asks:**
- `Enter file to save the key`: Press **Enter** (use default)
- `Enter passphrase`: Press **Enter** (no password for simplicity)
- `Enter passphrase again`: Press **Enter**

Now start the SSH agent:
```powershell
Get-Service -Name ssh-agent | Set-Service -StartupType Manual
Start-Service ssh-agent
ssh-add $env:USERPROFILE\.ssh\id_ed25519
```

Copy your public key:
```powershell
Get-Content $env:USERPROFILE\.ssh\id_ed25519.pub
```

### Step 3: Add SSH Key to GitHub
1. Go to `https://github.com/settings/keys`
2. Click **"New SSH Key"**
3. **Title:** `My Windows PC`
4. **Key type:** Authentication Key
5. **Key:** Paste the key you copied
6. Click **"Add SSH Key"**

### Step 4: Test Connection
```powershell
ssh -T git@github.com
```
Type `yes` when asked. You should see: `Hi username! You've successfully authenticated`

---

## 5. Git Core Concepts

### The Three Areas of Git

```
Working Directory  -->  git add  -->  Staging Area  -->  git commit  -->  Repository (.git)
(Your files)                         (Ready to save)                     (Saved permanently)
```

1. **Working Directory** = Your project folder where you edit files
2. **Staging Area** = A "loading dock" where you place files you want to save
3. **Repository** = The permanent history of all your commits

**Analogy:** Think of it like packing a box for shipping:
- Working Directory = Items on your desk
- Staging Area = Items you put in the box
- Commit = Sealing the box and labeling it

---

## 6. Every Git Command You Need

### BEGINNER COMMANDS

#### `git init` - Create a New Repository
```powershell
cd C:\Users\ASUS\Desktop\Devops_Project
git init
```
**What happens:** Creates a hidden `.git` folder. This folder IS the repository.

#### `git status` - Check What Changed
```powershell
git status
```
- Red files = Changed but NOT staged
- Green files = Staged and ready to commit
- Clean = Nothing to commit

**Use this ALL THE TIME.** It's your best friend.

#### `git add` - Stage Files for Commit
```powershell
git add filename.java        # Stage one file
git add .                    # Stage ALL changed files
git add file1.java file2.java  # Stage multiple files
```

#### `git commit` - Save a Snapshot
```powershell
git commit -m "Your descriptive message here"
```
Good messages: `"Add HelloWorld Java application"`, `"Fix login bug"`
Bad messages: `"update"`, `"asdf"`, `"fixed stuff"`

#### `git log` - View Commit History
```powershell
git log                          # Full details
git log --oneline                # Compact view
git log --oneline --graph --all  # Visual branch view
```
Press `q` to exit.

#### `git diff` - See What Changed
```powershell
git diff              # Unstaged changes
git diff --staged     # Staged changes
```

### REMOTE REPOSITORY COMMANDS

#### `git remote add` - Connect to GitHub
```powershell
git remote add origin git@github.com:YOUR_USERNAME/YOUR_REPO.git
git remote -v    # Verify
```

#### `git push` - Upload to GitHub
```powershell
git push -u origin main   # First time
git push                  # After first time
```

#### `git pull` - Download from GitHub
```powershell
git pull origin main
```

#### `git clone` - Copy a GitHub Repo
```powershell
git clone git@github.com:username/repo-name.git
```

#### `git fetch` - Download Without Merging
```powershell
git fetch origin
```
`git pull` = `git fetch` + `git merge` (automatic). `git fetch` just downloads.

### BRANCHING COMMANDS

#### `git branch` - List/Create/Delete Branches
```powershell
git branch                    # List all branches
git branch feature-login      # Create new branch
git branch -d feature-login   # Delete branch
```

#### `git checkout` - Switch Branches
```powershell
git checkout feature-login          # Switch to branch
git checkout -b new-branch-name     # Create AND switch
```

#### `git merge` - Combine Branches
```powershell
git checkout main              # Switch to target branch first
git merge feature-login        # Merge feature into main
```

### UNDO / FIX COMMANDS

#### `git restore` - Discard Changes
```powershell
git restore filename.java           # Discard working changes
git restore --staged filename.java  # Unstage a file
```

#### `git reset` - Undo Commits
```powershell
git reset --soft HEAD~1    # Undo commit, keep changes staged
git reset HEAD~1           # Undo commit, unstage changes
git reset --hard HEAD~1    # Undo commit AND delete changes (DANGEROUS)
```

#### `git stash` - Temporarily Save Changes
```powershell
git stash           # Save and clean working directory
git stash pop       # Restore saved changes
git stash list      # List all stashes
```

#### `git revert` - Safely Undo a Commit
```powershell
git revert abc1234    # Creates a NEW commit that undoes the old one
```

### .gitignore - Tell Git to Ignore Files
Create a file called `.gitignore` in your project root:
```
# Compiled files
*.class
*.jar
# IDE files
.idea/
.vscode/
# Build output
target/
build/
# OS files
Thumbs.db
```

---

## 7. Branching and Merging

### Merge Conflicts
When two branches change the SAME line, you get a conflict:
```
<<<<<<< HEAD
System.out.println("Hello from main");
=======
System.out.println("Hello from feature");
>>>>>>> feature-branch
```

**To resolve:**
1. Open the file, choose which code to keep
2. Remove the `<<<<`, `====`, `>>>>` markers
3. `git add .` then `git commit -m "Resolve merge conflict"`

---

## 8. Pull Requests

1. Push your branch: `git push origin feature-branch`
2. Go to your repo on GitHub
3. Click **"Compare & pull request"**
4. Fill in Title and Description
5. Click **"Create pull request"**
6. After review, click **"Merge pull request"**

---

## 9. Hands-On Exercise

```powershell
cd C:\Users\ASUS\Desktop\Devops_Project
git init
echo "# My DevOps Project" > README.md
git status
git add README.md
git status
git commit -m "Initial commit - add README"
git log --oneline
git checkout -b feature/add-hello
# (create HelloWorld.java - see Part 2)
git add .
git commit -m "Add HelloWorld Java application"
git checkout main
git merge feature/add-hello
git branch -d feature/add-hello
git log --oneline --graph --all
```

---

## Quick Reference Card

| What You Want To Do | Command |
|---------------------|---------|
| Start a new repo | `git init` |
| Check status | `git status` |
| Stage files | `git add .` |
| Commit changes | `git commit -m "message"` |
| View history | `git log --oneline` |
| Create branch | `git checkout -b name` |
| Switch branch | `git checkout name` |
| Merge branch | `git merge name` |
| Push to GitHub | `git push origin main` |
| Pull from GitHub | `git pull origin main` |
| Clone a repo | `git clone URL` |
| See changes | `git diff` |
| Undo changes | `git restore filename` |

---

> **Next: See `guide_part2_java_app_and_jenkins.md` for creating the Java application and setting up Jenkins CI/CD pipeline.**
