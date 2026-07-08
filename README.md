# 🛡️ SecureGuard

<p align="center">
  <strong>A Cross-Language Static Application Security Testing (SAST) CLI</strong><br>
  Detect common security vulnerabilities across multiple programming languages using regex-based detection and heuristic validation.
</p>

---

## Overview

SecureGuard is a lightweight Java-based Static Application Security Testing (SAST) command-line tool that helps developers identify common security vulnerabilities directly from source code before deployment.

It combines **regex-based detection** with **value-based heuristic validation** to identify insecure coding practices across multiple programming languages.

SecureGuard is designed with a modular architecture, making it lightweight, extensible, and easy to maintain.

---

# Features

- Multi-language source code scanning
- Regex-based vulnerability detection
- Value-based heuristic validation
- Recursive project scanning
- Language-aware rule filtering
- Line number reporting
- Severity-based reporting
- Confidence level reporting
- Aggregated console reporting
- Modular architecture
- Executable standalone JAR

---

# 🌍 Supported Languages

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

#  Supported Security Rules

| Rule ID | Vulnerability | Severity |
|----------|---------------|----------|
| SG001 | Hardcoded Credentials & Secrets | Critical |
| SG002 | Wildcard CORS Configuration | High |
| SG003 | SQL Injection | Critical |
| SG004 | Command Injection | Critical |
| SG005 | Path Traversal | High |
| SG006 | Weak Cryptography | Medium |
| SG007 | Insecure Random Number Generator | Medium |
| SG008 | XML External Entity (XXE) | High |
| SG009 | Debug Mode Enabled | Medium |
| SG010 | Insecure HTTP Communication | Medium |

---

#  Architecture

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

### Core Components

- RuleLoader
- FileScanner
- LanguageDetector
- RuleEngine
- ValidatorFactory
- ValueValidator
- ValueAnalyzer
- ConsoleReporter

---

#  Project Structure

```
SecureGuard/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │       └── rules/
│   └── test/
│
├── pom.xml
├── README.md
└── LICENSE (optional)
```

---

#  Getting Started

## Clone the Repository

```bash
git clone https://github.com/<samya-2006>/SecureGuard.git
```

```bash
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

#  Running SecureGuard

Scan the current directory:

```bash
java -jar target/SecureGuard.jar .
```

Scan a specific project:

```bash
java -jar target/SecureGuard.jar "C:\Path\To\Project"
```

Example:

```bash
java -jar target/SecureGuard.jar "C:\Users\John\Desktop\SampleProject"
```

---

#  Sample Output

```
=========================================
          SecureGuard v1.0.0
=========================================

Cross-Language Security Scanner

Initializing...

=========================================
File : Test.java
=========================================

Rule ID        : SG001
Rule           : Hardcoded Credentials & Secrets
Severity       : CRITICAL
Recommendation : Remove hardcoded secrets and use secure configuration management.
Error (Line 14): private static final String DB_PASS = "password123";
Confidence     : HIGH

-----------------------------------------

Rule ID        : SG003
Rule           : SQL Injection
Severity       : CRITICAL
Recommendation : Use parameterized queries or prepared statements.
Error (Line 26): stmt.executeQuery("SELECT * FROM users WHERE name='" + user + "'");
Confidence     : HIGH

-----------------------------------------

Scan Summary

Files Scanned : 1
Issues Found  : 2
```

*(Sample output abbreviated for readability.)*

---

# Why SecureGuard?

- Lightweight and fast
- Runs completely offline
- Supports multiple programming languages
- Modular architecture
- Easy to extend with additional rules
- Suitable for secure coding education and code reviews

---

#  Roadmap

## Version 1.0.0

- Multi-language scanning
- SG001–SG010 implemented
- Regex-based detection
- Value-based heuristic validation
- Recursive directory scanning
- Severity reporting
- Confidence reporting
- Executable standalone JAR

---

##  Planned Features

- Professional CLI commands

```bash
secureguard scan .
secureguard scan src/
secureguard --help
secureguard --version
```




- JSON reports
- SARIF export
- Custom security rules
- CI/CD with GitHub Actions
- Package manager support (Winget, Scoop, Chocolatey, Homebrew)

---

#  Contributing

Contributions, bug reports, feature requests, and pull requests are welcome.

If you'd like to improve SecureGuard or add new security rules, feel free to fork the repository and submit a pull request.

---

#  License

A project license has not yet been specified.

---

# Support

If you found SecureGuard useful, consider giving the repository a ⭐ on GitHub.

Feedback and contributions are always appreciated.
