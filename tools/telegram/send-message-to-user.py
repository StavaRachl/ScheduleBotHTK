import os
import sys

from telegram import Bot
from dotenv import load_dotenv

load_dotenv()

TOKEN: str = os.getenv("TELEGRAM_TOKEN")


async def main(user_chat_id: int, message: str) -> None:
    async with Bot(token=TOKEN) as bot:
        await bot.sendMessage(chat_id=user_chat_id, text=message)
        print(f"message {message} complete send for {user_chat_id}")

if __name__ == '__main__':
    if len(sys.argv) != 3:
        print("command <chat_id> <text>")
        sys.exit(1)

    text: str = sys.argv[2]

    try:
        chat_id = int(sys.argv[1])
    except ValueError:
        print("chat_id must be number")
        sys.exit(1)

    import asyncio

    asyncio.run(main(user_chat_id=chat_id, message=text))
