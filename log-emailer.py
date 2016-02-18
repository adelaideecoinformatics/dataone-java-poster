#!/usr/bin/python
import smtplib
import sys
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText

SENDER_EMAIL_ADDRESS = 'REPLACE.ME@example.com' # TODO replace me
EMAIL_SUBJECT = 'Log output from eml_pusher'
EMAIL_SERVER_PASSWORD = 'REPLACE.ME' # TODO replace me
EMAIL_SERVER_HOSTNAME = 'smtp.gmail.com'
EMAIL_SERVER_PORT = '587'

def main():
  if (len(sys.argv) < 2):
    print 'ERROR: first arg needs to be a file to read as the email content'
    print 'usage: '+sys.argv[0]+' <file>'
    print '   eg: '+sys.argv[0]+' output.log'
    return
  content = open(sys.argv[1], 'r').read()
  recipient = 'REPLACE.ME@example.com' # TODO replace me
  doSendMail(recipient, content)

def doSendMail(recipient, content):
  me = SENDER_EMAIL_ADDRESS
  you = recipient
  
  # Create message container - the correct MIME type is multipart/alternative.
  msg = MIMEMultipart('alternative')
  msg['Subject'] = EMAIL_SUBJECT
  msg['From'] = me
  msg['To'] = you
  part1 = MIMEText(content, 'plain')
  msg.attach(part1)

  username = me
  password = EMAIL_SERVER_PASSWORD
  
  # Sending the mail  
  server = smtplib.SMTP(EMAIL_SERVER_HOSTNAME, EMAIL_SERVER_PORT)
  server.ehlo()
  server.starttls()
  server.ehlo()
  server.login(username,password)
  server.sendmail(me, you, msg.as_string())
  server.close()

if __name__ == "__main__":
    main()
