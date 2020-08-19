import json
import requests

def main(server_key, to, message_title, message_body):
    headers = {
        'Authorization': 'key= ' + server_key,
        'Content-Type': 'application/json; UTF-8'
    }

    token = []
    for i in to:
        token.append(i[0])

    data = {
        'registration_ids': token,
        'data':{
            'title':'IMCP',
            'body': '아이가 위험합니다',
            'childKey': 'fA%XRq',
            'danger':'off'
        }
    }

    response = requests.post('https://fcm.googleapis.com/fcm/send', headers=headers, data=json.dumps(data))
    print(response)

if __name__ == '__main__':
    #FCM Setting -> Cloud Messaging
    #server_key='AAAAWvoqi8M:APA91bHClwyCE_mQ6ghybuTYiylWcphhb_nBZnw94YeOMrs3NoxCBZc3TXxMzgRt-LtqTCTUfNU4wdOG4glCLeii2NUZUpqF2SZBRDhT55VfZpCiOuhPizGTmvoYFmHkyoK_DmLAsdl7'
    server_key='AAAAK-E7ezg:APA91bHMDCXaStMIhwELOcDylGkg-W8EuUPfl8Jt3d2T5B0kdp_o8-IxLvuf9zeCu_vV8KEbLn9Lu6C9XrAwE8ezlJR2kgcrgAz2G7LSgtYY8Gn24r85qR1zE3zno-xdJlhvdYAwY9sK'
    #핸드폰 토큰값
    token=[('dZnu6k_ZRUiDrUmUnzlx15:APA91bH4z1CQZt2zRfEVp9IARnXGSwMEGkKrhXYwG1-t7Am4ZTMsFgd2WgBnYljmpcWX80_YWSTcwSU3J8HnaPvWjEpbf01UE23eCUji8RrzCCARr2VEFTB08MyANXi8eVPK6lbcooP6',), ('aaa',)]
    #token=['dZnu6k_ZRUiDrUmUnzlx15:APA91bH4z1CQZt2zRfEVp9IARnXGSwMEGkKrhXYwG1-t7Am4ZTMsFgd2WgBnYljmpcWX80_YWSTcwSU3J8HnaPvWjEpbf01UE23eCUji8RrzCCARr2VEFTB08MyANXi8eVPK6lbcooP6']
    #Notification Title
    message_head='FCM_TEST'
    #메시지 내용
    message_body='FCM 테스트입니다.'
    main(server_key, token, message_head, message_body)