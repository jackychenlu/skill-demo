#!/bin/bash
# Gradle Setup Helper Script for Windows/Linux/macOS

echo "=================================="
echo "Spring Boot Demo - Gradle 设置助手"
echo "=================================="
echo ""

# 检查 Gradle 是否安装
if command -v gradle &> /dev/null; then
    GRADLE_VERSION=$(gradle --version | head -n 1)
    echo "✓ Gradle 已安装: $GRADLE_VERSION"
    echo ""
    echo "现在生成 Gradle Wrapper..."
    cd "$(dirname "$0")"
    gradle wrapper --gradle-version 8.5
    echo "✓ Gradle Wrapper 生成完成!"
    exit 0
else
    echo "✗ Gradle 未安装"
    echo ""
    echo "请选择一个选项来安装 Gradle:"
    echo ""
    echo "选项 1 - Windows (使用 Chocolatey):"
    echo "  choco install gradle"
    echo ""
    echo "选项 2 - macOS (使用 Homebrew):"
    echo "  brew install gradle"
    echo ""
    echo "选项 3 - Linux (Ubuntu/Debian):"
    echo "  sudo apt-get install gradle"
    echo ""
    echo "选项 4 - 手动下载:"
    echo "  访问 https://gradle.org/releases/"
    echo "  下载 Gradle 8.5"
    echo "  设置 GRADLE_HOME 环境变量"
    echo ""
    echo "安装完成后，再次运行此脚本。"
    exit 1
fi
