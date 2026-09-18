import sqlite3
from sqlite3 import Cursor, Connection


class User:
    def __init__(self, user_id: int, group_name: str, dark_theme: bool):
        self._id = user_id
        self._group = group_name
        self._theme = dark_theme

    @property
    def id(self) -> id:
        return self._id

    @property
    def group(self) -> str:
        return self._group

    @property
    def theme(self) -> bool:
        return self._theme


def get_user() -> list[User]:
    connection: Connection = sqlite3.connect("../../runtime/data/schedule.db")

    cursor: Cursor = connection.execute("SELECT * FROM users")

    rows = cursor.fetchall()

    connection.close()

    return [
        User(
            user_id=row[0],
            group_name=row[1],
            dark_theme=bool(row[2])
        )
        for row in rows
    ]


def main():
    users: list[User] = get_user()

    groups: list = []

    for user in users:
        groups.append(user.group)

    unique = list(set(groups))

    for group in unique:
        print(group)


if __name__ == "__main__":
    main()
