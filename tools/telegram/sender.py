import sys
import os

from dotenv import load_dotenv

load_dotenv()

BOT_TOKEN: str = os.getenv("TELEGRAM_TOKEN_DEV", "")

def main() -> None:
    print(BOT_TOKEN)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Use: sender.py <message>")
        sys.exit(1)

    main()