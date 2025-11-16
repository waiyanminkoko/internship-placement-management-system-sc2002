# System Health Check Script
# Run this to verify both backend and frontend are running correctly

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "System Health Check" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Function to test URL
function Test-Endpoint {
    param (
        [string]$Url,
        [string]$Name
    )
    
    try {
        $response = Invoke-WebRequest -Uri $Url -Method GET -TimeoutSec 5 -UseBasicParsing
        if ($response.StatusCode -eq 200) {
            Write-Host "✓ $Name is running" -ForegroundColor Green
            return $true
        }
    }
    catch {
        Write-Host "✗ $Name is NOT running" -ForegroundColor Red
        Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Yellow
        return $false
    }
}

Write-Host "Checking Backend Server..." -ForegroundColor Yellow
$backendHealth = Test-Endpoint -Url "http://localhost:6060/api/health" -Name "Backend API"

Write-Host ""
Write-Host "Checking Frontend Server..." -ForegroundColor Yellow
$frontendHealth = Test-Endpoint -Url "http://localhost:5173" -Name "Frontend"

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan

if ($backendHealth -and $frontendHealth) {
    Write-Host "✓ System is HEALTHY" -ForegroundColor Green
    Write-Host ""
    Write-Host "Access Points:" -ForegroundColor Cyan
    Write-Host "  Backend API:  http://localhost:6060/api" -ForegroundColor White
    Write-Host "  Health Check: http://localhost:6060/api/health" -ForegroundColor White
    Write-Host "  Frontend App: http://localhost:5173" -ForegroundColor White
} elseif ($backendHealth) {
    Write-Host "⚠ Backend is running but Frontend is not" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "To start frontend:" -ForegroundColor Cyan
    Write-Host "  .\start-frontend.ps1" -ForegroundColor White
} elseif ($frontendHealth) {
    Write-Host "⚠ Frontend is running but Backend is not" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "To start backend:" -ForegroundColor Cyan
    Write-Host "  .\start-backend.ps1" -ForegroundColor White
} else {
    Write-Host "✗ System is NOT running" -ForegroundColor Red
    Write-Host ""
    Write-Host "To start both servers:" -ForegroundColor Cyan
    Write-Host "  .\start-system.ps1" -ForegroundColor White
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
pause
