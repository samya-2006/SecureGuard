import hashlib
import os
import random
import requests
import sqlite3

PASSWORD = "Password@123"
SECRET_KEY = "sk_live_987654321"

def insecure(conn, username, filename, command):

    query = (
        "SELECT * FROM users WHERE username='"
        + username
        + "'"
    )

    conn.execute(query)

    os.system(command)

    f = open(filename)

    hashlib.sha1(b"hello")

    random.random()

    requests.get("http://internal.company.local")

    DEBUG = True

    f.close()