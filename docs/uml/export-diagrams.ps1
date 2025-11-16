# PowerShell script to export PlantUML diagrams to images
# Requires PlantUML VS Code extension or PlantUML jar file

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "PlantUML Diagram Export Script" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Set paths dynamically based on script location
# This script is located in: <project-root>\docs\uml\
$scriptPath = $PSScriptRoot
$umlDir = $scriptPath
$projectRoot = (Get-Item $scriptPath).Parent.Parent.FullName
$classDir = "$umlDir\class-diagrams"
$sequenceDir = "$umlDir\sequence-diagrams"
$outputDir = "$umlDir\exported-images"

Write-Host "Project Root: $projectRoot" -ForegroundColor Gray
Write-Host "UML Directory: $umlDir" -ForegroundColor Gray
Write-Host ""

# Create output directory if it doesn't exist
if (-not (Test-Path $outputDir)) {
    New-Item -ItemType Directory -Path $outputDir -Force | Out-Null
    Write-Host "Created output directory: $outputDir" -ForegroundColor Green
}

# Check for PlantUML jar
$plantumlJar = "$projectRoot\docs\plantuml.jar"
if (-not (Test-Path $plantumlJar)) {
    Write-Host "PlantUML jar not found. Downloading..." -ForegroundColor Yellow
    try {
        Invoke-WebRequest -Uri "https://github.com/plantuml/plantuml/releases/download/v1.2024.7/plantuml-1.2024.7.jar" -OutFile $plantumlJar
        Write-Host "PlantUML jar downloaded successfully!" -ForegroundColor Green
    } catch {
        Write-Host "Failed to download PlantUML jar. Error: $_" -ForegroundColor Red
        Write-Host ""
        Write-Host "Alternative: Use VS Code PlantUML extension:" -ForegroundColor Yellow
        Write-Host "1. Open the .puml file in VS Code" -ForegroundColor White
        Write-Host "2. Press Alt+D to preview" -ForegroundColor White
        Write-Host "3. Right-click on preview and select 'Export Current Diagram'" -ForegroundColor White
        Write-Host "4. Choose PNG or SVG format" -ForegroundColor White
        exit 1
    }
}

# Find all .puml files from both class and sequence diagrams
$pumlFiles = @()
if (Test-Path $classDir) {
    $pumlFiles += Get-ChildItem -Path $classDir -Filter "*.puml" -File
}
if (Test-Path $sequenceDir) {
    $pumlFiles += Get-ChildItem -Path $sequenceDir -Filter "*.puml" -File
}

if ($pumlFiles.Count -eq 0) {
    Write-Host "No .puml files found in $classDir" -ForegroundColor Red
    exit 1
}

Write-Host "Found $($pumlFiles.Count) PlantUML file(s) to export" -ForegroundColor Cyan
Write-Host ""

# Export each file
foreach ($file in $pumlFiles) {
    Write-Host "Exporting: $($file.Name)" -ForegroundColor Yellow
    
    try {
        # Use absolute path for output directory to avoid PlantUML treating it as relative
        $absoluteOutputDir = (Resolve-Path $outputDir).Path
        
        # Get timestamp before export
        $beforeExport = Get-Date
        
        # Export to PNG - using absolute output path
        $null = & java -jar $plantumlJar -tpng $file.FullName -output $absoluteOutputDir 2>&1
        
        # Check for exported PNG (check files modified after export started)
        $pngFiles = Get-ChildItem -Path $outputDir -Filter "*.png" -File -ErrorAction SilentlyContinue | 
                    Where-Object { $_.LastWriteTime -ge $beforeExport -and $_.BaseName -like "$($file.BaseName)*" }
        
        if ($pngFiles) {
            foreach ($pngFile in $pngFiles) {
                Write-Host "  ✓ PNG exported: $($pngFile.Name)" -ForegroundColor Green
            }
        } else {
            # Check if file exists (might have been created earlier)
            $existingPng = Get-ChildItem -Path $outputDir -Filter "$($file.BaseName)*.png" -File -ErrorAction SilentlyContinue
            if ($existingPng) {
                Write-Host "  ✓ PNG exists: $($existingPng[0].Name)" -ForegroundColor Cyan
            } else {
                Write-Host "  ⚠ PNG file not created" -ForegroundColor Yellow
            }
        }
        
        # Get timestamp before SVG export
        $beforeSvgExport = Get-Date
        
        # Export to SVG (vector format, scalable) - using absolute output path
        $null = & java -jar $plantumlJar -tsvg $file.FullName -output $absoluteOutputDir 2>&1
        
        # Check for exported SVG (check files modified after export started)
        $svgFiles = Get-ChildItem -Path $outputDir -Filter "*.svg" -File -ErrorAction SilentlyContinue | 
                    Where-Object { $_.LastWriteTime -ge $beforeSvgExport -and $_.BaseName -like "$($file.BaseName)*" }
        
        if ($svgFiles) {
            foreach ($svgFile in $svgFiles) {
                Write-Host "  ✓ SVG exported: $($svgFile.Name)" -ForegroundColor Green
            }
        } else {
            # Check if file exists (might have been created earlier)
            $existingSvg = Get-ChildItem -Path $outputDir -Filter "$($file.BaseName)*.svg" -File -ErrorAction SilentlyContinue
            if ($existingSvg) {
                Write-Host "  ✓ SVG exists: $($existingSvg[0].Name)" -ForegroundColor Cyan
            } else {
                Write-Host "  ⚠ SVG file not created" -ForegroundColor Yellow
            }
        }
        
    } catch {
        Write-Host "  ✗ Error exporting $($file.Name): $_" -ForegroundColor Red
    }
    
    Write-Host ""
}

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "Export complete!" -ForegroundColor Green
Write-Host "Exported images are in: $outputDir" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan

# Open the output directory
Start-Process explorer.exe -ArgumentList $outputDir
