import socket
import wiringpi
import sys
import time
import os



wiringpi.wiringPiSetupGpio()                # Initialise wiringpi GPIO
wiringpi.pinMode(18,2)                      # Set up GPIO 18 to PWM mode
wiringpi.pinMode(17,1)                      # GPIO 17 to output
wiringpi.pwmWrite(18,0)                     # set pwm to zero initially

toServer = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
toServer.connect(("serverIPgoeshere", 4444))
print("Connecting...")
msg = toServer.recv(1024)
print msg

toServer.send("Connected\n")




#reset ports will be called on exit so that the motor and everything is turned off so it doesnt accidentally chop someones finger of or something			
def reset_ports():                          # resets the ports for a safe exit
    wiringpi.pwmWrite(18,0)                 # set pwm to zero
    wiringpi.digitalWrite(18, 0)            # ports 17 & 18 off
    wiringpi.digitalWrite(17, 0)
    wiringpi.pinMode(17,0)                  # set ports back to input mode
    wiringpi.pinMode(18,0)


#this function turns the motor in the direction specified
def turn(direction): 
	if direction == "right":
		wiringpi.digitalWrite(17, 0)  # port 17 on for rotation other way
		wiringpi.pwmWrite(18,205) #value between 0-1024, lets go for 20% speed so 1024/5 ~= 205
		sleep(#insert time it would take to rotate however far we want then stop it, assuming 5 rpm motor, at 1 rpm due to 20% speed, 6 degrees per second, if we wanted 15 degree rotation sleep for ~2.5 seconds)
		wiringpi.pwmWrite(18,0)           # set pwm to zero 
	if direction == "left":
		wiringpi.digitalWrite(17, 1)  # port 17 on for rotation other way
		wiringpi.pwmWrite(18,205) #value between 0-1024, lets go for 20% speed so 1024/5 ~= 205
		sleep(#insert time it would take to rotate however far we want then stop it, assuming 5 rpm motor, at 1 rpm due to 20% speed, 6 degrees per second, if we wanted 15 degree rotation sleep for ~2.5 seconds)
		wiringpi.pwmWrite(18,0)          # set pwm to zero 

try:
	while(True):
		req = toServer.recv(1024)
		print req
		turn(req)
			
except KeyboardInterrupt:           # trap a CTRL+C keyboard interrupt
    reset_ports()                   # reset ports on interrupt 

reset_ports()       # reset ports on normal exit
