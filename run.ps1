# run.ps1

# Change to the directory where the script is located
Set-Location $PSScriptRoot

# Set MAVEN_OPTS environment variable
$env:MAVEN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED"

# Validate arguments
if (-not $args[0]) {
    Write-Host "Error: No s'ha especificat la classe principal."
    Write-Host "Ús: .\run.ps1 <Nom.De.La.Classe.Main> [args...]"
    exit 1
}

# First argument is the main class
$mainClass = $args[0]

# Collect program args (if any)
if ($args.Length -gt 1) {
    $programArgs = $args[1..($args.Length - 1)] -join " "
} else {
    $programArgs = ""
}

Write-Host "Setting MAVEN_OPTS to: $env:MAVEN_OPTS"
Write-Host "Main Class: $mainClass"
Write-Host "Program Args: $programArgs"

# Build exec arguments
$execArgs = "-Dexec.mainClass=$mainClass"
if ($programArgs -ne "") {
    $execArgs += " -Dexec.args=`"$programArgs`""
}

Write-Host "Exec args: $execArgs"

# Execute mvn command
mvn clean test-compile exec:java -PrunMain $execArgs
