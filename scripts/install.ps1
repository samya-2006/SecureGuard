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

Write-Host "Installed. Try: secureguard ."
