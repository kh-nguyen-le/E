import socket
from PIL import Image

toServer = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
toServer.connect(("192.168.0.11", 8080))
print("Connecting...")
msg = toServer.recv(1024)
print msg

toServer.send("ACK\n")
req =toServer.recv(1024)
print req
if req=="Snapshot Request\n":
    print "Sending picture"
    im = open("C:\\Users\\Teddy\\Pictures\\darkarts.jpg", 'rb')
    imageData = im.read()
    toServer.send(imageData)

print toServer.recv(1024)
