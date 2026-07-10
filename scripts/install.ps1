# SecureGuard installer (Windows PowerShell)
$InstallDir = "$env:USERPROFILE\.secureguard"
New-Item -ItemType Directory -Force -Path $InstallDir | Out-Null

$JarSrc = Join-Path $PSScriptRoot "..\target\SecureGuard.jar"
Copy-Item $JarSrc -Destination "$InstallDir\SecureGuard.jar" -Force

$WrapperPath = "$InstallDir\secureguard.cmd"
@"
@echo off
java -jar "%USERPROFILE%\.secureguard\SecureGuard.jar" %*
"@ | Set-Content -Path $WrapperPath -Encoding ASCII

$UserPath = [Environment]::GetEnvironmentVariable("Path", "User")
if ($UserPath -notlike "*$InstallDir*") {
    [Environment]::SetEnvironmentVariable("Path", "$UserPath;$InstallDir", "User")
    Write-Host "Added $InstallDir to your user PATH. Restart your terminal."
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
