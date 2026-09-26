from __future__ import annotations

import sqlite3
from dataclasses import dataclass
from pathlib import Path

import psycopg2
from dotenv import load_dotenv
from psycopg2.extensions import connection as PostgresConnection

PROJECT_ROOT: Path = Path(__file__).resolve().parent.parent.parent

load_dotenv(PROJECT_ROOT / ".env")

@dataclass(frozen=True)
class PostgresConfig:
    host: str
    port: int
    database: str
    user: str
    password: str

@dataclass(frozen=True)
class User:
    chat_id: int
    group_name: str
    dark_theme: bool

def get_required_env(name: str) -> str:
    value: str | None = __import__("os").getenv(name)

    if value is None or value.strip() == "":
        raise RuntimeError(f"Enviroment variable '{value}' is not set")

    return value

def load_postgres_config() -> PostgresConfig:
    return PostgresConfig(
        host=get_required_env("POSTGRES_HOST"),
        port=int(get_required_env("POSTGRES_PORT")),
        database=get_required_env("POSTGRES_DB"),
        user=get_required_env("POSTGRES_USER"),
        password=get_required_env("POSTGRES_PASSWORD")
    )

def create_postgres_table(connection: PostgresConnection) -> None:
    sql: str = """
        CREATE TABLE IF NOT EXISTS users (
            chat_id BIGINT PRIMARY KEY,
            group_name VARCHAR(255) NOT NULL,
            dark_theme BOOLEAN NOT NULL DEFAULT FALSE
        )
    """

    with connection.cursor() as cursor:
        cursor.execute(sql)

def read_users(connection: sqlite3.Connection) -> list[User]:
    users: list[User] = []

    cursor: sqlite3.Cursor = connection.cursor()

    cursor.execute("""
        SELECT chat_id, group_name, dark_theme
        FROM users
    """)

    rows: list[tuple[int, str, bool]] = cursor.fetchall()

    for chat_id, group_name, dark_theme in rows:
        users.append(
            User(
                chat_id=chat_id,
                group_name=group_name,
                dark_theme=bool(dark_theme)
            )
        )
    
    return users

def migrate_users(users: list[User], connection: PostgresConnection) -> None:
    sql: str = """
        INSERT INTO users (
            chat_id,
            group_name,
            dark_theme
        )
        VALUES(%s, %s, %s)
        ON CONFLICT (chat_id)
        DO UPDATE SET
            group_name = EXCLUDED.group_name,
            dark_theme = EXCLUDED.dark_theme
    """

    with connection.cursor() as cursor:
        for user in users:
            cursor.execute(
                sql, (
                    user.chat_id,
                    user.group_name,
                    user.dark_theme
                )
            )

def count_sqlite_users(connection: sqlite3.Connection) -> int:
    cursor: sqlite3.Cursor = connection.cursor()

    cursor.execute("SELECT COUNT(*) FROM users")

    result: tuple[int] | None = cursor.fetchone()

    if result is None:
        raise RuntimeError("Failed to get SQLite users count")

    return result[0]

def count_postgresql_users(connection: PostgresConnection) -> int:
    with connection.cursor() as cursor:
        cursor.execute("SELECT COUNT(*) FROM users")

        result: tuple[int] = cursor.fetchone()

        if result is None:
            raise RuntimeError("Failed to get PostgreSQL users count")

        return result[0]

def verify_migration(
        sqlite_connection: sqlite3.Connection, 
        postgresql_connection: PostgresConnection
    ) -> None:

    sqlite_count: int = count_sqlite_users(sqlite_connection)

    postgresql_count: int = count_postgresql_users(postgresql_connection)

    print(f"SQLite {sqlite_count}")
    print(f"PostgreSQL {postgresql_count}")

    if sqlite_count != postgresql_count:
        raise RuntimeError(
            f"User count mismatch: "
            f"SQLite={sqlite_count}"
            f"PostgreSQL={postgresql_count}"
        )

    print("Migration verification passed.")

def main() -> None:
    sqlite_connection: sqlite3.Connection | None = None
    postgresql_connection: PostgresConnection | None = None

    try:
        print("Loading configuration...")

        config: PostgresConfig = load_postgres_config()

        sqlite_database: Path = (
            PROJECT_ROOT / "runtime" / "data" / "schedule.db"
        )

        print(f"SQLite: {sqlite_database}")

        sqlite_connection = sqlite3.connect(
            sqlite_database
        )

        print("Connecting to PostgreSQL...")

        postgresql_connection = psycopg2.connect(
            host=config.host,
            port=config.port,
            dbname=config.database,
            user=config.user,
            password=config.password
        )

        print("Creating PostgreSQL table...")

        create_postgres_table(postgresql_connection)

        print("Reading SQLite users...")

        users: list[User] = read_users(sqlite_connection)

        print(f"Found users: {len(users)}")

        print("Migrating users...")

        migrate_users(users, postgresql_connection)

        print("Verifying migration...")

        verify_migration(sqlite_connection, postgresql_connection)

        postgresql_connection.commit()

        print("Migration complete successfully.")

    except Exception:
        if postgresql_connection is not None:
            postgresql_connection.rollback()
        raise
    finally:
        if sqlite_connection is not None:
            sqlite_connection.close()

        if postgresql_connection is not None:
            postgresql_connection.close()

if __name__ == "__main__":
    main()