# 🛡️ SecureGuard

<p align="center">

![Java](https://img.shields.io/badge/Java-17-orange)
![Version](https://img.shields.io/badge/version-v2.1.0-blue)
![Platform](https://img.shields.io/badge/platform-Cross--Language-success)
![CLI](https://img.shields.io/badge/interface-CLI-lightgrey)

<strong>A Cross-Language Static Application Security Testing (SAST) CLI</strong>

Detect common security vulnerabilities across multiple programming languages using regex-based detection and heuristic validation.

</p>

---

# Overview

SecureGuard is a lightweight Java-based Static Application Security Testing (SAST) command-line tool designed to detect common security vulnerabilities directly from source code before deployment.

SecureGuard combines:

- Regex-based detection
- Value-based heuristic validation
- Language-aware rule filtering

This approach helps reduce false positives while keeping the scanner lightweight, fast, offline, and easy to use.

SecureGuard currently supports **9 programming languages** and **10 built-in security rules**.

---

# Features

- Cross-language source code scanning
- Recursive project scanning
- Regex-based vulnerability detection
- Value-based heuristic validation
- Language-aware rule filtering
- Multiline scanning
- Line number reporting
- Severity reporting
- Confidence reporting
- Aggregated console reporting
- Offline scanning
- Standalone executable JAR
- Installable CLI
- Global `secureguard` command

---

# Installation Methods

SecureGuard can be used in three different ways.

| Method | Recommended For | Java | Git | Maven |
|--------|-----------------|------|-----|-------|
| Download and run JAR | Normal users | Required | Not Required | Not Required |
| Install SecureGuard CLI | CLI users | Required | Required | Required |
| Build and run from source | Developers | Required | Required | Required |

---

# Method 1 — Download and Run SecureGuard

This is the **recommended method for normal users**.

Git and Maven are **not required**.

## Step 1 — Install Java

SecureGuard requires **Java 17 or later**.

Download Java:

https://adoptium.net/temurin/releases/

Select **Java 17 or later** and install the JDK.

### Windows Terminal Installation

Java can also be installed using PowerShell:

```powershell
winget install -e --id EclipseAdoptium.Temurin.17.JDK
```

Restart the terminal after installation.

Verify Java:

```powershell
java -version
```

---

## Step 2 — Download SecureGuard

Open the SecureGuard Releases page:

https://github.com/samya-2006/SecureGuard/releases/latest

Download:

```text
SecureGuard.jar
```

Place the JAR in any folder.

Example:

```text
C:\Tools\SecureGuard
```

---

## Step 3 — Scan a Project

Open PowerShell in the folder containing:

```text
SecureGuard.jar
```

Run:

```powershell
java -jar .\SecureGuard.jar scan "C:\Path\To\Your\Project"
```

Example:

```powershell
java -jar .\SecureGuard.jar scan "C:\Projects\SampleApp"
```

SecureGuard recursively scans supported source files inside the specified project directory.

> **Important:** SecureGuard currently expects a directory path. Pass the folder containing the source files instead of an individual source file.

### Correct

```powershell
java -jar .\SecureGuard.jar scan "C:\Projects\SampleApp"
```

### Incorrect

```powershell
java -jar .\SecureGuard.jar scan "C:\Projects\SampleApp\Test.java"
```

---

# Method 2 — Install SecureGuard as a CLI

Use this method if you want to run SecureGuard globally using:

```powershell
secureguard scan .
```

## Requirements

Install the following tools before continuing.

### Git

Download Git:

https://git-scm.com/download/win

Verify:

```powershell
git --version
```

---

### Java 17 or Later

Download Java:

https://adoptium.net/temurin/releases/

Verify:

```powershell
java -version
```

---

### Apache Maven 3.8 or Later

Download Maven:

https://maven.apache.org/download.cgi

For Windows, download the **Binary zip archive**.

Example:

```text
apache-maven-3.9.x-bin.zip
```

Extract Maven and add its `bin` directory to the system `PATH`.

Verify:

```powershell
mvn -version
```

---

## Step 1 — Clone SecureGuard

Run:

```powershell
git clone https://github.com/samya-2006/SecureGuard.git
```

Enter the SecureGuard directory:

```powershell
cd SecureGuard
```

---

## Step 2 — Build SecureGuard

Run:

```powershell
mvn clean package
```

A successful build generates:

```text
target\SecureGuard.jar
```

The terminal should display:

```text
BUILD SUCCESS
```

---

## Step 3 — Install the SecureGuard CLI

Run:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\install.ps1
```

Restart the terminal after installation.

Verify SecureGuard:

```powershell
secureguard --version
```

---

## Step 4 — Scan a Project

Navigate to the project you want to scan:

```powershell
cd "C:\Path\To\Your\Project"
```

Run:

```powershell
secureguard scan .
```

SecureGuard recursively scans the current project directory.

You can also provide a project directory directly:

```powershell
secureguard scan "C:\Projects\SampleApp"
```

> `secureguard scan .` scans the directory where the terminal is currently opened.

---

# Method 3 — Build and Run From Source

This method is intended for developers who want to modify, test, or contribute to SecureGuard.

## Requirements

Download and install:

- Git: https://git-scm.com/download/win
- Java 17+: https://adoptium.net/temurin/releases/
- Apache Maven 3.8+: https://maven.apache.org/download.cgi

Verify the required tools:

```powershell
git --version
java -version
mvn -version
```

---

## Step 1 — Clone the Repository

```powershell
git clone https://github.com/samya-2006/SecureGuard.git
```

Enter the repository:

```powershell
cd SecureGuard
```

---

## Step 2 — Build the Project

Run:

```powershell
mvn clean package
```

The generated executable JAR is located at:

```text
target\SecureGuard.jar
```

---

## Step 3 — Run the Built JAR

Provide the project directory you want to scan:

```powershell
java -jar .\target\SecureGuard.jar scan "C:\Path\To\Your\Project"
```

Example:

```powershell
java -jar .\target\SecureGuard.jar scan "C:\Projects\SampleApp"
```

> Avoid using `scan .` inside the SecureGuard repository unless you intentionally want to scan SecureGuard's own source and test files.

---

# CLI Commands

| Command | Description |
|---------|-------------|
| `secureguard scan <path>` | Scan a project directory |
| `secureguard scan .` | Scan the current directory |
| `secureguard --help` | Display CLI help |
| `secureguard --version` | Display SecureGuard version |

---

# Supported Languages

| Language | Supported |
|-----------|-----------|
| Java | ✅ |
| Python | ✅ |
| JavaScript | ✅ |
| TypeScript | ✅ |
| C | ✅ |
| C++ | ✅ |
| C# | ✅ |
| Go | ✅ |
| PHP | ✅ |

---

# Supported Security Rules

| Rule ID | Vulnerability | Severity |
|----------|---------------|----------|
| SG001 | Hardcoded Credentials & Secrets | Critical |
| SG002 | Wildcard CORS Configuration | High |
| SG003 | SQL Injection | Critical |
| SG004 | Command Injection | Critical |
| SG005 | Path Traversal | High |
| SG006 | Weak Cryptography | High |
| SG007 | Insecure Random Number Generator | High |
| SG008 | XML External Entity (XXE) | High |
| SG009 | Debug Mode Enabled | Medium |
| SG010 | Insecure HTTP Communication | High |

---

# Sample Output

```text
=========================================
          SecureGuard v2.1.0
=========================================

Cross-Language Security Scanner

Initializing...

=========================================
        SecureGuard Scan Report
=========================================

File : VulnerableApp.java

Rule ID        : SG001
Rule           : Hardcoded Credentials & Secrets
Severity       : CRITICAL
Error (Line 7) : private static final String PASSWORD = "Admin@123";

-----------------------------------------

Rule ID        : SG003
Rule           : SQL Injection
Severity       : CRITICAL
Error (Line 16): stmt.executeQuery(query);

-----------------------------------------

Files Scanned : 1
Total Issues  : 2
```

---

# Architecture

```text
                    Main
                      │
                      ▼
               RuleLoader
                      │
                      ▼
               FileScanner
                      │
                      ▼
          LanguageDetector
                      │
                      ▼
                RuleEngine
               /          \
              ▼            ▼
      Regex Detection  ValidatorFactory
                           │
                           ▼
                    ValueValidator
                           │
                           ▼
                     ValueAnalyzer
                           │
                           ▼
                  ConsoleReporter
```

## Core Components

- `Main`
- `RuleLoader`
- `FileScanner`
- `LanguageDetector`
- `RuleEngine`
- `ValidatorFactory`
- `ValueValidator`
- `ValueAnalyzer`
- `ConsoleReporter`

---

# Project Structure

```text
SecureGuard/
│
├── .github/
├── scripts/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │       └── rules/
│   └── test/
│
├── pom.xml
├── README.md
└── LICENSE
```

---

# Why SecureGuard?

- Lightweight and fast
- Runs completely offline
- Supports multiple programming languages
- Uses heuristic validation to reduce false positives
- Easy to extend with additional security rules
- Suitable for secure coding education
- Useful during local secure code reviews

---

# Roadmap

## ✅ v2.1.0

- Cross-language scanning
- SG001–SG010 implemented
- Regex-based detection
- Value-based heuristic validation
- Recursive project scanning
- Language-aware rule filtering
- Multiline scanning
- Installable CLI
- Global `secureguard` command
- `--help`
- `--version`
- Standalone executable JAR

## Planned

- Rule filtering: `--rule SG003`
- Severity filtering: `--severity HIGH`
- `.secureguardignore` support
- Rule accuracy improvements
- Reduced false positives
- CLI usability improvements
- Performance improvements
- AST-based analysis in a future major version

---

# Support

If SecureGuard helped you, consider giving the repository a ⭐.

Feedback and contributions are welcome.
