# import the necessary packages
from imutils.video import VideoStream
from pyzbar import pyzbar
import argparse
import datetime
import imutils
import time
import cv2
import datetime as dt
import paho.mqtt.client as mqtt

broker_address = "172.20.10.3"      # 브로커 ip
client1 = mqtt.Client("clientPublisher")
client1.connect(broker_address)


# construct the argument parser and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-o", "--output", type=str, default="barcodes.csv",
	help="path to output CSV file containing barcodes")
args = vars(ap.parse_args())

### From there, let’s initialize our video stream and open our CSV file:
# initialize the video stream and allow the camera sensor to warm up
# 비티오 스트림 초기화 및 카메라 센서가 예열되도록 함
print("[INFO] starting video stream...")

# vs = VideoStream(src=0).start()
vs = VideoStream(usePiCamera=True).start()     # 파이 카메라 사용시
time.sleep(2.0)
 
# open the output CSV file for writing and initialize the set of
# barcodes found thus far
# 작성을 위해 출력된 CSV 파일을 열고, 지금까지 찾은 바코드 세트 초기화
csv = open(args["output"], "w")
found = set()

### Let’s begin capturing + processing frames:
# loop over the frames from the video stream
while True:
    # grab the frame from the threaded video stream and resize it to
    # have a maximum width of 400 pixels
    frame = vs.read()
    frame = imutils.resize(frame, width=400)
 
    # find the barcodes in the frame and decode each of the barcodes
    # 프레임에서 바코드를 찾고, 각 바코드들 마다 디코드
    barcodes = pyzbar.decode(frame)


### Let’s proceed to loop over the detected barcodes
# loop over the detected barcodes
    for barcode in barcodes:
        # extract the bounding box location of the barcode and draw
        # the bounding box surrounding the barcode on the image
        # 이미지에서 바코드의 경계 상자부분을 그리고, 바코드의 경계 상자부분(?)을 추출한다. 
        (x, y, w, h) = barcode.rect
        cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 0, 255), 2)
 
        # the barcode data is a bytes object so if we want to draw it
        # on our output image we need to convert it to a string first
        # 바코드 데이터는 바이트 객체이므로, 어떤 출력 이미지에 그리려면 가장 먼저 문자열로 변환해야 한다.
        barcodeData = barcode.data.decode("utf-8")
        barcodeType = barcode.type
 
        # draw the barcode data and barcode type on the image
        # 이미지에서 바코드 데이터와 테입(유형)을 그린다
        text = "{} ({})".format(barcodeData, barcodeType)
        cv2.putText(frame, text, (x, y - 10),
            cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
 
        print(barcodeData)
        client1.publish("QR", barcodeData)  # mqtt 서버 중 QR Topic으로 바코드에 담긴 내용 송신
        # if the barcode text is currently not in our CSV file, write
        # the timestamp + barcode to disk and update the set
        # 현재 바코드 텍스트가 CSV 파일안에 없을경우, timestamp, barcode를 작성하고 업데이트
        if barcodeData not in found:
            csv.write("{},{}\n".format(datetime.datetime.now(),
                barcodeData))
            csv.flush()
            found.add(barcodeData)
        time.sleep(2)
    time.sleep(0.05)
            # show the output frame
    cv2.imshow("Barcode Scanner", frame)
    key = cv2.waitKey(1) & 0xFF
 
    # if the `q` key was pressed, break from the loop
    # q를 누르면 loop를 break함
    if key == ord("q"):
        break

 
# close the output CSV file do a bit of cleanup
print("[INFO] cleaning up...")

csv.close()
cv2.destroyAllWindows()
vs.stop()

