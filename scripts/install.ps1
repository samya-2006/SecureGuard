$ErrorActionPreference = "Stop"

$ProjectRoot = Split-Path -Parent $PSScriptRoot
$JarSrc = Join-Path $ProjectRoot "target\SecureGuard.jar"

$InstallDir = Join-Path $env:USERPROFILE ".secureguard"
$BinDir = Join-Path $InstallDir "bin"

Write-Host ""
Write-Host "Installing SecureGuard..."
Write-Host ""

if (-not (Test-Path $JarSrc)) {
    Write-Host "SecureGuard.jar was not found at:"
    Write-Host $JarSrc
    Write-Host ""
    Write-Host "Run 'mvn clean package' before installing."
    exit 1
}

New-Item `
    -ItemType Directory `
    -Force `
    -Path $InstallDir | Out-Null

New-Item `
    -ItemType Directory `
    -Force `
    -Path $BinDir | Out-Null

Copy-Item `
    $JarSrc `
    -Destination (Join-Path $InstallDir "SecureGuard.jar") `
    -Force

$LauncherPath = Join-Path $BinDir "secureguard.cmd"

$Launcher = @'
@echo off
java -cp "%USERPROFILE%\.secureguard\SecureGuard.jar" com.secureguard.cli.SecureGuardCli %*
'@

Set-Content `
    -Path $LauncherPath `
    -Value $Launcher `
    -Encoding ASCII

$UserPath = [Environment]::GetEnvironmentVariable(
    "Path",
    "User"
)

if ($UserPath -notlike "*$BinDir*") {

    $NewPath = if ([string]::IsNullOrWhiteSpace($UserPath)) {
        $BinDir
    } else {
        "$UserPath;$BinDir"
    }

    [Environment]::SetEnvironmentVariable(
        "Path",
        $NewPath,
        "User"
    )

    Write-Host ""
    Write-Host "Added SecureGuard to user PATH."
}

Write-Host ""
Write-Host "SecureGuard installed successfully!"
Write-Host ""
Write-Host "Examples:"
Write-Host "  secureguard scan ."
Write-Host "  secureguard scan C:\Projects\Demo"
Write-Host "  secureguard --help"
Write-Host "  secureguard --version"
Write-Host ""
Write-Host "Restart your terminal if the command is not recognized."