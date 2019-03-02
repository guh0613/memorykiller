import os
import sys

from pathlib import Path
from zipfile import ZipFile
from fnmatch import fnmatch

def ls(path, ext = "*.java", callback = lambda x:x):
    result = []
    for root, dirs, files in os.walk(path):
        for name in files:
            abspath = os.path.join(root, name)
            if fnmatch(name, ext):
                path = os.path.join(root, name)
                callback(path)
                result.append(path)
    return result


dirname, filename = os.path.split(os.path.abspath(sys.argv[0])) 
path = os.path.realpath(sys.argv[0]) 

tmpDir = Path(dirname, "tmp")

if tmpDir.exists():
    os.system("rm -r tmp")
    os.system("mkdir tmp")
else:
    os.system("mkdir tmp")


pwd = os.getcwd()

data = open("./app/build.gradle").read()
data = data.replace("compile ", "implementation ")
with open("./app/build.gradle", "w") as file:
    file.write(data)

code = os.system("chmod +x gradlew")
if code:
    print("********** chmod error")
code = os.system("./gradlew clean --daemon")
if code:
    print("********** clean error")
code = os.system("./gradlew assembleRelease --daemon")
if code:
    print("********** build error")

apkPath = Path(dirname, "app/build/outputs/apk/release/")
apkPath = ls(apkPath, "*.apk")[0]

os.chdir(pwd)
os.system("cp {} app.apk".format(str(apkPath)))

