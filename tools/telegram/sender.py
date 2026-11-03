
import asyncio
import os
from pathlib import Path

import asyncpg
from dotenv import load_dotenv
from telegram import Bot


PROJECT_ROOT = Path(__file__).resolve().parents[1]

load_dotenv(PROJECT_ROOT / ".env")

TOKEN: str = os.getenv("TELEGRAM_TOKEN")


async def main() -> None:
    # Подключение к PostgreSQL
    connection = await asyncpg.connect(
        host=os.getenv("POSTGRES_HOST"),
        port=int(os.getenv("POSTGRES_PORT")),
        database=os.getenv("POSTGRES_DB"),
        user=os.getenv("POSTGRES_USER"),
        password=os.getenv("POSTGRES_PASSWORD"),
    )

    try:
        async with Bot(TOKEN) as bot:

            # Получаем все chat_id
            records = await connection.fetch(
                "SELECT chat_id FROM user_info"
            )

            for record in records:
                chat_id = record["chat_id"]

                if chat_id < 0:
                    username = "Группа"
                    firstname = "Группа"
                    lastname = "Группа"

                else:
                    try:
                        chat = await bot.get_chat(chat_id)

                        username = chat.username or ""
                        firstname = chat.first_name or ""
                        lastname = chat.last_name or ""

                    except Exception as error:
                        print(f"Ошибка chat_id={chat_id}: {error}")
                        continue

                # Обновляем запись
                await connection.execute(
                    """
                    UPDATE user_info
                    SET
                        username = $1,
                        firstname = $2,
                        lastname = $3
                    WHERE chat_id = $4
                    """,
                    username,
                    firstname,
                    lastname,
                    chat_id
                )

                print(
                    f"Обновлён {chat_id}: "
                    f"{username} {firstname} {lastname}"
                )

                await asyncio.sleep(0.1)

    finally:
        await connection.close()


if __name__ == "__main__":
    asyncio.run(main())
