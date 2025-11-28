#!/bin/bash
# Setup Git hooks for the project

echo "Setting up Git hooks..."

# Make pre-commit hook executable
chmod +x .git/hooks/pre-commit

echo "✅ Git hooks configured successfully!"
echo ""
echo "The pre-commit hook will now run Spotless checks before each commit."
echo ""
echo "Useful commands:"
echo "  ./gradlew spotlessCheck  - Check for formatting issues"
echo "  ./gradlew spotlessApply  - Auto-format all code"
echo ""
