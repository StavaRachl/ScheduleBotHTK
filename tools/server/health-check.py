import os
import subprocess

import requests

from dotenv import load_dotenv

load_dotenv()

BOT_TOKEN: str = os.getenv("TELEGRAM_TOKEN")

SERVICE_NAME: str = "bot"


def check_service() -> bool:
    result = subprocess.run(
        ["systemctl", "is-active", SERVICE_NAME],
        capture_output=True,
        text=True
    )

    if result.stdout.strip() == "active":
        print("Java bot: RUNNING")
        return True

    print("Java bot: STOPPED")
    return False


def check_telegram() -> bool:
    url = f"https:api.telegram.org/bot{BOT_TOKEN}/getMe"

    try:
        response = requests.get(url, timeout=5)

        if response.ok and response.json().get("ok"):
            bot = response.json()["result"]
            print(f"Telegram API: OK (@{bot['username']}")
            return True

    except requests.RequestException as e:
        print(f"Telegram API: ERROR ({e})")
        return False


def check_internet() -> bool:
    try:
        requests.get("https://google.com", timeout=5)
        print("Internet: OK")
        return True
    except requests.RequestException as e:
        print(f"Internet: ERROR ({e})")
        return False


def main():
    print("HEALTH CHECK")

    print()

    results = [
        print("CHECKING SERVICE.    (1/3)"),
        check_service(),
        print("CHECKING TELEGRAM..  (2/3"),
        check_telegram(),
        print("CHECKING INTERNET... (3/3)"),
        check_internet()
    ]

    if all(results):
        print("STATUS: OK")
    else:
        print("STATUS: ERROR")


if __name__ == "__main__":
    main()
