@echo off
setlocal enabledelayedexpansion
for /f "tokens=1" %%a in ('jps ^| findstr product-show-web.jar') do taskkill /f /pid %%a