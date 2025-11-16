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
