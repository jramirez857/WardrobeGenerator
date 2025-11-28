# Code Formatting and Linting

This project uses **Spotless** with **ktlint** to ensure consistent code formatting and style.

## Setup

### 1. Install Git Hooks (Recommended)

Run the setup script to configure pre-commit hooks:

**On Windows:**
```bash
setup-hooks.bat
```

**On Linux/Mac:**
```bash
./setup-hooks.sh
```

This will automatically run formatting checks before each commit.

### 2. Editor Configuration

The project includes an `.editorconfig` file that most IDEs (including Android Studio/IntelliJ) will automatically detect and use.

## Commands

### Check Formatting
Check if your code follows the formatting rules:
```bash
./gradlew spotlessCheck
```

### Auto-Format Code
Automatically format all code to match the style guidelines:
```bash
./gradlew spotlessApply
```

### Format Before Committing
If the pre-commit hook fails:
1. Run `./gradlew spotlessApply` to auto-format
2. Stage the formatted changes: `git add .`
3. Commit again: `git commit -m "your message"`

## What Gets Formatted

- **Kotlin files** (`.kt`):
  - 4 spaces for indentation
  - Max line length: 120 characters
  - Trailing whitespace removed
  - Files end with newline
  - ktlint standard rules applied

- **Kotlin Gradle files** (`.gradle.kts`):
  - ktlint rules applied

- **XML files**:
  - 4 spaces for indentation

## IDE Integration

### Android Studio / IntelliJ IDEA

1. Go to **Settings** → **Editor** → **Code Style** → **Kotlin**
2. Click **Set from...** → **Predefined Style** → **Kotlin style guide**
3. The `.editorconfig` file will override specific settings

### Running Spotless on Save (Optional)

You can configure Android Studio to run Spotless automatically:

1. Install the **File Watchers** plugin
2. Add a new File Watcher:
   - **File type**: Kotlin
   - **Program**: `gradlew` (or `gradlew.bat` on Windows)
   - **Arguments**: `spotlessApply`
   - **Working directory**: `$ProjectFileDir$`

## Bypassing Pre-Commit Hook

In rare cases where you need to commit without formatting checks:
```bash
git commit --no-verify -m "your message"
```

**Note:** Only use this in emergencies. Always format your code properly!

## Formatting Rules

### Kotlin Style
- **Indentation**: 4 spaces (no tabs)
- **Max line length**: 120 characters
- **Import organization**: Optimized and organized
- **Trailing commas**: Disabled
- **Wildcard imports**: Allowed (disabled rule)
- **Final newline**: Required
- **Trailing whitespace**: Removed

### Naming Conventions
- Classes: `PascalCase`
- Functions/Variables: `camelCase`
- Constants: `UPPER_SNAKE_CASE`
- Private properties: `_camelCase` (with underscore prefix, optional)

## Troubleshooting

### "Permission denied" on Linux/Mac
```bash
chmod +x .git/hooks/pre-commit
chmod +x setup-hooks.sh
```

### Spotless fails with "No files to format"
This is normal if all files are already formatted correctly.

### Pre-commit hook not running
1. Ensure you ran the setup script
2. Check that `.git/hooks/pre-commit` exists
3. On Linux/Mac, ensure it's executable: `chmod +x .git/hooks/pre-commit`

### Merge conflicts in formatted code
1. Resolve the conflict normally
2. Run `./gradlew spotlessApply` on the resolved files
3. Stage and commit

## CI/CD Integration (Future)

When setting up CI/CD, add this step to fail builds on formatting violations:
```yaml
- name: Check code formatting
  run: ./gradlew spotlessCheck
```

This ensures all code pushed to the repository is properly formatted.
