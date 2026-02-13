# Gradle Setup Helper Script for Windows
# 在 PowerShell 中运行: .\setup-gradle.ps1

Write-Host "=================================="
Write-Host "Spring Boot Demo - Gradle 设置助手" -ForegroundColor Green
Write-Host "=================================="
Write-Host ""

# 检查 Gradle 是否安装
$gradle = Get-Command gradle -ErrorAction SilentlyContinue

if ($gradle) {
    Write-Host "✓ Gradle 已安装" -ForegroundColor Green
    & gradle --version
    Write-Host ""
    Write-Host "现在生成 Gradle Wrapper..." -ForegroundColor Yellow
    Push-Location (Split-Path -Parent $MyInvocation.MyCommand.Path)
    & gradle wrapper --gradle-version 8.5
    Pop-Location
    Write-Host "✓ Gradle Wrapper 生成完成!" -ForegroundColor Green
    exit 0
} else {
    Write-Host "✗ Gradle 未安装" -ForegroundColor Red
    Write-Host ""
    Write-Host "请选择一个选项来安装 Gradle:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "选项 1 - 使用 Chocolatey (推荐):"
    Write-Host "  choco install gradle" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "选项 2 - 使用 Scoop:"
    Write-Host "  scoop install gradle" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "选项 3 - 手动下载:"
    Write-Host "  访问 https://gradle.org/releases/" -ForegroundColor Cyan
    Write-Host "  下载 Gradle 8.5"
    Write-Host "  提取到 C:\Program Files\gradle-8.5"
    Write-Host "  设置环境变量 GRADLE_HOME = C:\Program Files\gradle-8.5"
    Write-Host "  将 %GRADLE_HOME%\bin 加入 PATH"
    Write-Host ""
    Write-Host "安装完成后，再次运行此脚本。" -ForegroundColor Yellow
    exit 1
}
