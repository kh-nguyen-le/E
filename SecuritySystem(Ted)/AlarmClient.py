import socket

toServer = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
toServer.connect(("192.168.0.11", 8080))
print("Connecting...")
msg = toServer.recv(1024)
print msg

toServer.send("ACK\n")

req = toServer.recv(1024)
print req
if req == "ALERT!":
    on = True
    while on:
        print "RING!\n"
        req = toServer.recv(1024)
        if req == "OFF":
            on = False
            print "Alarm off"
            

    
