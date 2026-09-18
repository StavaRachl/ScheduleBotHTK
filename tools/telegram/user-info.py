import os
import sys

from telegram import Bot
from dotenv import load_dotenv

load_dotenv()

TOKEN: str = os.getenv('TELEGRAM_TOKEN')


async def main(user_chat_id: int) -> None:
    async with Bot(TOKEN) as bot:
        chat = await bot.get_chat(user_chat_id)

        print(f"ID:          {chat.id}")
        print(f"Username:    @{chat.username}" if chat.username else "Username:    отсутствует")
        print(f"First name:  {chat.first_name}")
        print(f"Last name:   {chat.last_name}")
        print(f"Type:        {chat.type}")


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Использование: command <chat_id>")
        sys.exit(1)

    try:
        chat_id: int = int(sys.argv[1])
    except ValueError:
        print("chat_id должен быть числом")
        sys.exit(1)

    import asyncio

    asyncio.run(main(chat_id))
