PROJECT_DIR="YOUR_DIR"

set -euo pipefail

echo "Starting deployment..."
cd $PROJECT_DIR
echo "current derictory $(pwd)"

echo "checking status git repository"
git status --short

echo "pulling github repository"
git pull

echo "building project..."
mvn clean package

echo "building and restarting project..."
docker compose up -d --build java

echo "deployment complete"


