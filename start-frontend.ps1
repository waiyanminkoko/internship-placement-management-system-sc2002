# Quick Start Script for Frontend
# Run this script to start the frontend development server

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Internship Placement Management System" -ForegroundColor Cyan
Write-Host "Frontend Development Server Startup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Node.js is installed
Write-Host "Checking Node.js installation..." -ForegroundColor Yellow
$nodeCheck = Get-Command node -ErrorAction SilentlyContinue

if ($null -eq $nodeCheck) {
    Write-Host "ERROR: Node.js is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Node.js from https://nodejs.org/" -ForegroundColor Red
    pause
    exit 1
}

Write-Host "Node.js found: $($nodeCheck.Source)" -ForegroundColor Green
$nodeVersion = node -v
Write-Host "Version: $nodeVersion" -ForegroundColor Green
Write-Host ""

# Check if npm is installed
Write-Host "Checking npm installation..." -ForegroundColor Yellow
$npmCheck = Get-Command npm -ErrorAction SilentlyContinue

if ($null -eq $npmCheck) {
    Write-Host "ERROR: npm is not installed" -ForegroundColor Red
    pause
    exit 1
}

Write-Host "npm found: $($npmCheck.Source)" -ForegroundColor Green
$npmVersion = npm -v
Write-Host "Version: $npmVersion" -ForegroundColor Green
Write-Host ""

# Get current directory and construct frontend path
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
$frontendPath = Join-Path $scriptPath "frontend"

# Check if frontend directory exists
if (-not (Test-Path $frontendPath)) {
    Write-Host "ERROR: Frontend directory not found at $frontendPath" -ForegroundColor Red
    pause
    exit 1
}

Write-Host "Frontend directory: $frontendPath" -ForegroundColor Cyan
Write-Host ""

# Navigate to frontend directory
Set-Location -Path $frontendPath

# Check if package.json exists
if (-not (Test-Path "package.json")) {
    Write-Host "ERROR: package.json not found in frontend directory" -ForegroundColor Red
    Set-Location -Path $scriptPath
    pause
    exit 1
}

# Check if node_modules exists
if (-not (Test-Path "node_modules")) {
    Write-Host "Installing dependencies (first time setup)..." -ForegroundColor Yellow
    Write-Host "This may take several minutes..." -ForegroundColor Yellow
    npm install
    if ($LASTEXITCODE -ne 0) {
        Write-Host "ERROR: npm install failed" -ForegroundColor Red
        Set-Location -Path $scriptPath
        pause
        exit 1
    }
    Write-Host ""
}

# Start the development server
Write-Host "Starting frontend development server..." -ForegroundColor Yellow
Write-Host "Server will start on http://localhost:5173" -ForegroundColor Green
Write-Host ""
Write-Host "Press Ctrl+C to stop the server" -ForegroundColor Cyan
Write-Host ""

# Open browser after a short delay (in background job)
Start-Job -ScriptBlock {
    Start-Sleep -Seconds 3
    Start-Process "http://localhost:5173"
} | Out-Null

# Run npm dev script using npx for better path handling on Windows
npm run dev

Write-Host ""
Write-Host "Server stopped." -ForegroundColor Yellow
Set-Location -Path $scriptPath
pause
