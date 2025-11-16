# Generate Javadoc for Internship Placement Management System
# Author: SC2002 Group 6
# Last Updated: November 16, 2025

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Javadoc Generation Script" -ForegroundColor Cyan
Write-Host "  SC2002 Group 6 - IPMS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Navigate to project root
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent (Split-Path -Parent $scriptPath)
Set-Location $projectRoot

Write-Host "📁 Project Root: $projectRoot" -ForegroundColor Yellow
Write-Host ""

# Check if Maven is installed
Write-Host "🔍 Checking Maven installation..." -ForegroundColor Green
$mavenVersion = mvn -v 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ ERROR: Maven is not installed or not in PATH" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Maven found: $($mavenVersion[0])" -ForegroundColor Green
Write-Host ""

# Clean previous build
Write-Host "🧹 Cleaning previous build..." -ForegroundColor Yellow
mvn clean | Out-Null
Write-Host "✅ Clean complete" -ForegroundColor Green
Write-Host ""

# Generate Javadocs
Write-Host "📚 Generating Javadocs..." -ForegroundColor Yellow
Write-Host "   This may take a few moments..." -ForegroundColor Gray
mvn javadoc:javadoc

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✅ Javadoc generation completed successfully!" -ForegroundColor Green
    Write-Host ""
    
    # Count generated files
    $javadocPath = Join-Path $projectRoot "docs\javadoc"
    $fileCount = (Get-ChildItem -Path $javadocPath -Recurse -File).Count
    $htmlCount = (Get-ChildItem -Path $javadocPath -Filter "*.html" -Recurse).Count
    
    Write-Host "📊 Statistics:" -ForegroundColor Cyan
    Write-Host "   Total files: $fileCount" -ForegroundColor White
    Write-Host "   HTML files: $htmlCount" -ForegroundColor White
    Write-Host "   Location: docs\javadoc\" -ForegroundColor White
    Write-Host ""
    
    # List documented classes
    Write-Host "📖 Documented Classes:" -ForegroundColor Cyan
    $classFiles = Get-ChildItem -Path $javadocPath -Filter "*.html" -Recurse | 
                  Where-Object { 
                      $_.Name -notmatch "^(index|package|help|search|overview|constant|serialized|allclasses|allpackages)" -and 
                      $_.Directory.Name -notmatch "class-use|script-dir|legal|resources" 
                  } | 
                  Select-Object -ExpandProperty Name | 
                  Sort-Object
    
    $classCount = $classFiles.Count
    Write-Host "   Total documented classes: $classCount" -ForegroundColor White
    Write-Host ""
    
    # Ask if user wants to open the documentation
    Write-Host "🌐 Would you like to open the Javadoc in your browser? (Y/N): " -ForegroundColor Yellow -NoNewline
    $response = Read-Host
    
    if ($response -eq "Y" -or $response -eq "y") {
        $indexPath = Join-Path $javadocPath "index.html"
        Start-Process $indexPath
        Write-Host "✅ Javadoc opened in browser" -ForegroundColor Green
    }
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "  Javadoc Generation Complete!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Cyan
    
} else {
    Write-Host ""
    Write-Host "❌ ERROR: Javadoc generation failed!" -ForegroundColor Red
    Write-Host "   Check the Maven output above for details." -ForegroundColor Yellow
    Write-Host ""
    exit 1
}
