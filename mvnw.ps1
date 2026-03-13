# Save Maven path and command that succeeded
$MVN_PATH = "C:\Software\idea-2025.3.1.win\plugins\maven\lib\maven3\bin\mvn.cmd"

function Run-Maven {
    param(
        [string[]]$Arguments
    )
    & $MVN_PATH $Arguments
}

# Default to running tests if no arguments are provided
if ($args.Count -eq 0) {
    Write-Host "Running all tests using discovered Maven path..."
    Run-Maven test
} else {
    Run-Maven $args
}
