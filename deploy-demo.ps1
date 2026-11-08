ssh -p PORT USERNAME@IP_ADDRESS "bash /DEMO/deploy.sh"

if ($LASTEXITCODE -ne 0) {
    Write-Error "Deployment failed!"
    exit 1
}