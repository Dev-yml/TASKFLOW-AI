# Load .env and start the Spring Boot backend
$envFile = Join-Path $PSScriptRoot ".env"
Get-Content $envFile | Where-Object { $_ -notmatch '^\s*#' -and $_ -match '=' } | ForEach-Object {
    $parts = $_ -split '=', 2
    $key   = $parts[0].Trim()
    $value = $parts[1].Trim()
    [System.Environment]::SetEnvironmentVariable($key, $value, 'Process')
    Write-Host "Set $key"
}

Write-Host "`nStarting Spring Boot on port $env:SERVER_PORT ..."
mvn spring-boot:run
