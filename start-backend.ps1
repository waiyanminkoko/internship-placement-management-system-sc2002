# Quick Start Script for Backend
# Run this script to start the backend server

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Internship Placement Management System" -ForegroundColor Cyan
Write-Host "Backend Server Startup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Get current directory
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path

# Check if pom.xml exists
if (-not (Test-Path (Join-Path $scriptPath "pom.xml"))) {
    Write-Host "ERROR: pom.xml not found. Are you in the project root?" -ForegroundColor Red
    Write-Host "Script location: $scriptPath" -ForegroundColor Yellow
    pause
    exit 1
}

# Ensure we're in the project directory
Set-Location -Path $scriptPath

# Check if Maven is installed
Write-Host "Checking Maven installation..." -ForegroundColor Yellow
$mavenCheck = Get-Command mvn -ErrorAction SilentlyContinue

if ($null -eq $mavenCheck) {
    Write-Host "ERROR: Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Maven from https://maven.apache.org/" -ForegroundColor Red
    pause
    exit 1
}

Write-Host "Maven found: $($mavenCheck.Source)" -ForegroundColor Green
Write-Host ""

# Check if Java is installed
Write-Host "Checking Java installation..." -ForegroundColor Yellow
$javaCheck = Get-Command java -ErrorAction SilentlyContinue

if ($null -eq $javaCheck) {
    Write-Host "ERROR: Java is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install JDK 21 or higher" -ForegroundColor Red
    pause
    exit 1
}

Write-Host "Java found: $($javaCheck.Source)" -ForegroundColor Green
$javaVersion = java -version 2>&1 | Select-String "version" | Out-String
Write-Host $javaVersion
Write-Host ""

Write-Host "Project directory: $scriptPath" -ForegroundColor Cyan
Write-Host ""

Write-Host "Compiling the application..." -ForegroundColor Yellow
mvn compile -q

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Compilation failed" -ForegroundColor Red
    pause
    exit 1
}

Write-Host ""
Write-Host "Starting the server..." -ForegroundColor Yellow
Write-Host "This may take a few minutes on first run..." -ForegroundColor Yellow
Write-Host ""

mvn spring-boot:run

Write-Host ""
Write-Host "Server stopped." -ForegroundColor Yellow
pause
