package com.yonas.chatfx.Singletons;
import com.yonas.chatfx.Services.Impl.LoginService;
import com.yonas.chatfx.Services.Interfaces.ILoginService;

public class LoginServiceSingleton {
    private static final ILoginService loginServiceInstance = new LoginService();

    public static ILoginService getLoginService() {
        return loginServiceInstance;
    }
}
