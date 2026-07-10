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

SecureGuard is a lightweight Java-based Static Application Security Testing (SAST) command-line tool that helps developers identify common security vulnerabilities directly from source code before deployment.

Unlike traditional regex-only scanners, SecureGuard combines:

- Regex-based detection
- Value-based heuristic validation
- Language-aware rule filtering

to reduce false positives while remaining lightweight, fast, and easy to use.

SecureGuard currently supports **9 programming languages** and **10 built-in security rules**.

---

# Features

- Cross-language source code scanning
- Regex-based vulnerability detection
- Value-based heuristic validation
- Recursive project scanning
- Language-aware rule filtering
- Line number reporting
- Severity reporting
- Confidence reporting
- Aggregated console reporting
- Modular architecture
- Standalone executable JAR
- Offline scanning
- Lightweight CLI
- Installable command-line interface
- Global `secureguard` command

---

# Quick Usage

Scan the current directory:

```bash
secureguard scan .
```

Scan another project:

```bash
secureguard scan "C:\Projects\MyApplication"
```

Display help:

```bash
secureguard --help
```

Display version:

```bash
secureguard --version
```

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

# Architecture

```
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

- Main
- RuleLoader
- FileScanner
- LanguageDetector
- RuleEngine
- ValidatorFactory
- ValueValidator
- ValueAnalyzer
- ConsoleReporter

---

# Project Structure

```
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

# Getting Started

## Clone the Repository

```bash
git clone https://github.com/samya-2006/SecureGuard.git
cd SecureGuard
```

---

## Requirements

- Java 17 or later
- Apache Maven 3.8+

---

## Build

```bash
mvn clean package
```

This generates:

```text
target/SecureGuard.jar
```

---

## Install the CLI (Windows)

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\install.ps1
```

Restart your terminal after installation.

---

# Running SecureGuard

Navigate to the project you want to scan:

```bash
cd C:\Path\To\Your\Project
```

Scan the current directory:

```bash
secureguard scan .
```

Scan another project directly:

```bash
secureguard scan "C:\Projects\SampleApp"
```

Display help:

```bash
secureguard --help
```

Display version:

```bash
secureguard --version
```

---

## Run Without Installation

You can also execute SecureGuard directly:

```bash
java -jar target/SecureGuard.jar scan .
```

---

# Sample Output

```text
=========================================
          SecureGuard v2.1.0
=========================================

Cross-Language Security Scanner

Initializing...

========================================
File : Test.java
========================================

Rule ID        : SG001
Rule           : Hardcoded Credentials & Secrets
Severity       : CRITICAL
Error (Line 10): private static final String PASSWORD = "Admin@123";

----------------------------------------

Rule ID        : SG003
Rule           : SQL Injection
Severity       : CRITICAL
Error (Line 22): stmt.executeQuery(query);

----------------------------------------

Files Scanned : 32
Total Issues  : 26
```

---

# Why SecureGuard?

- Lightweight and fast
- Runs completely offline
- Cross-language scanning
- Modular architecture
- Heuristic validation reduces false positives
- Easy to extend with new rules
- Suitable for secure coding education
- Useful during secure code reviews

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

- Rule filtering

```text
--rule SG003
```

- Severity filtering

```text
--severity HIGH
```

- `.secureguardignore` support
- JSON report generation
- SARIF export
- Additional rule improvements
- AST-based analysis (future major version)


---

# Support

If SecureGuard helped you, consider giving the repository a ⭐.

Feedback and contributions are always welcome.
