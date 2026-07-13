$ErrorActionPreference = "Stop"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectDir = Split-Path -Parent $ScriptDir

$JarSrc = Join-Path $ProjectDir "target\SecureGuard.jar"
$InstallDir = Join-Path $env:USERPROFILE ".secureguard"
$JarDest = Join-Path $InstallDir "SecureGuard.jar"
$CmdFile = Join-Path $InstallDir "secureguard.cmd"

Write-Host "Installing SecureGuard..."

if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Host "ERROR: Java is not installed or not available in PATH."
    Write-Host "Install Java 17 or later before installing SecureGuard."
    exit 1
}

if (-not (Test-Path $JarSrc)) {
    Write-Host "ERROR: SecureGuard.jar was not found."
    Write-Host ""
    Write-Host "Build SecureGuard first using:"
    Write-Host "mvn clean package"
    exit 1
}

New-Item -ItemType Directory -Force -Path $InstallDir | Out-Null

Copy-Item $JarSrc -Destination $JarDest -Force

@"
@echo off
java -jar "%USERPROFILE%\.secureguard\SecureGuard.jar" %*
"@ | Set-Content $CmdFile

$UserPath = [Environment]::GetEnvironmentVariable("Path", "User")

if ($UserPath -notlike "*$InstallDir*") {
    [Environment]::SetEnvironmentVariable(
        "Path",
        "$UserPath;$InstallDir",
        "User"
    )
}

Write-Host ""
Write-Host "SecureGuard installed successfully!"
Write-Host ""
Write-Host "Restart your terminal and run:"
Write-Host ""
Write-Host "secureguard --version"
Write-Host "secureguard scan ."
