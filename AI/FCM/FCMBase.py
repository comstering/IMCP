import json
import requests

def main(server_key, token, message_title, message_body):
    headers = {
        'Authorization': 'key= ' + server_key,
        'Content-Type': 'application/json; UTF-8'
    }

    data = {
        'registration_ids': [
            token
        ],
        'data':{
            'title':'FCM TEST',
            'body': message_body,
            'SOS':'off'
        }
    }

    response = requests.post('https://fcm.googleapis.com/fcm/send', headers=headers, data=json.dumps(data))
    print(response)

if __name__ == '__main__':
    #FCM Setting -> Cloud Messaging
    #server_key='AAAAWvoqi8M:APA91bHClwyCE_mQ6ghybuTYiylWcphhb_nBZnw94YeOMrs3NoxCBZc3TXxMzgRt-LtqTCTUfNU4wdOG4glCLeii2NUZUpqF2SZBRDhT55VfZpCiOuhPizGTmvoYFmHkyoK_DmLAsdl7'
    server_key='AAAAK-E7ezg:APA91bHMDCXaStMIhwELOcDylGkg-W8EuUPfl8Jt3d2T5B0kdp_o8-IxLvuf9zeCu_vV8KEbLn9Lu6C9XrAwE8ezlJR2kgcrgAz2G7LSgtYY8Gn24r85qR1zE3zno-xdJlhvdYAwY9sK'
    #핸드폰 토큰값
    token='c2BldVHpR7moJxpRtpeueq:APA91bEDHxqxtTlqBFI4dQk1w5SohlDUKi27s8fkDRNx8ubyK7_s4CTpxNYB_XfQCNCdPZvsAniUaZzqXpTO5400Kz8a0bNce-jcY-TszMaisVVVLhJEQ0vwtNP1PPzsmPPapa98r-Fk'
    #Notification Title
    message_head='FCM_TEST'
    #메시지 내용
    message_body='FCM 테스트입니다.'
    main(server_key, token, message_head, message_body)