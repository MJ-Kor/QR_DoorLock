import paho.mqtt.client as mqtt
import RPi.GPIO as GPIO
import time

    
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(23, GPIO.OUT)
GPIO.output(23, False)
GPIO.setup(24, GPIO.OUT)
GPIO.output(24, False)


def on_message(client, userdata, message):
    if str(message.payload.decode("utf-8")) == "OPEN":      # result Topic에서 받은 내용이 OPEN이면 초록 led 켜짐
        GPIO.output(23, True)
        time.sleep(3.5)
        GPIO.output(23, False)
    elif str(message.payload.decode("utf-8")) == "CLOSE":   # result Topic에서 받은 내용이 CLOSE이면 빨간 led 켜짐
        GPIO.output(24, True)
        time.sleep(3.5)
        GPIO.output(24, False)
    else:
        GPIO.output(23, False)
        GPIO.output(24, False)
        
broker_address = "172.20.10.3"      # 브로커 ip
client = mqtt.Client()
client.connect(broker_address)
client.subscribe("result")
client.on_message = on_message
client.loop_forever()