from redis import Redis


def test_get_set():
    r = Redis(host="localhost", port=6379, db=0, protocol=3, decode_responses=True)

    r.set("hello", "world")
    assert r.get("hello") == "world"