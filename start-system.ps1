# Complete System Startup Script
# Starts both backend and frontend in separate windows

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Internship Placement Management System" -ForegroundColor Cyan
Write-Host "Complete System Startup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Get the script directory (project root)
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path

Write-Host "Project directory: $scriptPath" -ForegroundColor Cyan
Write-Host ""

# Check prerequisites
Write-Host "Checking prerequisites..." -ForegroundColor Yellow
$missingPrereqs = @()

# Check Java
Write-Host "  Checking Java..." -ForegroundColor Gray
$javaCheck = Get-Command java -ErrorAction SilentlyContinue
if ($null -eq $javaCheck) {
    Write-Host "    ❌ Java not found" -ForegroundColor Red
    $missingPrereqs += "Java 21"
} else {
    Write-Host "    ✅ Java found" -ForegroundColor Green
}

# Check Maven
Write-Host "  Checking Maven..." -ForegroundColor Gray
$mavenCheck = Get-Command mvn -ErrorAction SilentlyContinue
if ($null -eq $mavenCheck) {
    Write-Host "    ❌ Maven not found" -ForegroundColor Red
    $missingPrereqs += "Maven"
} else {
    Write-Host "    ✅ Maven found" -ForegroundColor Green
}

# Check Node.js
Write-Host "  Checking Node.js..." -ForegroundColor Gray
$nodeCheck = Get-Command node -ErrorAction SilentlyContinue
if ($null -eq $nodeCheck) {
    Write-Host "    ❌ Node.js not found" -ForegroundColor Red
    $missingPrereqs += "Node.js"
} else {
    Write-Host "    ✅ Node.js found" -ForegroundColor Green
}

Write-Host ""

# If missing prerequisites, show installation instructions
if ($missingPrereqs.Count -gt 0) {
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "MISSING PREREQUISITES" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "The following software is required:" -ForegroundColor Yellow
    Write-Host ""
    
    if ($missingPrereqs -contains "Java 21") {
        Write-Host "❌ Java 21+" -ForegroundColor Red
        Write-Host "   Download: https://adoptium.net/temurin/releases/?version=21" -ForegroundColor Cyan
        Write-Host "   Install, then restart PowerShell" -ForegroundColor Gray
        Write-Host ""
    }
    
    if ($missingPrereqs -contains "Maven") {
        Write-Host "❌ Maven 3.8+" -ForegroundColor Red
        Write-Host "   Download: https://maven.apache.org/download.cgi (Binary zip)" -ForegroundColor Cyan
        Write-Host "   Extract to C:\Program Files\Apache\maven" -ForegroundColor Gray
        Write-Host "   Add to PATH: C:\Program Files\Apache\maven\bin" -ForegroundColor Gray
        Write-Host ""
    }
    
    if ($missingPrereqs -contains "Node.js") {
        Write-Host "❌ Node.js 18+ (includes npm)" -ForegroundColor Red
        Write-Host "   Download: https://nodejs.org/ (LTS version)" -ForegroundColor Cyan
        Write-Host "   Run installer and accept defaults" -ForegroundColor Gray
        Write-Host ""
    }
    
    Write-Host "After installing, restart PowerShell and run this script again." -ForegroundColor Yellow
    Write-Host ""
    pause
    exit 1
}

Write-Host "All prerequisites found!" -ForegroundColor Green
Write-Host ""

# Verify scripts exist
$backendScript = Join-Path $scriptPath "start-backend.ps1"
$frontendScript = Join-Path $scriptPath "start-frontend.ps1"

if (-not (Test-Path $backendScript)) {
    Write-Host "ERROR: start-backend.ps1 not found at $backendScript" -ForegroundColor Red
    pause
    exit 1
}

if (-not (Test-Path $frontendScript)) {
    Write-Host "ERROR: start-frontend.ps1 not found at $frontendScript" -ForegroundColor Red
    pause
    exit 1
}

# Start Backend
Write-Host "Starting Backend Server..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-ExecutionPolicy", "Bypass", "-File", "`"$backendScript`""

Write-Host "Backend server starting in new window..." -ForegroundColor Green
Write-Host "Waiting 10 seconds for backend to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Start Frontend
Write-Host "Starting Frontend Server..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-ExecutionPolicy", "Bypass", "-File", "`"$frontendScript`""

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "System Startup Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Backend:  http://localhost:6060" -ForegroundColor Cyan
Write-Host "Frontend: http://localhost:5173" -ForegroundColor Cyan
Write-Host ""
Write-Host "Two PowerShell windows have been opened." -ForegroundColor Yellow
Write-Host "Close those windows to stop the servers." -ForegroundColor Yellow
Write-Host ""
pause
