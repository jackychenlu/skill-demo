# Browser-Use SDK Supplement

Reusable notes for SDK-based browser tasks in this workspace.

## Script location
- SDK runner: run_browser.py

## Required environment
- BROWSER_USE_API_KEY is read from .env or the environment.

## Remote mode
- The script uses Agent(..., use_remote=True) to run against the cloud browser.

## Run command (Windows)
- D:/maple/AI/skillTest/.venv/Scripts/python.exe run_browser.py

## Dependencies
- Install once per venv: python -m pip install browser-use

## Common issue
- ModuleNotFoundError: No module named 'browser_use' -> install the package in the active venv.
