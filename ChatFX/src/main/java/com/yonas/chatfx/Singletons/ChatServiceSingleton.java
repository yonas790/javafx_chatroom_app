package com.yonas.chatfx.Singletons;
import com.yonas.chatfx.Services.Impl.ChatService;
import com.yonas.chatfx.Services.Interfaces.IChatService;

public class ChatServiceSingleton {
    private static final IChatService chatServiceInstance = new ChatService();

    public static IChatService getChatService() {
        return chatServiceInstance;
    }
}