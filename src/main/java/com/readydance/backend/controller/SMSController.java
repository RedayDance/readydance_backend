package com.readydance.backend.controller;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.MessageListRequest;
import net.nurigo.sdk.message.request.MultipleMessageSendingRequest;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.MessageListResponse;
import net.nurigo.sdk.message.response.MultipleMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class SMSController {

    DefaultMessageService messageService;

    //Todo : API KEY Ini file로 관리
    public SMSController() {
        this.messageService = NurigoApp.INSTANCE.initialize("NCSJXM6XEO4OKWP9", "7LSYBHRA7CBDKXFVFGOSRESTFGPQZKOJ", "https://api.solapi.com");
    }

    @GetMapping("/get-message-list")
    public void getMessageList() {
        MessageListResponse response = this.messageService.getMessageList(new MessageListRequest());

        System.out.println(response);
    }

    @PostMapping("/send-one")
    public void sendOne() {
        Message message = new Message();
        message.setFrom("01092595158");
        message.setTo("01092595158");
        message.setText("한글 45자, 영자 90자 이하 입력되면 자동으로 SMS타입의 메시지가 추가됩니다.");
        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        System.out.println(response);
    }

    @GetMapping("/send-many")
    public void sendMany() {
        ArrayList<Message> messageList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Message message = new Message();
            message.setFrom("01092595158");
            message.setTo("0100001000" + i);
            message.setText("한글 45자, 영자 90자 이하 입력되면 자동으로 SMS타입의 메시지가 추가됩니다." + i);

            messageList.add(message);
        }

        MultipleMessageSendingRequest request = new MultipleMessageSendingRequest(messageList);
        MultipleMessageSentResponse response = this.messageService.sendMany(request);

        System.out.println(response);
    }
}
