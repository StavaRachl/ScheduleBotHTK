import subprocess


def run(command):
    result = subprocess.run(
        command,
        capture_output=True,
        text=True
    )

    return result.stdout.strip()


def main():