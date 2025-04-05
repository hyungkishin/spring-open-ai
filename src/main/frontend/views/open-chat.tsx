import {MessageInput} from "@vaadin/react-components";
import {AssistantService} from "Frontend/generated/endpoints";
import type {ViewConfig} from '@vaadin/hilla-file-router/types.js';
import React, {useState} from "react";
import {nanoid} from "nanoid";
import {MessageItem} from "Frontend/components/Message";
import MessageList from "Frontend/components/MessageList";

export const config: ViewConfig = {
    menu: {
        title: 'openAi 와 채팅하기'
    }
}

export default function OpenChatView() {
    const [chatId, setChatId] = useState(nanoid());
    const [working, setWorking] = useState(false);
    const [messages, setMessages] = useState<MessageItem[]>([{
        role: 'assistant',
        content: `안녕하세요 ! 무었을 도와드릴까요 ?`
    }])

    function appendToLatestMessage(chunk: string | undefined) {
        setMessages(messages => {
            const latestMessage = messages[messages.length - 1]
            latestMessage.content += chunk
            return [...messages.slice(0, -1), latestMessage]
        })
    }

    function addMessage(message: MessageItem) {
        setMessages(messages => [...messages, message])
    }

    async function sendMessage(message: string) {
        setWorking(true)
        addMessage({
            role: 'user',
            content: message
        })
        let first = true
        AssistantService.chat(chatId, message)
            .onNext(token => {
                if (first && token) {
                    addMessage({
                        role: 'assistant',
                        content: token
                    })

                    first = false
                } else {
                    appendToLatestMessage(token)
                }
            })
            .onError(() => setWorking(false))
            .onComplete(() => setWorking(false))
    }

    return (
        <>
            <MessageList messages={messages} className="flex-grow overflow-scroll"/>
            <MessageInput onSubmit={e => sendMessage(e.detail.value)} className="px-0" disabled={working}/>
        </>
    )

}