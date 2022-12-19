from pathlib import Path
from subprocess import PIPE, STDOUT, run

HERE = Path(__file__).resolve().parent
print("here", HERE)
TEST = HERE.parent
print("TEST", TEST)

ROOT = TEST.parent
print("ROOT", ROOT)

SRC = ROOT / "src"
print("SRC", SRC)


class CompilationError(Exception):
    pass


def compile(source: Path, cflags=[], ldadd=[]):
    binary = source.with_suffix(".so")

    result = run(
        ["gcc", "-shared", *cflags, "-o", str(binary), str(source), *ldadd],
        stdout=PIPE,
        stderr=STDOUT,
        cwd=SRC,
    )

    if result.returncode == 0:
        return

    raise CompilationError(result.stdout.decode())
