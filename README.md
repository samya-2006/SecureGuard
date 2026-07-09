# 🛡️ SecureGuard

<p align="center">

![Java](https://img.shields.io/badge/Java-17-orange)
![Version](https://img.shields.io/badge/version-v2.0.0-blue)
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

to significantly reduce false positives while remaining lightweight and fast.

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

Core Components

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

## Clone

```bash
git clone https://github.com/samya-2006/SecureGuard.git
```

```bash
cd SecureGuard
```

---

## Requirements

- Java 17+
- Maven 3.8+

---

## Build

```bash
mvn clean package
```

The executable JAR will be generated at:

```text
target/SecureGuard.jar
```

---

# Running SecureGuard

Scan the current directory:

```bash
java -jar target/SecureGuard.jar .
```

Scan another project:

```bash
java -jar target/SecureGuard.jar "C:\Projects\SampleApp"
```

---

# Sample Output

```text
=========================================
          SecureGuard v2.0.0
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

## ✅ v2.0.0

- Cross-language scanning
- SG001–SG010 implemented
- Regex-based detection
- Value-based heuristic validation
- Recursive scanning
- Language-aware rules
- Multiline scanning
- Executable standalone JAR

## Planned

- Professional CLI

```text
secureguard scan .
secureguard scan src
secureguard --help
secureguard --version
```

- Rule filtering

```text
--rule SG003
```

- Severity filtering

```text
--severity HIGH
```

- Ignore file support (.secureguardignore)
- Additional rule improvements
- AST-based analysis (future major version)

---

# Contributing

Contributions, feature requests, bug reports and pull requests are welcome.

If you'd like to improve SecureGuard, feel free to fork the repository and submit a pull request.

---

# License

MIT License (recommended)

---

# Support

If SecureGuard helped you, consider giving the repository a ⭐.

Feedback and contributions are always welcome.
